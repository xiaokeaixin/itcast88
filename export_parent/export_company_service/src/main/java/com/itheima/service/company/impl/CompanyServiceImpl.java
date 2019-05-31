package com.itheima.service.company.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.company.CompanyDao;
import com.itheima.domain.company.Company;
import com.itheima.service.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDao companyDao;

    @Override
    public List<Company> findAll() {
        return companyDao.findAll();
    }

    @Override
    public Company findById(String id) {
        return companyDao.findById(id);
    }

    @Override
    public void save(Company company) {
        //设置主键:UUID
        String id = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        company.setId(id);
        companyDao.save(company);
    }

    @Override
    public void update(Company company) {
        companyDao.update(company);
    }

    @Override
    public void delete(String id) {
        companyDao.delete(id);
    }

    /*@Override
    public PageResult findPage(int page, int size) {
        //1.查询总记录条数
        long total = companyDao.findTotal();
        //2.查询带有分页的结果集
        List<Company> list = companyDao.findPage((page - 1) * size, size);
        //3.创建返回值对象
        PageResult pageResult = new PageResult(total, list, page, size);
        //4.返回
        return pageResult;
    }*/

    /**
     * 使用mybatis的pagehelper插件实现分页
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo findPageByHelper(int page, int size) {
        //1.使用它的静态方法设置分页信息
        PageHelper.startPage(page, size);
        //2.不用怀疑，直接调用查询所有方法
        List<Company> companies = companyDao.findAll();//不是调用带分页的查询方法
        //3.返回
        return new PageInfo(companies);
    }
}
