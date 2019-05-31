package com.itheima.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.itheima.commons.utils.UtilFuns;
import com.itheima.domain.system.Module;
import com.itheima.service.system.ModuleService;
import com.itheima.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/system/module")
public class ModuleController extends BaseController {

    @Autowired
    private ModuleService moduleService;

    /**
     * 查询所有模块
     *
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        //1.调用业务层查询所有
        PageInfo pageInfo = moduleService.findAll(page, size);
        //2.存入请求域中
        request.setAttribute("page", pageInfo);
        //3.前往模块列表页面
        return "system/module/module-list";
    }

    /**
     * 前往新增页面
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        //1.查询所有模块：注意此时不能带分页信息
        List<Module> moduleList = moduleService.findAll();
        //2.存入请求域中
        request.setAttribute("menus", moduleList);
        return "system/module/module-add";
    }

    /**
     * 前往更新页面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //1.根据id查询模块
        Module module = moduleService.findById(id);
        //2.存入请求域中
        request.setAttribute("module", module);
        //3.查询所有模块列表(注意此时不能带分页信息)
        List<Module> moduleList = moduleService.findAll();
        //4.存入请求域中
        request.setAttribute("menus", moduleList);
        //5.前往编辑页面
        return "system/module/module-update";
    }


    /**
     * 保存或者更新
     */
    @RequestMapping("/edit")
    public String edit(Module module) {
        //1.判断是保存还是更新
        if (UtilFuns.isEmpty(module.getId())) {
            //保存操作

            moduleService.save(module);
        } else {
            //更新操作：非全字段更新需要先查询
            moduleService.update(module);
        }
        return "redirect:/system/module/list.do";
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public String delete(String id) {
        moduleService.delete(id);
        return "redirect:/system/module/list.do";
    }
}
