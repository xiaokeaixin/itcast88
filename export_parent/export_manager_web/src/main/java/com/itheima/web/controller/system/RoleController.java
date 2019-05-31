package com.itheima.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.itheima.commons.utils.UtilFuns;
import com.itheima.domain.system.Module;
import com.itheima.domain.system.Role;
import com.itheima.service.system.RoleService;
import com.itheima.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    /**
     * 查询所有角色
     *
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        //1.调用业务层查询所有
        PageInfo pageInfo = roleService.findAll(super.companyId, page, size);
        //2.存入请求域中
        request.setAttribute("page", pageInfo);
        //3.前往角色列表页面
        return "system/role/role-list";
    }

    /**
     * 前往新增页面
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "system/role/role-add";
    }

    /**
     * 前往更新页面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //1.根据id查询角色
        Role role = roleService.findById(id);
        //2.存入请求域中
        request.setAttribute("role", role);
        //3.前往编辑页面
        return "system/role/role-update";
    }


    /**
     * 保存或者更新
     */
    @RequestMapping("/edit")
    public String edit(Role role) {
        //设置企业id和企业名称
        role.setCompanyId(super.companyId);
        role.setCompanyName(super.companyName);
        //1.判断是保存还是更新
        if (UtilFuns.isEmpty(role.getId())) {
            //保存操作
            roleService.save(role);
        } else {
            //更新操作：非全字段更新需要先查询
            roleService.update(role);
        }
        return "redirect:/system/role/list.do";
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public String delete(String id) {
        roleService.delete(id);
        return "redirect:/system/role/list.do";
    }

    /**
     * 前往分配权限页面
     * 第一种处理思路，在前往分配权限页面的方法中拼接json
     * json格式数据，由我们自己拼接
     * @param id
     * @return

    @RequestMapping("/roleModule")
    public String roleModule(@RequestParam("roleid") String id) {
        //1.根据id查询角色
        Role role = roleService.findById(id);
        //2.存入请求域
        request.setAttribute("role", role);
        //查询所有模块
        List<Module> allModuleList = null;
        //查询当前觉得id所具备的模块列表
        List<Module> currentRoleModuleList = null;
        //遍历所有模块列表
        StringBuilder stringBuilder = new StringBuilder();
        for (Module module : allModuleList) {
            stringBuilder.append("{ id :").append("'" + module.getId() + "},");
            stringBuilder.append("{ pId :").append("'" + module.getParentId() + "},");
            stringBuilder.append("{ name :").append("'" + module.getName() + "},");
            if (currentRoleModuleList.contains(module)) {
                stringBuilder.append("{ checked :").append("'" + true + "},");
            } else {
                stringBuilder.append("{ checked :").append("'" + false + "},");
            }
        }
        String json = stringBuilder.toString();
        json.substring(0, json.length() - 1);
        json.concat("]");
        return "system/role/role-module";
    }
     */

    /**
     * 第二种处理思路，在分配中不管树形结构的json数据生成
     * 在role-module.jso中发起ajax请求，用@ResponseBody注解配合jackson组建自动生成json     * @param id
     * @return
     */
    @RequestMapping("/roleModule")
    public String roleModule(@RequestParam("roleid") String id) {
        //1.根据id查询角色
        Role role = roleService.findById(id);
        //2.存入请求域
        request.setAttribute("role", role);
        return "system/role/role-module";
    }

    /**
     * 第二种思路中关于json格式数据生成的分析：
     *      我们需要查询出来所有的模块列表
     *      同时还要获取当前角色的模块列表
     *      两个列表进行比对，最终决定哪些模块是被选中的状态，哪些是不选中的。
     *      比对完成后，按照要求生成返回值对象
     *
     *  问题
     *      此种方式，需要查询数据库两次。
     *      第一次是：获取所有模块列表
     *      第二次是：获取当前角色列表
     *
     * 异步请求生成树形结构的json数据
     * json的数据格式是
     * [
     * { id:11, pId:1, name:"随意勾选 1-1", open:true},
     * { id:111, pId:11, name:"随意勾选 1-1-1"},
     * ]
     * 返回值是一个json数据，可以用工具类实现
     * @param id
     * @return

    @RequestMapping("/initModuleData")
    public @ResponseBody List initModuleData(@RequestParam("roleid") String id) {
        //创建返回值对象
        List<Map> returnList = new ArrayList<>();
        //查询所有模块
        List<Module> allModuleList = null;
        //查询当前觉得id所具备的模块列表
        List<Module> currentRoleModuleList = null;
        for (Module module : allModuleList) {
            Map map = new HashMap();
            //设置map中的数据
            map.put("id", module.getId());
            map.put("pId", module.getParentId());
            map.put("name", module.getName());
            if (currentRoleModuleList.contains(module)) {
                map.put("checked", "true");
            } else {
                map.put("checked", "false");
            }
            //加入到返回值集合中
            returnList.add(map);
        }
        return returnList;
    }*/

    /**
     * 获取树形结构的json数据
     * @param id
     * @return
     */
    @RequestMapping("/initModuleData")
    public @ResponseBody List initModuleData(@RequestParam("roleid") String id) {
        List<Map> list = roleService.findTreeJson(id);
        return list;
    }

    /**
     * moduleIds是用，拼接的模块id
     * moduleIds是用，拼接的模块id
     * @param id
     * @param moduleIds
     * @return
     */
    @RequestMapping("/updateRoleModule")
    public String updateRoleModule(@RequestParam("roleid") String id, String moduleIds) {
        //1.调用业务层实现更新权限
        roleService.updateRoleModule(id,moduleIds);
        //2.重定向到角色列表页面
        return "redirect:/system/role/list.do";
    }
}
