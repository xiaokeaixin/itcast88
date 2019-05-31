package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.commons.utils.UtilFuns;
import com.itheima.domain.cargo.*;
import com.itheima.service.cargo.ExtCproductService;
import com.itheima.service.cargo.FactoryService;
import com.itheima.web.controller.BaseController;
import com.itheima.web.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCprodcutController extends BaseController {

    @Reference
    private ExtCproductService extCproductService;

    @Reference
    private FactoryService factoryService;

    /**
     * 前往附件的列表页面
     *
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/list")
    public String list(String contractId, String contractProductId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        //1.创建查询条件对象
        ExtCproductExample extCproductExample = new ExtCproductExample();
        //2.设置查询条件
        extCproductExample.createCriteria().andContractProductIdEqualTo(contractProductId);
        //3.查询所有附件（要求：附件是属于某个货物的）
        PageInfo pageInfo = extCproductService.findAll(extCproductExample, page, size);
        //4.存入请求域中
        request.setAttribute("page", pageInfo);
        //5.查询所有附件的生产厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        //6.把生产厂家也存入请求域
        request.setAttribute("factoryList", factoryList);
        //7.把货物的id存入请求域中
        request.setAttribute("contractProductId", contractProductId);
        //8.把合同id存入请求域中
        request.setAttribute("contractId", contractId);
        return "cargo/extc/extc-list";
    }

    /**
     * 前往编辑页面
     *
     * @param id
     * @return
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {//,String contractId,String contractProductId){
        //1.根据id查询附件信息
        ExtCproduct extCproduct = extCproductService.findById(id);
        //2.把附件存入请求域中
        request.setAttribute("extCproduct", extCproduct);
        //3.查询附件的生产厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        //4.把生产厂家也存入请求域
        request.setAttribute("factoryList", factoryList);

//        request.setAttribute("contractId",contractId);
//        request.setAttribute("contractProductId",contractProductId);
        //5.前往编辑页面
        return "cargo/extc/extc-update";
    }

    @Autowired
    private FileUploadUtil fileUploadUtil;

    /**
     * 保存或者更新
     *
     * @param extCproduct
     * @return
     */
    @RequestMapping("/edit")
    public String edit(ContractProduct contractProduct, MultipartFile productPhoto, ExtCproduct extCproduct) throws Exception {
        //1.设置企业信息
        extCproduct.setCompanyId(super.companyId);
        extCproduct.setCompanyName(super.companyName);
        //2.判断是保存还是更新
        if (UtilFuns.isEmpty(extCproduct.getId())) {
            String imgPath = "";
            //判读是否选择了文件
            if (productPhoto.getOriginalFilename() != "" && productPhoto.getOriginalFilename() != null) {
                //实现文件上传
                imgPath = "http://" + fileUploadUtil.upload(productPhoto);
            }
            contractProduct.setProductImage(imgPath);
            //保存
            extCproductService.save(extCproduct);
        } else {
            String imgPath = "";
            //判读是否选择了文件
            if (productPhoto.getOriginalFilename() != "" && productPhoto.getOriginalFilename() != null) {
                //实现文件上传
                imgPath = "http://" + fileUploadUtil.upload(productPhoto);
            }
            contractProduct.setProductImage(imgPath);
            //更新
            extCproductService.update(extCproduct);
        }
        return "redirect:/cargo/extCproduct/list.do?contractId=" + extCproduct.getContractId() + "&contractProductId=" + extCproduct.getContractProductId();//get请求方式的数据拼接：?key=value&key=value
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping("/delete")
    public String delete(String id, String contractId, String contractProductId) {
        extCproductService.delete(id);
        return "redirect:/cargo/extCproduct/list.do?contractId=" + contractId + "&contractProductId=" + contractProductId;
    }
}
