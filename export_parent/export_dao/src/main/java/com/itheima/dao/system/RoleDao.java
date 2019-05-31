package com.itheima.dao.system;

import com.itheima.domain.system.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface RoleDao {

    //根据id查询
    Role findById(String id);

    //查询全部用户
    List<Role> findAll(String companyId);

	//根据id删除
    int delete(String id);

	//添加
    int save(Role role);

	//更新
    int update(Role role);

    //获取树形结构的json数据
    List<Map> finTreeJson(String roleId);

    //根据角色id删除角色模块中间表数据
    void deleteRoleModule(String roleId);

    //保存角色和模块关联到中间表
    void saveRoleModule(@Param("roleId") String roleId, @Param("moduleId") String moduleId);

    List<Map> findAllModule(String id);
}