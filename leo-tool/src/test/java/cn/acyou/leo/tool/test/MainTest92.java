package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.util.ExcelUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author youfang
 * @version [1.0.0, 2024/4/23 16:50]
 **/
public class MainTest92 {

    public static void main(String[] args) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("数据");

        ExcelUtil.createBuilder(workbook, sheet)
                .createRow(0, (short) 400)   //标题行（一级） 行高400
                .createData("0-0-0-3(整体数据)[247,176,127||true]")
                .createData("0-0-4-17(当日数据)[197,224,179||true]")
                .createData("0-0-18-19(1. 视频翻译)[178,199,230||true]")
                .createData("0-0-20-21(1. 视频克隆)[178,199,230||true]")
                .createData("0-0-22-23(1. 模特视频)[178,199,230||true]")
                .createData("");

        ExcelUtil.createBuilder(workbook, sheet)
                .createRow(1, (short) 600)   //标题行（二级） 行高600
                .createData("1-1-0-0(日期)[247,176,127||true]")
                .createData("1-1-1-1(星期)[247,176,127||true]")
                .createData("1-1-2-2(总用户数)[247,176,127||true]")
                .createData("1-1-3-3(累计收入(元))[247,176,127||true]")
                .createData("1-1-4-4(当日收入(元))[197,224,179||true]")
                .createData("1-1-5-5(新增注册数)[197,224,179||true]")
                .createData("1-1-6-6(注册转化率)[197,224,179||true]")
                .createData("1-1-7-7(新增订单数)[197,224,179||true]")
                .createData("1-1-8-8(新户充值(元))[197,224,179||true]")
                .createData("1-1-9-9(新户订单均额(元))[197,224,179||true]")
                .createData("1-1-10-10(复购充值(元))[197,224,179||true]")
                .createData("1-1-11-11(复购订单数)[197,224,179||true]")
                .createData("1-1-12-12(复购订单均额(元))[197,224,179||true]")
                .createData("1-1-13-13(复购金额占比)[197,224,179||true]")
                .createData("1-1-14-14(非受邀付款人数/金额)[197,224,179||true]")
                .createData("1-1-15-15(受邀付款人数/金额)[197,224,179||true]")
                .createData("1-1-16-16(新增课程购买（积分）)[197,224,179||true]")
                .createData("1-1-17-17(新增课程订单数)[197,224,179||true]")
                .createData("1-1-18-18(合成条数)[178,199,230||true]")
                .createData("1-1-19-19(消耗积分)[178,199,230||true]")
                .createData("1-1-20-20(新增数量)[178,199,230||true]")
                .createData("1-1-21-21(消耗积分)[178,199,230||true]")
                .createData("1-1-22-22(合成条数)[178,199,230||true]")
                .createData("1-1-23-23(消耗积分)[178,199,230||true]")
                .createData("");

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
