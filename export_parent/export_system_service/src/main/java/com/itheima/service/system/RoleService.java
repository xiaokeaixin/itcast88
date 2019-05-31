package com.itheima.service.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Role;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * 角色的业务层接口
 */
public interface RoleService {
    //根据id查询
    Role findById(String id);

    //查询全部用户
    PageInfo findAll(String companyId, int page, int size);

    //根据id删除
    void delete(String id);

    //添加
    void save(Role role);

    //更新
    void update(Role role);

    List<Map> findTreeJson(String roleId);

    void updateRoleModule(String roleId, String moduleIds);

    List<Map> findAllModule(String companyId);
}
