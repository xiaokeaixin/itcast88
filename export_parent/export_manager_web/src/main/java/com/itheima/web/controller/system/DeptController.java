package com.itheima.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Dept;
import com.itheima.service.system.DeptService;
import com.itheima.web.controller.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 部门的控制器
 */
@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;

    /**
     * 带有分页的部门列表查询
     *
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/list", name = "查询部门列表")
    public String list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        //1.调用service查询部门
        PageInfo pageInfo = deptService.findAll(super.companyId, page, size);
        //2.存入请求域中
        request.setAttribute("page", pageInfo);
        //3.响应结果视图：前往部门的列表页面
        return "system/dept/dept-list";
    }

    /**
     * 前往添加页面
     * 添加部门事，是允许选择父部门，必须把所有部门查询出来
     *
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        //1.查询所有部门，也要注意companyId，但是不能有分页信息
        List<Dept> deptList = deptService.findAll(super.companyId);
        //2.把查询结果存入请求域中
        request.setAttribute("deptList", deptList);
        //3.前往添加页面
        return "system/dept/dept-add";
    }

    /**
     * 保存或者更新部门
     *
     * @param dept
     * @return
     */
    @RequestMapping("/edit")
    public String edit(Dept dept) {
        if (StringUtils.isEmpty(dept.getId())) {
            //保存
            //1.给部门设置companyId和companyName
            dept.setCompanyId(super.companyId);
            dept.setCompanyName(super.companyName);
            deptService.save(dept);
        } else {
            //更新
//            deptService.update(dept);//调用方法直接更新需要全字段更新
            Dept dbDept = deptService.findById(dept.getId());
            BeanUtils.copyProperties(dept, dbDept, new String[]{"companyId", "companyName"});
            deptService.update(dbDept);
        }
        return "redirect:/system/dept/list.do";//重定向到列表页面
    }

    /**
     * 前往编辑部门页面
     *
     * @param id
     * @return
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //1.查询所有部门，也要注意companyId，但是不能有分页信息
        List<Dept> deptList = deptService.findAll(super.companyId);
        //2.把查询结果存入请求域中
        request.setAttribute("deptList", deptList);
        //3.根据id查询部门
        Dept dept = deptService.findById(id);
        //4.把部门存入请求域
        request.setAttribute("dept", dept);
        //5.前往编辑页面
        return "system/dept/dept-update";
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public String delete(String id) {
        deptService.delete(id);
        return "redirect:/system/dept/list.do";
    }
}
