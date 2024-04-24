package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.util.ExcelUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author youfang
 * @version [1.0.0, 2024/4/23 16:50]
 **/
public class MainTest91 {

    public static void main(String[] args) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("数据");
        //标题行（一级）
        XSSFRow row0 = sheet.createRow(0);
        //设置行高
        row0.setHeight((short) 400);
        XSSFCell row0cell = row0.createCell(0);
        row0cell.setCellValue("整体数据");
        row0cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(247, 176, 127), null, true));
        ExcelUtil.mergedRegion(sheet, new CellRangeAddress(0, 0, 0, 3));

        row0cell = row0.createCell(4);
        row0cell.setCellValue("当日数据");
        row0cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(197, 224, 179), null, true));
        ExcelUtil.mergedRegion(sheet, new CellRangeAddress(0, 0, 4, 17));

        row0cell = row0.createCell(18);
        row0cell.setCellValue("1. 视频翻译");
        row0cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(178, 199, 230), null, true));
        ExcelUtil.mergedRegion(sheet, new CellRangeAddress(0, 0, 18, 19));

        row0cell = row0.createCell(20);
        row0cell.setCellValue("2. 视频克隆");
        row0cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(178, 199, 230), null, true));
        ExcelUtil.mergedRegion(sheet, new CellRangeAddress(0, 0, 20, 21));

        row0cell = row0.createCell(22);
        row0cell.setCellValue("2. 模特视频");
        row0cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(178, 199, 230), null, true));
        ExcelUtil.mergedRegion(sheet, new CellRangeAddress(0, 0, 22, 23));

        //标题行（二级）
        XSSFRow row1 = sheet.createRow(1);
        //设置行高
        row1.setHeight((short) 600);
        XSSFCell row1cell = row1.createCell(0);
        row1cell.setCellValue("日期");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(247, 176, 127), null, true));
        row1cell = row1.createCell(1);
        row1cell.setCellValue("星期");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(247, 176, 127), null, true));
        row1cell = row1.createCell(2);
        row1cell.setCellValue("总用户数");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(247, 176, 127), null, true));
        row1cell = row1.createCell(3);
        row1cell.setCellValue("累计收入（元）");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(247, 176, 127), null, true));

        row1cell = row1.createCell(4);
        row1cell.setCellValue("当日收入（元）");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(197, 224, 179), null, true));
        row1cell = row1.createCell(5);
        row1cell.setCellValue("新增注册数");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(197, 224, 179), null, true));
        row1cell = row1.createCell(6);
        row1cell.setCellValue("注册转化率");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(197, 224, 179), null, true));
        row1cell = row1.createCell(7);
        row1cell.setCellValue("新增订单数");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(197, 224, 179), null, true));
        row1cell = row1.createCell(8);
        row1cell.setCellValue("新户充值（元）");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(197, 224, 179), null, true));
        row1cell = row1.createCell(9);
        row1cell.setCellValue("新户订单均额（元）");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(197, 224, 179), null, true));
        row1cell = row1.createCell(10);
        row1cell.setCellValue("复购充值（元）");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(197, 224, 179), null, true));
        row1cell = row1.createCell(11);
        row1cell.setCellValue("复购订单数");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(197, 224, 179), null, true));
        row1cell = row1.createCell(12);
        row1cell.setCellValue("复购订单均额（元）");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(197, 224, 179), null, true));
        row1cell = row1.createCell(13);
        row1cell.setCellValue("复购金额占比");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(197, 224, 179), null, true));
        row1cell = row1.createCell(14);
        row1cell.setCellValue("非受邀付款人数/金额");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(197, 224, 179), null, true));
        row1cell = row1.createCell(15);
        row1cell.setCellValue("受邀付款人数/金额");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(197, 224, 179), null, true));
        row1cell = row1.createCell(16);
        row1cell.setCellValue("新增课程购买(积分)");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(197, 224, 179), null, true));
        row1cell = row1.createCell(17);
        row1cell.setCellValue("新增课程订单数");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(197, 224, 179), null, true));

        row1cell = row1.createCell(18);
        row1cell.setCellValue("合成条数");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(178, 199, 230), null, true));
        row1cell = row1.createCell(19);
        row1cell.setCellValue("消耗积分");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(178, 199, 230), null, true));
        row1cell = row1.createCell(20);
        row1cell.setCellValue("新增数量");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(178, 199, 230), null, true));
        row1cell = row1.createCell(21);
        row1cell.setCellValue("消耗积分");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(178, 199, 230), null, true));
        row1cell = row1.createCell(22);
        row1cell.setCellValue("合成条数");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(178, 199, 230), null, true));
        row1cell = row1.createCell(23);
        row1cell.setCellValue("消耗积分");
        row1cell.setCellStyle(ExcelUtil.createStyle(workbook, new java.awt.Color(178, 199, 230), null, true));

        //数据行
        for (int i = 2; i < 10; i++) {
            XSSFRow row2 = sheet.createRow(i);
            for (int j = 0; j < 24; j++) {
                XSSFCell cell = row2.createCell(j);
                cell.setCellValue((double) 5);
                cell.setCellStyle(ExcelUtil.createStyle(workbook, null, null, false));
            }
        }
        //统计行
        int lastRowNum = sheet.getLastRowNum();
        XSSFRow summationRow = sheet.createRow(lastRowNum + 1);
        XSSFCell cell = summationRow.createCell(0);
        cell.setCellValue("合计");
        cell.setCellStyle(ExcelUtil.createStyle(workbook, null, new java.awt.Color(224, 62, 62), true));
        for (int j = 1; j < 24; j++) {
            XSSFCell cellData = summationRow.createCell(j);
            String letter = ExcelUtil.convertToLetter(j);
            if (j == 1) {
                cellData.setCellFormula("SUM(E3:INDEX(E:E,ROW()))");
            }else
            if (j == 2) {
                cellData.setCellFormula("INDEX(C:C,ROW()-1)");
            }else
            if (j == 3) {
                cellData.setCellFormula("INDEX(E:E,ROW())/(INDEX(H:H,ROW())-INDEX(L:L,ROW()))");
            }else
            if (j == 11) {
                cellData.setCellFormula("COUNT(" + letter + "3:" + letter + (lastRowNum + 1) + ")");
            }
            else {
                cellData.setCellFormula("SUM(" + letter + "3:" + letter + (lastRowNum + 1) + ")");
            }
            cellData.setCellStyle(ExcelUtil.createStyle(workbook, null, new java.awt.Color(224, 62, 62), true));
        }

        File file = new File("D:\\temp\\统计数据V1_" + System.currentTimeMillis() + ".xlsx");
        workbook.write(new FileOutputStream(file));
        workbook.close();
    }



}
