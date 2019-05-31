package com.itheima.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.system.ModuleDao;
import com.itheima.dao.system.UserDao;
import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import com.itheima.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ModuleDao moduleDao;

    @Override
    public PageInfo findAll(String companyId, int page, int size) {
        PageHelper.startPage(page, size);
        List<User> userList = userDao.findAll(companyId);
        return new PageInfo(userList);
    }

    @Override
    public User findById(String userId) {
        return userDao.findById(userId);
    }

    @Override
    public void delete(String userId) {
        userDao.delete(userId);
    }

    @Override
    public void save(User user) {
        String id = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        user.setId(id);
        userDao.save(user);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public List<User> findAll(String companyId) {
        return userDao.findAll(companyId);
    }

    @Override
    public List<Map> findTreeJson(String userId) {
        return userDao.findTreeJson(userId);
    }

    @Override
    public List findName(String id) {
        return userDao.findName(id);
    }

    @Override
    public void updateRoleModule(String userid, String[] roleIds) {
        //首先删除 用户的角色
        userDao.deleteRole(userid);
        //重新添加用户的角色
        for (String roleId : roleIds) {
            userDao.addRole(userid, roleId);
        }
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public List<Module> finUserMenus(String userId) {
        //根据id查询用户
        User user = userDao.findById(userId);
        //判断用户是saas管理员 企业内部管理员 租户员工
        if (user.getDegree() == 0) {
            //sass系统管理员
            return moduleDao.finModuleByBelong(0);
        } else if (user.getDegree() == 1) {
            //是企业系统管理员
            return moduleDao.finModuleByBelong(1);
        } else {
            //是租户
            return moduleDao.finModuleByUserId(userId);
        }
//        return moduleDao.finModuleByUserId(userId);
    }
}
