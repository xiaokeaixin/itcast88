package com.itheima.service.cargo;


import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.ContractProduct;
import com.itheima.domain.cargo.ContractProductExample;
import com.itheima.vo.ContractProductVo;

import java.util.List;

/**
 * 业务层接口
 */
public interface ContractProductService {

    /**
     * 保存
     */
    void save(ContractProduct contractProduct);

    /**
     * 更新
     */
    void update(ContractProduct contractProduct);

    /**
     * 删除
     */
    void delete(String id);

    /**
     * 根据id查询
     */
    ContractProduct findById(String id);

    /**
     * 分页查询
     */
    PageInfo findAll(ContractProductExample example, int page, int size);

    /**
     * 根据企业id和船运时间查询货物信息
     *
     * @param companyId
     * @param shipTime
     * @return
     */
    List<ContractProductVo> findContractProductByShipTime(String companyId, String shipTime);

    /**
     * 批量保存
     * @param contractProducts
     */
    void batchSave(List<ContractProduct> contractProducts);
}
