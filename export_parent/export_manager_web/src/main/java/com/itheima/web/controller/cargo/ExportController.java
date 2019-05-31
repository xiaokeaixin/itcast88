package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.commons.utils.UtilFuns;
import com.itheima.domain.cargo.*;
import com.itheima.service.cargo.ContractService;
import com.itheima.service.cargo.ExportProductService;
import com.itheima.service.cargo.ExportService;
import com.itheima.vo.ExportProductVo;
import com.itheima.vo.ExportVo;
import com.itheima.web.controller.BaseController;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cargo/export")
public class ExportController extends BaseController {

    @Reference
    private ContractService contractService;

    @Reference
    private ExportService exportService;

    @Reference
    private ExportProductService exportProductService;

    /**
     * 前往合同管理页面
     * 此时的合同状态为1的
     *
     * @return
     */
    @RequestMapping("/contractList")
    public String contractList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        //创建查询条件对象
        ContractExample contractExample = new ContractExample();
        //设置查询条件
//        ContractExample.Criteria criteria = contractExample.createCriteria();
        contractExample.createCriteria().andStateEqualTo(1);
        contractExample.createCriteria().andCompanyIdEqualTo(super.companyId);
        //执行查询
        PageInfo pageInfo = contractService.findAll(contractExample, page, size);
        //存入请求域中
        request.setAttribute("page", pageInfo);
        //前往合同管理列表页面
        return "cargo/export/export-contractList";
    }

    /**
     * 前往新增报运单页面
     *
     * @param id
     * @return
     */
    @RequestMapping("/toExport")
    public String toExport(String id) {
        request.setAttribute("id", id);
        return "cargo/export/export-toExport";
    }

    /**
     * 保存或更新报运单
     *
     * @param export
     * @return
     */
    @RequestMapping("/edit")
    public String edit(Export export) {
        //设置企业id和名称
        export.setCompanyName(super.companyName);
        export.setCompanyId(super.companyId);
        //判断保存还是更新
        if (UtilFuns.isEmpty(export.getId())) {
            //保存
            exportService.save(export);
        } else {
            //更新
            exportService.update(export);
        }
        //前往报运单列表页面
        return "redirect:/cargo/export/list.do";
    }

    /**
     * 前往列表页面
     *
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        //创建查询条件
        ExportExample exportExample = new ExportExample();
        //设置条件
        exportExample.createCriteria().andCompanyIdEqualTo(super.companyId);
        //执行查询
        PageInfo pageInfo = exportService.findAll(exportExample, page, size);
        request.setAttribute("page", pageInfo);
        return "cargo/export/export-list";
    }

    /**
     * 前往查看详情页面
     *
     * @return
     */
    @RequestMapping("/toView")
    public String toView(String id) {
        //调用service查询
        Export export = exportService.findById(id);
        //存入请求域中
        request.setAttribute("export", export);
        return "cargo/export/export-view";
    }

    /**
     * 提交 状态改为1
     *
     * @param id
     * @return
     */
    @RequestMapping("/submit")
    public String submit(String id) {
        //创建报运单对象
        Export export = new Export();
        //设置报运单状态和id
        export.setId(id);
        export.setState(1);
        //更新报运单
        exportService.update(export);
        return "redirect:/cargo/export/list.do";
    }

    /**
     * 取消 状态改为0
     *
     * @param id
     * @return
     */
    @RequestMapping("/cancel")
    public String cancel(String id) {
        //创建报运单对象
        Export export = new Export();
        //设置报运单状态和id
        export.setId(id);
        export.setState(0);
        //更新报运单
        exportService.update(export);
        return "redirect:/cargo/export/list.do";
    }

    /**
     * 前往更新页面
     *
     * @param id
     * @return
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //调用service查询
        Export export = exportService.findById(id);
        //存入请求域中
        request.setAttribute("export", export);
        //查询当前报运单的所有货物
        ExportProductExample example = new ExportProductExample();
        example.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> exportProducts = exportProductService.findAll(example);
        //把货物信息也存入请求域中0
        request.setAttribute("eps", exportProducts);
        return "cargo/export/export-update";
    }

    @RequestMapping("/delete")
    public String delete(String id) {
        //使用service删除报运单
        exportService.delete(id);
        return "redirect:/cargo/export/list.do";
    }

    /**
     * 电子报运
     * cargo/export/exportE.do?id=20B2054AEC8444039048A9C9DDDB8C8A
     *
     * @param id
     * @return
     */
    @RequestMapping("/exportE")
    public String exportE(String id) {
        //查询报运单
        Export export = exportService.findById(id);
        //根据条件查询报运单中的货物
        ExportProductExample example = new ExportProductExample();
        example.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> exportProducts = exportProductService.findAll(example);
        //遍历ExportProduct设置进ExportProductVo中
        List<ExportProductVo> exportProductVoList = new ArrayList<>();
        for (ExportProduct exportProduct : exportProducts) {
            ExportProductVo exportProductVo = new ExportProductVo();
            BeanUtils.copyProperties(exportProduct, exportProductVo);
            exportProductVo.setExportProductId(id);
            exportProductVoList.add(exportProductVo);
        }
        ExportVo exportVo = new ExportVo();
        exportVo.setProducts(exportProductVoList);
        //传值进入ExportVo中
        BeanUtils.copyProperties(export,exportVo);
        //更新状态
        export.setState(2);
        exportService.update(export);
        //调用webservice服务端实现Vo传递
        exportVo.setExportId(id);
        WebClient.create("http://localhost:8081/ws/export/user").type(MediaType.APPLICATION_XML).post(exportVo);
        return "redirect:/cargo/export/list.do";
    }
}
