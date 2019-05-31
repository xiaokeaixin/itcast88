package com.itheima.web.controller;


import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import com.itheima.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * shiro的登录方式
     * @param email
     * @param password
     * @return
     */
    @RequestMapping("/login")
    public String login(String email, String password) {
        try {
            //1.获取Subject主体信息
            Subject subject = SecurityUtils.getSubject();
            //2.获取令牌(就是把邮箱和密码套一个盒子)
            UsernamePasswordToken uToken = new UsernamePasswordToken(email,password);
            //3.调用subject的登录方法，把令牌提供给shiro的核心
            subject.login(uToken);
            //4.获取认证结果:获取到user对象，则表示认证成功，如果没有获取到shiro会直接抛异常
            User user = (User) subject.getPrincipal();
            //5.邮箱密码都匹配存入session
            session.setAttribute("user", user);
            //6.使用用户id查询用户可以操作的菜单集合
            List<Module> moduleList = userService.finUserMenus(user.getId());//他是通过一条语句实现的
            //7.存入回话域对象中
            session.setAttribute("modules", moduleList);
            //8.前往主页面
            return "home/main";//登录前往主页面
        } catch (Exception e) {
            //认证失败
            request.setAttribute("error", "邮箱不存在或者密码不正确!");
            return "forward:login.jsp";//有了forward就不用视图解析器了
        }
    }

    /**
     * 传统的登录方式
     */
    /*@RequestMapping("/login")
    public String login(String email, String password) {
        //1.判断邮箱或密码是否有数据
        if (UtilFuns.isEmpty(email)) {
            //首次访问，从index.jsp直接跳转到此处的
            return "redirect:/login.jsp";
        }
        User user = userService.findByEmail(email);
        //3.判断用户是否存在
        if (user == null || !user.getPassword().equals(password)) {
            //邮箱不存在，或者密码不对
//            return "redirect:/login.jsp";//重定向但是不能携带信息
//            return "login.do";
            request.setAttribute("error", "邮箱不存在或者密码不正确!");
            return "forward:login.jsp";//有了forward就不用视图解析器了
        }
        //4.邮箱密码都匹配存入session
        session.setAttribute("user", user);
        //5.使用用户id查询用户可以操作的菜单集合
        List<Module> moduleList = userService.finUserMenus(user.getId());//他是通过一条语句实现的
        //6.存入回话域对象中
        session.setAttribute("modules", moduleList);
        return "home/main";//登录前往主页面

        *//**
         * Set<Module> moduleSet = new HashSet();
         *
         * 使用用户Id查询用户的所有角色
         * List<Role> roles = userService.findUserRole(userId);
         * 遍历角色集合
         * for(Role role : roles){
         *     //使用角色id查询所有模块
         *     moduleSet.addAll(roleService.findRoleModule(role.getId()));
         * }
         * 把查询得到的模块返回
         * session.setAttribute();....
         *//*
    }*/

    //退出
    @RequestMapping(value = "/logout", name = "用户登出")
    public String logout() {
        //SecurityUtils.getSubject().logout();   //登出
        return "forward:login.jsp";
    }

    @RequestMapping("/home")
    public String home() {
        return "home/home";
    }
}
