package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.commons.utils.DownloadUtil;
import com.itheima.commons.utils.UtilFuns;
import com.itheima.vo.ContractProductVo;
import com.itheima.service.cargo.ContractProductService;
import com.itheima.web.controller.BaseController;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Controller
@RequestMapping("/cargo/contract")
public class OutProductController extends BaseController {

    @Reference
    private ContractProductService contractProductService;

    @RequestMapping("/print")
    public String print() {
        return "cargo/print/contract-print";
    }

    /**
     * 打印出货表
     * 模板打印
     *
     * @param inputDate
     */
    @RequestMapping("/printExcel")
    public void printExcel(String inputDate) throws Exception {
        //1.借助servletContext对象的getRealPath获取文件的路径
        String templatePath = session.getServletContext().getRealPath("/make/xlsprint/tOUTPRODUCT.xlsx");
        //2.读取excel模板，创建Excel对象
        XSSFWorkbook wb = new XSSFWorkbook(templatePath);
        //3.读取Sheet对象
        Sheet sheet = wb.getSheetAt(0);
        //4.定义一些可复用的对象
        int rowIndex = 0;//行的索引
        int cellIndex = 1;//单元格的索引
        Row nRow = null;
        Cell nCell = null;
        //5.读取大标题行
        nRow = sheet.getRow(rowIndex++);//0-->1
        //6.读取大标题的单元格
        nCell = nRow.getCell(cellIndex);
        //7.设置大标题内容
        String bigTitle = inputDate.replaceAll("-0", "-").replaceAll("-", "年") + "月份出货表";//inputDate  2015-01  2015年1月份出货表
        nCell.setCellValue(bigTitle);
        //8.跳过第二行
        rowIndex++;//1-->2
        //9.读取第三行，得到它的样式
        nRow = sheet.getRow(rowIndex);
        //读取行高
        float lineHeight = nRow.getHeightInPoints();
        //10.获取第三行的8个单元格中的样式
        CellStyle cs1 = nRow.getCell(cellIndex++).getCellStyle();
        CellStyle cs2 = nRow.getCell(cellIndex++).getCellStyle();
        CellStyle cs3 = nRow.getCell(cellIndex++).getCellStyle();
        CellStyle cs4 = nRow.getCell(cellIndex++).getCellStyle();
        CellStyle cs5 = nRow.getCell(cellIndex++).getCellStyle();
        CellStyle cs6 = nRow.getCell(cellIndex++).getCellStyle();
        CellStyle cs7 = nRow.getCell(cellIndex++).getCellStyle();
        CellStyle cs8 = nRow.getCell(cellIndex++).getCellStyle();
        //11.获取要生成的数据
        List<ContractProductVo> list = contractProductService.findContractProductByShipTime(companyId, inputDate);
        //12.遍历数据
        for (ContractProductVo cpv : list) {
            //13.创建数据行
            nRow = sheet.createRow(rowIndex++);
            //16.设置数据行高
            nRow.setHeightInPoints(lineHeight);
            //17.重置cellIndex
            cellIndex = 1;
            //18.创建数据单元格，设置单元格内容和样式
            //客户名称
            nCell = nRow.createCell(cellIndex++);
            nCell.setCellStyle(cs1);
            nCell.setCellValue(cpv.getCustomName());
            //订单号
            nCell = nRow.createCell(cellIndex++);
            nCell.setCellStyle(cs2);
            nCell.setCellValue(cpv.getContractNo());
            //货号
            nCell = nRow.createCell(cellIndex++);
            nCell.setCellStyle(cs3);
            nCell.setCellValue(cpv.getProductNo());
            //数量
            nCell = nRow.createCell(cellIndex++);
            nCell.setCellStyle(cs4);
            nCell.setCellValue(cpv.getCnumber());
            //工厂
            nCell = nRow.createCell(cellIndex++);
            nCell.setCellStyle(cs5);
            nCell.setCellValue(cpv.getFactoryName());
            //工厂交期
            nCell = nRow.createCell(cellIndex++);
            nCell.setCellStyle(cs6);
            nCell.setCellValue(UtilFuns.dateTimeFormat(cpv.getDeliveryPeriod(), "yyyy-MM"));
            //船期
            nCell = nRow.createCell(cellIndex++);
            nCell.setCellStyle(cs7);
            nCell.setCellValue(UtilFuns.dateTimeFormat(cpv.getShipTime(), "yyyy-MM"));
            //贸易条款
            nCell = nRow.createCell(cellIndex++);
            nCell.setCellStyle(cs8);
            nCell.setCellValue(cpv.getTradeTerms());
        }

        //the last：下载出货表文件
        ByteArrayOutputStream bos = new ByteArrayOutputStream();//字节数组的输出流，它可存可取，带缓冲区
        wb.write(bos);
        new DownloadUtil().download(bos, response, bigTitle + "出货表.xlsx");
        bos.close();
        wb.close();
    }

    /**
     打印出货表
     *  HSSFWorkbook    针对excel 2003及以前     最大行数和列数限制    最大支持65536行     都不支持百万级数据的操作
     *  XSSFWorkbook    针对excel 2003及以后     最大支持1048576行                       都不支持百万级数据的操作
     *  SXSSFWorkbook   它支持百万级数据的POI     但是不支持模板打印    它不支持太多的样式
     */
    /*
    @RequestMapping("/printExcel")
    public void printExcel(String inputDate)throws Exception{
        //1.创建Excel对象
//        XSSFWorkbook wb = new XSSFWorkbook();
        SXSSFWorkbook wb = new SXSSFWorkbook();
        //2.创建Sheet对象
        Sheet sheet = wb.createSheet();
        //3.定义一些可复用的对象
        int rowIndex = 0;//行的索引
        int cellIndex = 1;//单元格的索引
        Row nRow = null;
        Cell nCell = null;
        //4.设置列的宽度  列的宽度有bug
        sheet.setColumnWidth(1,26*256);
        sheet.setColumnWidth(2,12*256);
        sheet.setColumnWidth(3,29*256);
        sheet.setColumnWidth(4,12*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,10*256);
        sheet.setColumnWidth(7,10*256);
        sheet.setColumnWidth(8,8*256);

        //5.创建大标题行   大标题：2012年8月份出货表
        nRow = sheet.createRow(rowIndex++);//使用的是0,使用完了+1
        //设置大标题行的高度
        nRow.setHeightInPoints(36);
        //6.创建大标题的单元格
        nCell = nRow.createCell(cellIndex);
        //7.合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));
        //8.设置大标题内容
        String bigTitle = inputDate.replaceAll("-0","-").replaceAll("-","年") + "月份出货表";//inputDate  2015-01  2015年1月份出货表
        nCell.setCellValue(bigTitle);
        //设置大标题样式
        CellStyle bigTitleCellStyle = this.bigTitle(wb);
        nCell.setCellStyle(bigTitleCellStyle);
        //9.创建小标题内容
        String[] titles = new String[]{"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        //10.创建小标题行
        nRow = sheet.createRow(rowIndex++);//使用的是1，使用完了再加1
        //设置小标题行高
        nRow.setHeightInPoints(26.25f);
        //12.创建小标题的单元格
        for(String title : titles){
            nCell = nRow.createCell(cellIndex++);
            //设置小标题内容
            nCell.setCellValue(title);
            //设置小标题样式
            CellStyle cellStyle = this.title(wb);
            nCell.setCellStyle(cellStyle);
        }
        //13.获取要生成的数据
        List<ContractProductVo> list = contractProductService.findContractProductByShipTime(companyId,inputDate);
        //14.遍历数据
        for(ContractProductVo cpv : list){
            for (int i = 0; i < 5000; i++) {
            //15.创建数据行
            nRow = sheet.createRow(rowIndex++);
            //16.设置数据行高
            nRow.setHeightInPoints(24);
            //17.重置cellIndex
            cellIndex = 1;
            //18.创建数据单元格，设置单元格内容和样式
            //客户名称
            nCell = nRow.createCell(cellIndex++);
//            CellStyle c1 = this.text(wb);
//            nCell.setCellStyle(c1);
            nCell.setCellValue(cpv.getCustomName());
            //订单号
            nCell = nRow.createCell(cellIndex++);
//            CellStyle c2 = this.text(wb);
//            nCell.setCellStyle(c2);
            nCell.setCellValue(cpv.getContractNo());
            //货号
            nCell = nRow.createCell(cellIndex++);
//            CellStyle c3 = this.text(wb);
//            nCell.setCellStyle(c3);
            nCell.setCellValue(cpv.getProductNo());
            //数量
            nCell = nRow.createCell(cellIndex++);
//            CellStyle c4 = this.text(wb);
//            nCell.setCellStyle(c4);
            nCell.setCellValue(cpv.getCnumber());
            //工厂
            nCell = nRow.createCell(cellIndex++);
//            CellStyle c5 = this.text(wb);
//            nCell.setCellStyle(c5);
            nCell.setCellValue(cpv.getFactoryName());
            //工厂交期
            nCell = nRow.createCell(cellIndex++);
//            CellStyle c6 = this.text(wb);
//            nCell.setCellStyle(c6);
            nCell.setCellValue(UtilFuns.dateTimeFormat(cpv.getDeliveryPeriod(),"yyyy-MM"));
            //船期
            nCell = nRow.createCell(cellIndex++);
//            CellStyle c7 = this.text(wb);
//            nCell.setCellStyle(c7);
            nCell.setCellValue(UtilFuns.dateTimeFormat(cpv.getShipTime(),"yyyy-MM"));
            //贸易条款
            nCell = nRow.createCell(cellIndex++);
//            CellStyle c8 = this.text(wb);
//            nCell.setCellStyle(c8);
            nCell.setCellValue(cpv.getTradeTerms());
            }
        }

        //the last：下载出货表文件
        ByteArrayOutputStream bos = new ByteArrayOutputStream();//字节数组的输出流，它可存可取，带缓冲区
        wb.write(bos);
        new DownloadUtil().download(bos,response,bigTitle+"出货表.xlsx");
        bos.close();
        wb.close();
    }*/


    //大标题的样式
    public CellStyle bigTitle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);//字体加粗
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);                //横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        return style;
    }

    //小标题的样式
    public CellStyle title(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);                //横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        style.setBorderTop(BorderStyle.THIN);                        //上细线
        style.setBorderBottom(BorderStyle.THIN);                    //下细线
        style.setBorderLeft(BorderStyle.THIN);                        //左细线
        style.setBorderRight(BorderStyle.THIN);                        //右细线
        return style;
    }

    //文字样式
    public CellStyle text(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 10);

        style.setFont(font);

        style.setAlignment(HorizontalAlignment.LEFT);                //横向居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        style.setBorderTop(BorderStyle.THIN);                        //上细线
        style.setBorderBottom(BorderStyle.THIN);                    //下细线
        style.setBorderLeft(BorderStyle.THIN);                        //左细线
        style.setBorderRight(BorderStyle.THIN);                        //右细线

        return style;
    }
}
