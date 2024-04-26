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
public class MainTest93 {

    public static void main(String[] args) throws Exception {
        File file = new File("D:\\temp\\houseSold\\V1111.xlsx");
        XSSFWorkbook workbook = ExcelUtil.workbook(file);
        XSSFSheet sheet = ExcelUtil.getOrCreateSheet(workbook, "数据");
        XSSFRow row = ExcelUtil.getOrCreateRow(sheet, sheet.getLastRowNum() + 1);
        XSSFCell cell = ExcelUtil.getOrCreateCell(row, 0);
        ExcelUtil.writeCell(cell, "https://pic2.zhimg.com/v2-e9a5e20028568de5b288d2c4adfd4565_r.jpg");
        workbook.write(new FileOutputStream(file));
        workbook.close();
    }
}
