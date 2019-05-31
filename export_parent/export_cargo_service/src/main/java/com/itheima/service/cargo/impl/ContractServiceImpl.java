package com.itheima.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.commons.utils.UtilFuns;
import com.itheima.dao.cargo.ContractDao;
import com.itheima.domain.cargo.Contract;
import com.itheima.domain.cargo.ContractExample;
import com.itheima.service.cargo.ContractService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractDao contractDao;

    @Override
    public Contract findById(String id) {
        return contractDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Contract contract) {
        //1.设置购销合同的id
        contract.setId(UtilFuns.generateId());
        //2.设置合同的总金额为0
        contract.setTotalAmount(0d);
        //3.设置货物数和附件数为0
        contract.setProNum(0);
        contract.setExtNum(0);
        //4.设置合同的状态为草稿：0草稿  1已上报  2已报运
        contract.setState(0);
        //5.设置创建时间
        contract.setCreateTime(new Date());
        contract.setUpdateTime(new Date());
        //6.保存
        contractDao.insertSelective(contract);
    }

    @Override
    public void update(Contract contract) {
        contract.setUpdateTime(new Date());
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
        contractDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(ContractExample example, int page, int size) {
        //1.设置分页条件
        PageHelper.startPage(page,size);
        //2.查询所有
        List<Contract> contractList = contractDao.selectByExample(example);
        //3.返回分页对象
        return new PageInfo(contractList);
    }
}
