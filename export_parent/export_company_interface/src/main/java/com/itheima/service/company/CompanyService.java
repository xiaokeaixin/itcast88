package com.itheima.service.company;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.company.Company;

import java.util.List;

/**
 * 企业的业务层接口
 */
public interface CompanyService {

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
     * 带有分页的企业列表查询
     *
     * @param page 当前页
     * @param size 页大小
     * @return
     */
    /*PageResult findPage(int page, int size);*/

    /**
     * 使用mybatis的pagehelper插件实现分页查询
     *
     * @param page
     * @param size
     * @return
     */
    PageInfo findPageByHelper(int page, int size);
}
