package cn.acyou.leo.tool.test.poi;

import cn.acyou.leo.framework.util.ExcelUtil;
import cn.acyou.leo.framework.util.WorkUtil;
import cn.acyou.leo.tool.test.poi.pojo.TextBookData;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
public class MainTest2025V1 {

    @Test
    public void poiReadFile() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\temp\\x\\补发优惠券.xlsx"));
        XSSFSheet sheet = workbook.getSheetAt(0);
        int lastRowIndex = sheet.getLastRowNum();
        for (int i = 1; i <= lastRowIndex; i++) {
            XSSFRow row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            String cellValue0 = row.getCell(0).getStringCellValue();

            String body = HttpUtil.createPost("")
                    .body("{\n" +
                            "  \"company_id\": 66600,\n" +
                            "  \"member_id\": " + cellValue0 + "\n" +
                            "}").execute().body();
            String string = JSON.parseObject(body).getJSONObject("data").getString("phone");
            ExcelUtil.getOrCreateCell(row, 2).setCellValue(string);
            System.out.println(cellValue0 + "：" + string);
            WorkUtil.trySleep(800);
        }
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\temp\\x\\补发优惠券_phone.xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

}
