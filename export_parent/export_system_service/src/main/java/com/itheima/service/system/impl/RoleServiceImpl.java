package com.itheima.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.commons.utils.UtilFuns;
import com.itheima.dao.system.RoleDao;
import com.itheima.domain.system.Role;
import com.itheima.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;


    @Override
    public Role findById(String id) {
        return roleDao.findById(id);
    }

    @Override
    public PageInfo findAll(String companyId, int page, int size) {
        //1.设置分页条件
        PageHelper.startPage(page, size);
        //2.查询结果集
        List<Role> roleList = roleDao.findAll(companyId);
        //3.返回
        return new PageInfo(roleList);
    }

    @Override
    public void delete(String id) {
        roleDao.delete(id);
    }

    @Override
    public void save(Role role) {
        //1.处理ID
        role.setId(UtilFuns.generateId());
        //2.保存
        roleDao.save(role);
    }

    @Override
    public void update(Role role) {
        roleDao.update(role);
    }

    @Override
    public List<Map> findTreeJson(String roleId) {
        return roleDao.finTreeJson(roleId);
    }

    @Override
    public void updateRoleModule(String roleId, String moduleIds) {
        //1.先试用角色id删除中间表的数据
        roleDao.deleteRoleModule(roleId);
        //2.把moduLeIds用split方法转换成String数组
        String[] moduleIdArray = moduleIds.split(",");
        //3.遍历模块id数组
        for (String moduleId : moduleIdArray) {
            roleDao.saveRoleModule(roleId,moduleId);
        }
    }

    @Override
    public List<Map> findAllModule(String companyId) {
        return roleDao.findAllModule(companyId);
    }
}
