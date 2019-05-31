package com.itheima.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.commons.utils.UtilFuns;
import com.itheima.dao.system.SysLogDao;
import com.itheima.domain.system.SysLog;
import com.itheima.service.system.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService{

    @Autowired
    private SysLogDao sysLogDao;

    @Override
    public void saveLog(SysLog sysLog) {
        //1.设置id
        sysLog.setId(UtilFuns.generateId());
        //2.保存
        sysLogDao.save(sysLog);
    }

    @Override
    public PageInfo findAll(String companyId, int page, int size) {
        //1.设置分页信息
        PageHelper.startPage(page, size);
        //2.查询所有
        List<SysLog> sysLogList = sysLogDao.findAll(companyId);
        return new PageInfo(sysLogList);
    }
}
