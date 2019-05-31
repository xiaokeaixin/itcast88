package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.commons.utils.UtilFuns;
import com.itheima.domain.cargo.ContractProduct;
import com.itheima.domain.cargo.ContractProductExample;
import com.itheima.domain.cargo.Factory;
import com.itheima.domain.cargo.FactoryExample;
import com.itheima.service.cargo.ContractProductService;
import com.itheima.service.cargo.FactoryService;
import com.itheima.web.controller.BaseController;
import com.itheima.web.utils.FileUploadUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {

    @Reference
    private ContractProductService contractProductService;

    @Reference
    private FactoryService factoryService;

    /**
     * 货物的列表查询
     *
     * @param contractId 合同id，用于在保存货物时，确定是哪个合同的货物
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/list")
    public String list(String contractId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        //1.查询货物的列表（有要求：必须是某个合同的货物）
        ContractProductExample example = new ContractProductExample();
        example.createCriteria().andContractIdEqualTo(contractId);
        PageInfo pageInfo = contractProductService.findAll(example, page, size);
        //2.存入请求域中
        request.setAttribute("page", pageInfo);
        //3.把合同id存入请求域中
        request.setAttribute("contractId", contractId);
        //4.查询所有的生产厂家，注意必须是生产货物的厂家，不要生产附件的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        //5.把生产厂家也存入请求域
        request.setAttribute("factoryList", factoryList);
        return "cargo/product/product-list";
    }

    /**
     * 前往编辑页面
     *
     * @return
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //1.根据id查询货物信息
        ContractProduct contractProduct = contractProductService.findById(id);
        //2.存入请求域中
        request.setAttribute("contractProduct", contractProduct);
        //3.查询所有的生产厂家，注意必须是生产货物的厂家，不要生产附件的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        //4.把生产厂家也存入请求域
        request.setAttribute("factoryList", factoryList);
        return "cargo/product/product-update";
    }

    @Autowired
    private FileUploadUtil fileUploadUtil;

    /**
     * 保存或者更新
     *
     * @return
     */
    @RequestMapping("/edit")
    public String edit(ContractProduct contractProduct, MultipartFile productPhoto) throws Exception {
        contractProduct.setCompanyId(super.companyId);
        contractProduct.setCompanyName(super.companyName);
        //判断是保存还是更新
        if (UtilFuns.isEmpty(contractProduct.getId())) {
            String imgPath = "";
            //判读是否选择了文件
            if (productPhoto.getOriginalFilename() != "" && productPhoto.getOriginalFilename() != null) {
                //实现文件上传
//                System.out.println(productPhoto.getOriginalFilename());
                imgPath = "http://" + fileUploadUtil.upload(productPhoto);
            }
            contractProduct.setProductImage(imgPath);
            //保存
            contractProductService.save(contractProduct);
        } else {
            String imgPath = "";
            //判读是否选择了文件
            if (productPhoto.getOriginalFilename() != "" && productPhoto.getOriginalFilename() != null) {
                //实现文件上传
                imgPath = "http://" + fileUploadUtil.upload(productPhoto);
            }
            contractProduct.setProductImage(imgPath);
            //更新
            contractProductService.update(contractProduct);
        }
        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractProduct.getContractId();
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping("/delete")
    public String delete(String id, String contractId) {
        contractProductService.delete(id);
        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractId;
    }

    /**
     * 前往导入页面
     *
     * @return
     */
    @RequestMapping("/toImport")
    public String toImport(String contractId) {
        request.setAttribute("contractId", contractId);
        return "cargo/product/product-import";
    }

    /**
     * 批量导入货物
     *
     * @param contractId 货物所属合同
     * @return
     */
    @RequestMapping("/import")
    public String importExcel(String contractId, MultipartFile file) throws Exception {
        //创建一个集合用于存放需要保存的货物信息
        List<ContractProduct> contractProducts = new ArrayList<>();
        //使用file对象获取字节输入流
        InputStream in = file.getInputStream();
        //使用字节输入流构建Excele对象
        XSSFWorkbook wb = new XSSFWorkbook(in);
        //获取sheet
        XSSFSheet sheet = wb.getSheetAt(0);
        //获取sheet中的所有行
//        int rowNum = sheet.getLastRowNum();//得到最后一行遍历int数
//        for (int i = 1; i < rowNum; i++) {
//            XSSFRow row = sheet.getRow(i);//从1开始
//        }
        //得到行的迭代器
        Iterator<Row> iterator = sheet.iterator();
        int rowIndex = 0;
        while (iterator.hasNext()) {
            //取出每一个行对象
            Row row = iterator.next();
            //跳过第一行
            if (rowIndex == 0) {
                rowIndex++;
                continue;
            }
            //遍历一个object数据，把每行数据存入Object数组中
            Object[] objects = new Object[10];
            for (int i = 1; i < 10; i++) {
                objects[i] = getVale(row.getCell(i));
            }
            //创建货物对象
            ContractProduct cp = new ContractProduct(objects,super.companyId,super.companyName);
            //设置货物所属的购销合同
            cp.setContractId(contractId);
            //把有数据的货物添加到集合中
            contractProducts.add(cp);
        }
        //调用service实现保存
        contractProductService.batchSave(contractProducts);
        //批量导入成功后看看货物数量是否正确
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 根据单元格的数据类型获取指定类型单元格内容
     * @param cell
     * @return
     */
    private Object getVale(Cell cell) {
        //定义返回值
        Object rtValue = null;
        //获取单元格类型
        CellType cellType = cell.getCellType();
        //判断类型
        switch (cellType) {
            case STRING:{
                //字符串类型
                rtValue = cell.getStringCellValue();
                break;
            }
            case BOOLEAN:{
                rtValue = cell.getBooleanCellValue();
                break;
            }
            case NUMERIC:{//数值和日期类型都属于此类型
                if (DateUtil.isCellDateFormatted(cell)) {
                    //日期类型
                    rtValue = cell.getDateCellValue();
                    break;
                } else {
                    //数值类型
                    rtValue = cell.getNumericCellValue();
                    break;
                }
            }
            default:{
                return null;
            }
        }
        return rtValue;
    }
}
