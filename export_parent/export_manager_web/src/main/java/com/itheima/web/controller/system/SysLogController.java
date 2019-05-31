package com.itheima.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.itheima.service.system.SysLogService;
import com.itheima.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/system/log")
public class SysLogController extends BaseController {

    @Autowired
    private SysLogService sysLogService;

    /**
     * 查询日志列表
     *
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        //1.调用业务层查询
        PageInfo pageInfo = sysLogService.findAll(super.companyId, page, size);
        //2.存入请求域中
        request.setAttribute("page", pageInfo);
        //3.前往日志列表页面
        return "system/log/log-list";//请求转发
    }
}
