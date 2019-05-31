package com.itheima.service.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Module;

import java.util.List;

/**
 * 模块的业务层接口
 */
public interface ModuleService {
    //根据id查询
    Module findById(String moduleId);

    //根据id删除
    void delete(String moduleId);

    //添加用户
    void save(Module module);

    //更新用户
    void update(Module module);

    //查询全部
    PageInfo findAll(int page, int size);

    //查询全部，不带分页
    List<Module> findAll();
}
