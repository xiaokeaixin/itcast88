package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.commons.utils.UtilFuns;
import com.itheima.domain.cargo.Contract;
import com.itheima.domain.cargo.ContractExample;
import com.itheima.service.cargo.ContractService;
import com.itheima.web.controller.BaseController;
import com.itheima.web.exceptions.CustomeException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController {

    @Reference
    private ContractService contractService;

    /**
     * 查询所有购销合同
     * 注意的是：每个企业只能查它自己的合同
     *
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) throws CustomeException {
        //1.创建example对象
        ContractExample example = new ContractExample();
        //2.使用example设置查询条件：当前用户的企业id
        example.createCriteria().andCompanyIdEqualTo(super.companyId);
        //细粒度权限控制的条件
        /*
         * 0作为内部控制，租户企业不能使用
         *      0-saas管理员
         *      1-企业管理员
         *      2-管理所有下属部门和人员
         *      3-管理本部门
         *      4-普通员工
         *      private Integer degree;
         */
        switch (user.getDegree()) {
            case 0:{
                //saas管理员，不能查看任何内容
                throw new CustomeException("没有权限访问！");
            }
            case 1:{
                //企业管理员。相当于总经理
                example.createCriteria().andCompanyIdEqualTo(super.companyId);
                break;
            }
            case 2:{
                //部门总监
                example.createCriteria().andCreateDeptLike(user.getDeptId() + "%");
                break;
            }
            case 3:{
                //部门经理
                example.createCriteria().andCreateDeptEqualTo(user.getDeptId());
                break;
            }
            case 4:{
                //普通员工，只能查看自己的数据
                example.createCriteria().andCheckByEqualTo(user.getId());
                break;
            }
            default:{
                break;
            }
        }
        //添加排序
        example.setOrderByClause("create_time desc");
        //3.调用service查询
        PageInfo pageInfo = contractService.findAll(example, page, size);
        //4.把pageInfo存入请求域中
        request.setAttribute("page", pageInfo);
        //5.前往购销合同的列表页面
        return "cargo/contract/contract-list";
    }

    /**
     * 前往添加页面
     *
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "cargo/contract/contract-add";
    }

    /**
     * 保存或者更新
     *
     * @param contract
     * @return
     */
    @RequestMapping("/edit")
    public String edit(Contract contract) {
        //1.设置企业id和名称
        contract.setCompanyId(super.companyId);
        contract.setCompanyName(super.companyName);
        //2.判断是保存还是更新
        if (UtilFuns.isEmpty(contract.getId())) {
            //保存
            //设置合同的创建者，创建者部门
            contract.setCreateBy(user.getId());//用户id
            contract.setCreateDept(user.getDeptId());//用户的部门id
            contractService.save(contract);
        } else {
            //更新
            contractService.update(contract);
        }
        //3.使用重定向前往列表页面
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 前往编辑页面
     *
     * @param id
     * @return
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //1.根据id查询
        Contract contract = contractService.findById(id);
        //2.存入请求域中
        request.setAttribute("contract", contract);
        //3.前往编辑页面
        return "cargo/contract/contract-update";
    }


    /**
     * 前往详情页面
     *
     * @param id
     * @return
     */
    @RequestMapping("/toView")
    public String toView(String id) {
        //1.根据id查询
        Contract contract = contractService.findById(id);
        //2.存入请求域中
        request.setAttribute("contract", contract);
        //3.前往编辑页面
        return "cargo/contract/contract-view";
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public String delete(String id) {
        ///1.调用service删除
        contractService.delete(id);
        //2.前往列表页面
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 提交
     * 状态改为1
     *
     * @param id
     * @return
     */
    @RequestMapping("/submit")
    public String submit(String id) {
        //1.创建Contract对象
        Contract contract = new Contract();
        contract.setId(id);
        contract.setState(1);
        //2.更新操作
        contractService.update(contract);
        //3.前往列表页面
        return "redirect:/cargo/contract/list.do";
    }


    /**
     * 取消
     * 状态改为0
     *
     * @param id
     * @return
     */
    @RequestMapping("/cancel")
    public String cancel(String id) {
        //1.创建Contract对象
        Contract contract = new Contract();
        contract.setId(id);
        contract.setState(0);
        //2.更新操作
        contractService.update(contract);
        //3.前往列表页面
        return "redirect:/cargo/contract/list.do";
    }
}
