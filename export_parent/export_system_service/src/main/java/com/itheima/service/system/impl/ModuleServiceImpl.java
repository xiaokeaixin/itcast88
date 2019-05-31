package com.itheima.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.commons.utils.UtilFuns;
import com.itheima.dao.system.ModuleDao;
import com.itheima.domain.system.Module;
import com.itheima.service.system.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleDao moduleDao;

    @Override
    public Module findById(String moduleId) {
        return moduleDao.findById(moduleId);
    }

    @Override
    public void delete(String moduleId) {
        moduleDao.delete(moduleId);
    }

    @Override
    public void save(Module module) {
        //1.设置id
        module.setId(UtilFuns.generateId());
        //2.保存
        moduleDao.save(module);
    }

    @Override
    public void update(Module module) {
        moduleDao.update(module);
    }

    @Override
    public PageInfo findAll(int page, int size) {
        //1.设置分页条件

        PageHelper.startPage(page,size);
        //2.查询带分页的结果集
        List<Module> moduleList = moduleDao.findAll();
        //3.返回pageInfo
        return new PageInfo(moduleList);
    }

    @Override
    public List<Module> findAll() {
        return moduleDao.findAll();
    }
}
