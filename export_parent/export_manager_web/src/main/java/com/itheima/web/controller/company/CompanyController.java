package com.itheima.web.controller.company;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.company.Company;
import com.itheima.service.company.CompanyService;
import com.itheima.web.controller.BaseController;
import com.itheima.web.exceptions.CustomeException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 企业的控制器
 */
@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController {

    @Reference
    private CompanyService companyService;

    /*private HttpServletRequest request;

    @ModelAttribute
    public void init() {
        this.request = request;
    }*/

    /**
     * 查询所有
     *
     * @return
     */
//    @RequiresPermissions("企业管理")//基于注解的配置，必须有此权限才能访问
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "2") int size) {
        //1.调用service查询 此方法没有分页
        //List<Company> companyList = companyService.findAll();
        //2.存入请求域中
        //request.setAttribute("list", companyList);

        //1.调用service查询带有分页的结果集 此方法是使用我们自己封装的分页实现的
//        PageResult pageResult = companyService.findPage(page, size);
        //2.存入请求域中
//        request.setAttribute("page", pageResult);
        //1.调用service查询，使用mybatis的pageHelper插件实现
        PageInfo pageInfo = companyService.findPageByHelper(page, size);
        //2.存入请求域中
        request.setAttribute("page", pageInfo);
        //3.转发到列表页面
        return "company/company-list";
    }

    /**
     * 前往添加页面
     *
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "company/company-add";
    }

    /**
     * 保存或者更新
     *
     * @param company
     * @return
     */
    @RequestMapping("/edit")
    public String edit(Company company) throws CustomeException {
        //1.判断表单提交过来的company是否有主键，有就是更新，没有就是保存
        if (StringUtils.isEmpty(company.getId())) {
            //保存
//            int i = 1 / 0;
            /*if (StringUtils.isEmpty(company.getName())) {
                throw new CustomeException("请输入企业名称");
            }*/
            companyService.save(company);
        } else {
            //更新
            companyService.update(company);
        }
        //2.响应
        return "redirect:/company/list.do";
    }

    /**
     * 前往更新页面
     *
     * @return
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //1.查询出来要更新的对象
        Company company = companyService.findById(id);
        //2.存入请求域中
        request.setAttribute("company", company);
        //3.转发到更新页面
        return "company/company-update";
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public String delete(String id) {
        //1.直接调用service方法删除
        companyService.delete(id);
        //2.重定向到列表页面
        return "redirect:/company/list.do";
    }


}
