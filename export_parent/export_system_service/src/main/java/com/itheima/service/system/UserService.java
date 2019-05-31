package com.itheima.service.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    //根据企业id查询全部 带分页
    PageInfo findAll(String companyId, int page, int size);

    //根据id查询
    User findById(String userId);

    //根据id删除
    void delete(String userId);

    //保存
    void save(User user);

    //更新
    void update(User user);

    //查询全部
    List<User> findAll(String companyId);

    List<Map> findTreeJson(String userId);

    List findName(String id);

    void updateRoleModule(String userid, String[] roleIds);

    User findByEmail(String email);

    List<Module> finUserMenus(String userId);
}
