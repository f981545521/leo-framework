package cn.acyou.leo.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author youfang
 * @version [1.0.0, 2023/8/30 14:16]
 **/
@Slf4j
public class ExcelUtil {

    private final static DecimalFormat df = new DecimalFormat("0"); // 格式化number String字符
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd"); // 日期格式化
    private final static DecimalFormat df2 = new DecimalFormat("0"); // 格式化数字

    /**
     * 从Sheet页导入数据 （默认第一个sheet页）
     *
     * @param path 文件路径
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    public static List<Map<String, Object>> importData(String path) throws Exception {
        return importData(path, 0);
    }

    /**
     * 从Sheet页导入数据
     *
     * @param path       文件路径
     * @param sheetIndex Sheet页
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    public static List<Map<String, Object>> importData(String path, int sheetIndex) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(new File(path));
        return ExcelUtil.importData(workbook.getSheetAt(sheetIndex));
    }

    /**
     * 从Sheet页导入数据
     *
     * @param sheet Sheet页
     * @return {@link List}<{@link Map}<{@link String}, {@link Object}>>
     */
    public static List<Map<String, Object>> importData(XSSFSheet sheet) {
        // 返回数据
        List<Map<String, Object>> ls = new ArrayList<>();
        // 取第一行标题
        XSSFRow heard = sheet.getRow(0);
        String[] title = new String[heard.getLastCellNum()];
        for (int y = heard.getFirstCellNum(); y < heard.getLastCellNum(); y++) {
            title[y] = heard.getCell(y).getStringCellValue();
        }
        // 遍历当前sheet中剩下的所有行
        for (int j = 1; j < sheet.getLastRowNum() + 1; j++) {
            XSSFRow row = sheet.getRow(j);
            Map<String, Object> m = new LinkedHashMap<>();
            // 遍历所有的列
            for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                m.put(title[y], getCellValue(row.getCell(y)));
            }
            ls.add(m);
        }

        return ls;
    }


    /**
     * 导出数据到Excel
     *
     * @param response  响应
     * @param dataList  数据
     * @param sheetName sheet名称
     * @throws IOException
     */
    public static void exportExcel(HttpServletResponse response, List<Map<String, Object>> dataList, String sheetName) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String headerStr = "attachment;filename=" + sheetName + ".xlsx";
        response.setHeader("Content-Disposition", new String(headerStr.getBytes("GBK"), StandardCharsets.ISO_8859_1));
        exportExcel(response.getOutputStream(), dataList, sheetName);
    }

    /**
     * 导出数据到Excel
     *
     * @param out       输出流
     * @param dataList  数据
     * @param sheetName sheet名称
     * @throws IOException
     */
    public static void exportExcel(OutputStream out, List<Map<String, Object>> dataList, String sheetName) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultRowHeightInPoints((short) 20);
        Map<String, CellStyle> styles = createStyles(workbook);
        HSSFRow row = sheet.createRow(0);
        Map<String, Object> map = dataList.get(0);
        String[] tableHeaders = map.keySet().toArray(new String[0]);
        // 创建表头
        for (int i = 0; i < tableHeaders.length; i++) {
            sheet.setColumnWidth(i, 20 * 256);
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(tableHeaders[i]);
            cell.setCellStyle(styles.get("header"));
        }
        // 写入数据
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> rowMap = dataList.get(i);
            row = sheet.createRow(i + 1);
            for (int j = 0; j < tableHeaders.length; j++) {
                HSSFCell cell = row.createCell(j);
                cell.setCellValue(rowMap.get(tableHeaders[j]).toString());
                cell.setCellStyle(styles.get("data2"));
            }
        }
        workbook.write(out);
        out.flush();
        out.close();
    }


    /**
     * 描述：对表格中数值进行格式化
     */
    public static Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        Object value = null;
        switch (cell.getCellType()) {
            case STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case _NONE:
                break;
            case NUMERIC:
                if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                    value = df.format(cell.getNumericCellValue());
                } else if ("m/d/yy".equals(cell.getCellStyle().getDataFormatString())) {
                    value = sdf.format(cell.getDateCellValue());
                } else {
                    value = df2.format(cell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case FORMULA:
                break;
            case BLANK:
                value = "";
                break;
            case ERROR:
                break;
            default:
                break;
        }
        return value;
    }


    /**
     * 创建表格样式
     *
     * @param wb 工作薄对象
     * @return 样式列表
     */
    private static Map<String, CellStyle> createStyles(Workbook wb) {
        // 写入各条记录,每条记录对应excel表中的一行
        Map<String, CellStyle> styles = new HashMap<>();
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font totalFont = wb.createFont();
        totalFont.setFontName("Arial");
        totalFont.setFontHeightInPoints((short) 10);
        style.setFont(totalFont);
        styles.put("total", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.LEFT);
        styles.put("data1", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.CENTER);
        styles.put("data2", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        styles.put("data3", style);

        return styles;
    }
}
