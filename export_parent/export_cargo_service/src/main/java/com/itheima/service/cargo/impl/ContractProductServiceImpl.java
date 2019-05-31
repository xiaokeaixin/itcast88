package com.itheima.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.commons.utils.UtilFuns;
import com.itheima.dao.cargo.ContractDao;
import com.itheima.dao.cargo.ContractProductDao;
import com.itheima.dao.cargo.ExtCproductDao;
import com.itheima.domain.cargo.*;
import com.itheima.service.cargo.ContractProductService;
import com.itheima.vo.ContractProductVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Service
public class ContractProductServiceImpl implements ContractProductService {

    @Autowired
    private ContractProductDao contractProductDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ExtCproductDao extCproductDao;

    @Override
    public void save(ContractProduct contractProduct) {
        //1.设置货物的id
        contractProduct.setId(UtilFuns.generateId());
        //2.判断是否提供了数量和单价
        double amount = 0d;
        if(contractProduct.getCnumber() != null && contractProduct.getPrice() != null){
            amount = contractProduct.getPrice() * contractProduct.getCnumber();//当前货物的总金额
        }
        //3.设置当前货物的总金额
        contractProduct.setAmount(amount);
        //4.保存货物
        contractProductDao.insertSelective(contractProduct);
        //5.根据id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        //6.设置购销合同的总金额
        contract.setTotalAmount(contract.getTotalAmount() + amount);
        //7.设置货物的数量（合同中货物的款数）
        contract.setProNum(contract.getProNum()+1);
        //8.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void update(ContractProduct contractProduct) {
        //1.把数据库中当前货物的总金额取出来
        ContractProduct dbContractProduct = contractProductDao.selectByPrimaryKey(contractProduct.getId());
        double oldAmount = dbContractProduct.getAmount();
        //1.判断是否提供了数量和单价
        double amount = 0d;
        if(contractProduct.getCnumber() != null && contractProduct.getPrice() != null){
            amount = contractProduct.getPrice() * contractProduct.getCnumber();//当前货物的总金额
        }
        //2.设置当前货物的总金额
        contractProduct.setAmount(amount);
        //3.更新货物
        contractProductDao.updateByPrimaryKeySelective(contractProduct);
        //4.根据id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        //5.设置购销合同的总金额
        contract.setTotalAmount(contract.getTotalAmount()- oldAmount + amount);
        //6.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
        //1.先根据id查询货物信息
        ContractProduct contractProduct = contractProductDao.selectByPrimaryKey(id);
        //2.取出货物的总金额
        double contractProductAmount = contractProduct.getAmount();
        //3.取出货物下的所有附件：两种方式：
        // 第一种自己写SQL语句条件查询（由于mybatis的generator已经生成好了，我们只要组装查询条件即可）
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractProductIdEqualTo(id);
        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(extCproductExample);
        //第二种：使用mybatis的关联查询，需要配置一对多的关系，可以在映射配置文件中配置collection (由于是一对多，那么多的一方应该是延迟加载)
//        List<ExtCproduct> extCproducts = contractProduct.getExtCproducts();
        //4.遍历集合，取出每个附件
        double extCamount = 0d;
        for(ExtCproduct extCproduct : extCproducts){
            extCamount += extCproduct.getAmount();//取出所有附件的总金额之和
            //5.删除附件
            extCproductDao.deleteByPrimaryKey(extCproduct.getId());
        }
        //6.删除货物
        contractProductDao.deleteByPrimaryKey(id);
        //7.查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        //8.设置合同的总金额：当前金额-货物金额-附件金额
        contract.setTotalAmount(contract.getTotalAmount() - contractProductAmount - extCamount);
        //9.设置合同的货物数
        contract.setProNum(contract.getProNum() - 1);
        //10.设置合同的附件数
        contract.setExtNum(contract.getExtNum() - extCproducts.size());
        //11.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);

    }

    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(ContractProductExample example, int page, int size) {
        PageHelper.startPage(page,size);
        List<ContractProduct> contractProductList = contractProductDao.selectByExample(example);
        return new PageInfo(contractProductList);
    }

    @Override
    public List<ContractProductVo> findContractProductByShipTime(String companyId, String shipTime) {
        return contractProductDao.findContractProductByShipTime(companyId, shipTime);
    }

    @Override
    public void batchSave(List<ContractProduct> contractProducts) {
        for (ContractProduct contractProduct : contractProducts) {
            this.save(contractProduct);
        }
    }
}
