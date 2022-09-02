package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.oss.OSSUtil;
import cn.acyou.leo.framework.prop.OSSProperty;
import cn.acyou.leo.framework.util.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/17 17:35]
 **/
public class MainTest123 {

    public static void main(String[] args) throws Exception {
        OSSProperty ossProperty = new OSSProperty();
        ossProperty.setEndpoint("https://oss-cn-shanghai.aliyuncs.com");
        ossProperty.setAccessKeyId("*");
        ossProperty.setAccessKeySecret("*");
        OSSUtil ossUtil = new OSSUtil(ossProperty);

        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\333\\123.xlsx"));
        XSSFSheet sheet = workbook.getSheetAt(0);
        int lastRowIndex = sheet.getLastRowNum();
        for (int i = 0; i <= lastRowIndex; i++) {
            XSSFRow row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            XSSFCell cell = row.getCell(0);
            if (cell != null) {
                String cellValue = cell.getStringCellValue();
                handlerCellValue(ossUtil, cellValue);
                //System.out.println("cellValue" + cellValue);
            }
            XSSFCell cell1 = row.getCell(1);
            if (cell1 != null) {
                String cellValue1 = cell1.getStringCellValue();
                handlerCellValue(ossUtil, cellValue1);
                //System.out.println("cellValue1" + cellValue1);
            }
        }
    }

    private static void handlerCellValue(OSSUtil ossUtil, String cellValue) {
        if (StringUtils.isNotBlank(cellValue) && cellValue.contains("https://xxx.oss-cn-shanghai.aliyuncs.com")) {
            cellValue = cellValue.replaceAll("https://xxx.oss-cn-shanghai.aliyuncs.com/", "");
            boolean b = ossUtil.existFile("xxx", cellValue);
            System.out.println("准备删除：" + cellValue + "|OSS存在：" + b);
            if (b) {
                ossUtil.deleteFile("xxx", cellValue);
            }
        }
    }
}
