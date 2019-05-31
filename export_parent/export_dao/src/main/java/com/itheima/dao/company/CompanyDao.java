package com.itheima.dao.company;

import com.itheima.domain.company.Company;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 企业的持久层接口
 */
public interface CompanyDao {

    /**
     * 查询所有
     *
     * @return
     */
    List<Company> findAll();

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    Company findById(String id);

    /**
     * 保存
     *
     * @param company
     */
    void save(Company company);

    /**
     * 更新
     *
     * @param company
     */
    void update(Company company);

    /**
     * 根据id删除
     *
     * @param id
     */
    void delete(String id);

    /**
     * 查询总记录条数
     *
     * @return
     */
    long findTotal();

    /**
     * 查询带有分页的结果集
     *
     * @param startIndex
     * @param pageSize
     * @return
     *
     * Map<String,Integer> pageMap = new HashMap();
     * pageMap.put("startIndex",startIndex);
     * pageMap.put("pageSize",pageSize);
     */
//    List<Company> findPage(@Param("startIndex") int startIndex,@Param("pageSize") int pageSize);
}
