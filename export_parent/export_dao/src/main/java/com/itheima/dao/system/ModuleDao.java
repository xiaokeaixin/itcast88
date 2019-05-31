package com.itheima.dao.system;

import com.itheima.domain.system.Module;

import java.util.List;

/**
 */
public interface ModuleDao {

    //根据id查询
    Module findById(String moduleId);

    //根据id删除
    int delete(String moduleId);

    //添加用户
    int save(Module module);

    //更新用户
    int update(Module module);

    //查询全部
    List<Module> findAll();

    //根据用户id查询用户可以操作的模块集合
    List<Module> finModuleByUserId(String userId);

    //根据belong查询模块信息
    List<Module> finModuleByBelong(Integer belong);
}