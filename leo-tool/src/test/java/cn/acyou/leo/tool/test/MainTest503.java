package cn.acyou.leo.tool.test;


import cn.acyou.leo.framework.util.ExcelUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;

/**
 *
 *
 * @author youfang
 * @version [1.0.0, 2025/4/14 10:45]
 **/
public class MainTest503 {

    /**
     * 用一句话概括，xlsx和.xls格式的主要区别在于，
     * .xls格式单个工作表最多支持65536行，256列。
     * .xlsx格式最多支持1048576行，16384列。
     *
     * 此外就是，存储同样多的数据，.xlsx格式文件更小。基本就这两点区别。
     *
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        XSSFWorkbook workbook = ExcelUtil.workbook(new File("D:\\temp\\excel\\ALLWORD.xlsx"));
        XSSFSheet sheet = workbook.getSheetAt(0);
        /**
         * Exception in thread "main" java.lang.IllegalArgumentException: Invalid row number (1048576) outside allowable range (0..1048575)
         * 	at org.apache.poi.xssf.usermodel.XSSFRow.setRowNum(XSSFRow.java:423)
         * 	at org.apache.poi.xssf.usermodel.XSSFSheet.createRow(XSSFSheet.java:788)
         * 	at cn.acyou.leo.tool.test.MainTest503.main(MainTest503.java:20)
         */
        for (int i = 0; i < 5000000; i++) {
            XSSFRow row = sheet.createRow(i);
            row.createCell(0).setCellValue(i);
            row.createCell(1).setCellValue("你好:" + i);
        }
        System.out.println("end");
    }
}
