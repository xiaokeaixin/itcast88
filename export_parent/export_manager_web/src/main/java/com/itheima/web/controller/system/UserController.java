package com.itheima.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Dept;
import com.itheima.domain.system.Role;
import com.itheima.domain.system.User;
import com.itheima.service.system.DeptService;
import com.itheima.service.system.RoleService;
import com.itheima.service.system.UserService;
import com.itheima.web.controller.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        PageInfo pageInfo = userService.findAll(super.companyId, page, size);
        request.setAttribute("page", pageInfo);
        return "system/user/user-list";
    }

    @RequestMapping("toAdd")
    public String toAdd() {
        List<Dept> deptList = deptService.findAll(super.companyId);
        request.setAttribute("deptList", deptList);
        return "system/user/user-add";
    }

    @RequestMapping("/edit")
    public String edit(User user) {
        if (StringUtils.isEmpty(user.getId())) {
            user.setCompanyId(super.companyId);
            user.setCompanyName(super.companyName);
            userService.save(user);
        } else {
            User dbDept = userService.findById(user.getId());
            BeanUtils.copyProperties(user, dbDept, new String[]{"companyId", "companyName"});
            userService.update(dbDept);
        }
        return "redirect:/system/user/list.do";//重定向到列表页面
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        List<Dept> deptList = deptService.findAll(super.companyId);
        request.setAttribute("deptList", deptList);
        User user = userService.findById(id);
        request.setAttribute("user", user);

        return "system/user/user-update";
    }

    @RequestMapping("/delete")
    public String delete(String id) {
        userService.delete(id);
        return "redirect:/system/user/list.do";
    }

    @RequestMapping(value = "/roleList",name = "获取用户角色")
    public String roleList(String id) {
        //查询当前用户信息
        User user = userService.findById(id);
        request.setAttribute("user", user);
        //获取全部角色
        List<Map> roleList = roleService.findAllModule(super.companyId);
        request.setAttribute("roleList", roleList);
        //获取当前角色包含角色
        List name = userService.findName(id);
        String userRoleStr = name.toString();
        request.setAttribute("userRoleStr", userRoleStr);
        return "system/user/user-role";
    }

    @RequestMapping("/changeRole")
    public String changeRole(String userid ,String[] roleIds){
        userService.updateRoleModule(userid,roleIds);
        return "redirect:/system/user/list.do";
    }
}
