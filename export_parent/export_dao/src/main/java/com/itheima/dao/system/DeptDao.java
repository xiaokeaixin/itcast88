package com.itheima.dao.system;

import com.itheima.domain.system.Dept;

import java.util.List;

/**
 * 部门的持久层接口
 */
public interface DeptDao {

    /**
     * 查询所有部门，带分页（用的pageHelper）
     *
     * @param companyId 企业id
     * @return
     */
    List<Dept> findAll(String companyId);//不能直接写findAll()，因为你不能看见其他公司的部门。

    /**
     * 根据id查询部门
     *
     * @param id
     * @return
     */
    Dept findById(String id);//根据id查询，id是唯一标识

    /**
     * 保存
     *
     * @param dept
     */
    void save(Dept dept);//存入一个部门，必然会带着部门的所属公司（企业）

    /**
     * 更新
     *
     * @param dept
     */
    void update(Dept dept);//先要根据id查询出来，然后再修改一个部门

    /**
     * 根据id删除
     *
     * @param id
     */
    void delete(String id);//根据id删除
}
