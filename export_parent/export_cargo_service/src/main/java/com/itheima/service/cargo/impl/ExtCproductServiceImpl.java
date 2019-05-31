package com.itheima.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.commons.utils.UtilFuns;
import com.itheima.dao.cargo.ContractDao;
import com.itheima.dao.cargo.ExtCproductDao;
import com.itheima.domain.cargo.Contract;
import com.itheima.domain.cargo.ExtCproduct;
import com.itheima.domain.cargo.ExtCproductExample;
import com.itheima.service.cargo.ExtCproductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Service
public class ExtCproductServiceImpl implements ExtCproductService {

    @Autowired
    private ExtCproductDao extCproductDao;

    @Autowired
    private ContractDao contractDao;

    @Override
    public void save(ExtCproduct extCproduct) {
        //1.设置附件的id
        extCproduct.setId(UtilFuns.generateId());
        //2.设置附件的总金额(条件是：数量和单价都有)
        double amount = 0d;
        if(extCproduct.getCnumber() != null && extCproduct.getPrice() != null){
            amount = extCproduct.getCnumber() * extCproduct.getPrice();
        }
        //3. 设置附件的总金额
        extCproduct.setAmount(amount);
        //4.保存附件
        extCproductDao.insertSelective(extCproduct);
        //5.根据id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //6.设置购销合同的总金额
        contract.setTotalAmount(contract.getTotalAmount() + amount);
        //7.设置合同的附件款数
        contract.setExtNum(contract.getExtNum()+1);
        //8.更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void update(ExtCproduct extCproduct) {
        //1.取出附件原来的总金额
        ExtCproduct dbExtCproduct = extCproductDao.selectByPrimaryKey(extCproduct.getId());
        double oldAmount = dbExtCproduct.getAmount();
        //2.设置附件的总金额(条件是：数量和单价都有)
        double amount = 0d;
        if(extCproduct.getCnumber() != null && extCproduct.getPrice() != null){
            amount = extCproduct.getCnumber() * extCproduct.getPrice();
        }
        //3. 设置附件的总金额
        extCproduct.setAmount(amount);
        //4.更新附件
        extCproductDao.updateByPrimaryKeySelective(extCproduct);
        //5.根据id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //6.设置购销合同的总金额
        contract.setTotalAmount(contract.getTotalAmount()-oldAmount + amount);
        //7.更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
        //1.根据id查询附件
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(id);
        //2.取出附件的总金额
        double amount = extCproduct.getAmount();
        //3.删除附件
        extCproductDao.deleteByPrimaryKey(id);
        //4.获取购销合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //5.修改购销合同的总金额，减去删除的附件总金额
        contract.setTotalAmount(contract.getTotalAmount() - amount);
        //6.修改购销合同的附件数，减1
        contract.setExtNum(contract.getExtNum() - 1);
        //7.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public ExtCproduct findById(String id) {
        return extCproductDao.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(ExtCproductExample example, int page, int size) {
        PageHelper.startPage(page,size);
        List<ExtCproduct> extCproductList = extCproductDao.selectByExample(example);
        return new PageInfo(extCproductList);
    }
}
