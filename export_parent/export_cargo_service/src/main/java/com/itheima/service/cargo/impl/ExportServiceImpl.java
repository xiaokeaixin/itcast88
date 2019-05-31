package com.itheima.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.commons.utils.UtilFuns;
import com.itheima.dao.cargo.*;
import com.itheima.domain.cargo.*;
import com.itheima.service.cargo.ExportService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private ExportDao exportDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ContractProductDao contractProductDao;

    @Autowired
    private ExportProductDao exportProductDao;

    @Autowired
    private ExtEproductDao extEproductDao;

    @Override
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Export export) {
        //补全报运单中的必要信息
        String exportId = UtilFuns.generateId();
        export.setId(exportId);//主键信息
        export.setInputDate(new Date());//报运单创建时间
        export.setState(0);//报运单状态为草稿
        //取出报运单下的购销合同id集合
        String[] contractIds = export.getContractIds().split(",");//每个元素就是合同的id
        //遍历购销合同id数组
        StringBuilder ss = new StringBuilder();
        for (String contractId : contractIds) {
            //根据购销合同id查询购销合同
            Contract contract = contractDao.selectByPrimaryKey(contractId);
            //拼接合同号
            ss.append(contract.getContractNo()).append(" ");
            //购销合同当已经生成报运单之后 状态要改为2
            contract.setState(2);
            //更新购销合同
            contractDao.updateByPrimaryKeySelective(contract);
        }
        //设置报运单中的合同号
        ss.delete(ss.toString().length() - 1, ss.toString().length());
        export.setCustomerContract(ss.toString());
        //定义查询货物的条件对象
        ContractProductExample contractProductExample = new ContractProductExample();
        //把购销合同数组转成集合
        List<String> contractList = Arrays.asList(contractIds);
        //拼装查询条件
        contractProductExample.createCriteria().andContractIdIn(contractList);
        //取出货物
        List<ContractProduct> cps = contractProductDao.selectByExample(contractProductExample);
        //遍历合同下货物的集合
        int extNum = 0;
        for (ContractProduct cp : cps) {
            //创建报运单商品对象
            ExportProduct ep = new ExportProduct();
            //把合同下货物的数据填充到商品里面去
            BeanUtils.copyProperties(cp, ep, new String[]{"id"});
            //设置商品的id
            String exportProductId = UtilFuns.generateId();
            ep.setId(exportProductId);
            //给商品设置报运单的id
            ep.setExportId(exportId);
            //保存商品信息
            exportProductDao.insertSelective(ep);
            //取出维哥货物的附件
            List<ExtCproduct> extCproducts = cp.getExtCproducts();
            extNum += extCproducts.size();
            //遍历货物的附件
            for (ExtCproduct extCproduct : extCproducts) {
                //创建报运单附件对象
                ExtEproduct extEproduct = new ExtEproduct();
                //数据拷贝
                BeanUtils.copyProperties(extCproduct, extEproduct, new String[]{"id"});
                //设置附件的id
                extEproduct.setId(UtilFuns.generateId());
                //设置商品id
                extEproduct.setExportProductId(exportProductId);
                //设置报运单id
                extEproduct.setExportId(exportId);
                //保存商品的附件
                extEproductDao.insertSelective(extEproduct);
            }
        }
        //商品数和附件数
        export.setProNum(cps.size());
        export.setExtNum(extNum);
        //保存报运单
        exportDao.insertSelective(export);
    }

    @Override
    public void update(Export export) {
        //更新报运单
        exportDao.updateByPrimaryKeySelective(export);
        //判断是否有商品
        if (export.getExportProducts() != null) {
             //更新报运单中的商品
            List<ExportProduct> exportProducts = export.getExportProducts();
            //遍历集合
            for (ExportProduct exportProduct : exportProducts) {
                //更新报运单下的商品
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
    }

    /**
     * 删除报运单 删除报运单商品 附件 更新购销合同状态
     * @param id
     */
    @Override
    public void delete(String id) {
        //根据id查询出来报运单对象
        Export export = exportDao.selectByPrimaryKey(id);
        //取出报运单下所有的合同id
        String[] contractIds = export.getContractIds().split(",");//它里面使用逗号分隔的合同id
        //遍历合同id
        for (String contractId : contractIds) {
            //创建合同对象
            Contract contract = new Contract();
            //设置合同id和状态
            contract.setId(contractId);
            contract.setState(1);//吧状态改为已上报
            //更新购销合同
            contractDao.updateByPrimaryKeySelective(contract);
        }
        //删除报运单
        exportDao.deleteByPrimaryKey(id);
        //创建报运单商品的条件查询对象
        ExportProductExample exportProductExample = new ExportProductExample();
        //设置商品的查询条件
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        //获取报运单下的商品集合
        List<ExportProduct> exportProducts = exportProductDao.selectByExample(exportProductExample);
        //遍历集合，删除商品
        for (ExportProduct exportProduct : exportProducts) {
            exportProductDao.deleteByPrimaryKey(exportProduct.getId());
        }
        //常见报运单附件的条件查询对象
        ExtEproductExample extEproductExample = new ExtEproductExample();
        //设置附件的查询条件
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        //查询报运单的附件
        List<ExtEproduct> extEproducts = extEproductDao.selectByExample(extEproductExample);
        //遍历
        for (ExtEproduct extEproduct : extEproducts) {
            extEproductDao.deleteByPrimaryKey(extEproduct.getId());
        }
    }

    @Override
    public PageInfo findAll(ExportExample example, int page, int size) {
        PageHelper.startPage(page, size);
        List<Export> exportList = exportDao.selectByExample(example);
        return new PageInfo(exportList);
    }
}
