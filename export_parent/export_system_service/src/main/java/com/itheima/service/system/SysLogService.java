package com.itheima.service.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.SysLog;

/**
 * 系统日志的业务层
 */
public interface SysLogService {

    /**
     * 保存系统日志
     * @param sysLog
     */
    void saveLog(SysLog sysLog);

    /**
     * 查询系统日志带分页
     * @param companyId 企业id
     * @param page      当前页
     * @param size      页大小
     * @return
     */
    PageInfo findAll(String companyId, int page, int size);
}
