package cn.acyou.leo.tool.test.poi;

import cn.acyou.leo.tool.test.poi.pojo.TextBookData;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fangyou
 * @version [1.0.0, 2021-11-04 9:17]
 */
public class ApachePoiTest {

    /**
     * Apache POI 读取文件
     */
    @Test
    public void poiReadFile() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\poi\\课本表格2.xlsx"));
        int totalSheets = workbook.getNumberOfSheets();
        System.out.printf("本Excel 共有%s个sheet", totalSheets);
        for (int sheetIndex = 0; sheetIndex < totalSheets; sheetIndex++) {
            XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            int lastRowIndex = sheet.getLastRowNum();
            for (int i = 0; i <= lastRowIndex; i++) {
                XSSFRow row = sheet.getRow(i);
                if (row == null) {
                    break;
                }
                short lastCellNum = row.getLastCellNum();
                for (int j = 0; j < lastCellNum; j++) {
                    String cellValue = row.getCell(j).getStringCellValue();
                    System.out.println(cellValue);
                }
            }
        }
    }

    /**
     * Apache POI 写入文件
     */
    @Test
    public void poiWriteFile() throws Exception {
        String[] tableHeaders = {"类型", "标题", "内容"};
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sheet1");
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font font = workbook.createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        font.setBold(true);
        cellStyle.setFont(font);
        // 将第一行的三个单元格给合并
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
        HSSFRow row = sheet.createRow(0);
        HSSFCell beginCell = row.createCell(0);
        beginCell.setCellValue("课文");
        beginCell.setCellStyle(cellStyle);
        row = sheet.createRow(1);
        // 创建表头
        for (int i = 0; i < tableHeaders.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(tableHeaders[i]);
            cell.setCellStyle(cellStyle);
        }
        List<TextBookData> users = new ArrayList<>();
        users.add(new TextBookData("专辑", "大风歌", ""));
        users.add(new TextBookData("台本", "第一句", "大风起兮云飞扬"));
        users.add(new TextBookData("台本", "第二句", "威加海内兮归故乡"));
        users.add(new TextBookData("台本", "第三句", "安得猛士兮守四方"));
        for (int i = 0; i < users.size(); i++) {
            row = sheet.createRow(i + 2);
            TextBookData user = users.get(i);
            row.createCell(0).setCellValue(user.getType());
            row.createCell(1).setCellValue(user.getTitle());
            row.createCell(2).setCellValue(user.getContent());
        }
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\poi\\apachePoi_export_2.xls");
        workbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

}
