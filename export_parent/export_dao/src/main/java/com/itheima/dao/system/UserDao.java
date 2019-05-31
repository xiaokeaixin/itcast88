package com.itheima.dao.system;


import com.itheima.domain.system.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    //根据企业id查询全部
    List<User> findAll(String companyId);

    //根据id查询
    User findById(String userId);

    //根据id删除
    int delete(String userId);

    //保存
    int save(User user);

    //更新
    int update(User user);

    List<Map> findTreeJson(String roleId);

    List findName(String id);

    void deleteRole(String userid);

    void addRole(String userid, String s);

    User findByEmail(String email);
}