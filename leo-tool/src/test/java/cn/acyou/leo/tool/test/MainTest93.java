package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.util.ExcelUtil;
import cn.hutool.http.HttpUtil;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2024/4/23 16:50]
 **/
public class MainTest93 {

    public static void main(String[] args) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("数据");
        XSSFRow row = sheet.createRow(2);
        XSSFCell cell = row.createCell(2);
        ExcelUtil.writePicture(cell, "https://pic2.zhimg.com/v2-e9a5e20028568de5b288d2c4adfd4565_r.jpg");
        File file = new File("D:\\temp\\统计数据V2_" + System.currentTimeMillis() + ".xlsx");
        workbook.write(new FileOutputStream(file));
        workbook.close();
    }
}
