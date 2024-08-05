package cn.acyou.leo.framework.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
                if (y < title.length) {
                    m.put(title[y], getCellValue(row.getCell(y)));
                }
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
        setResponseExcel(response, sheetName);
        exportExcel(response.getOutputStream(), dataList, sheetName);
    }

    /**
     * 设置下载Excel响应头
     * @param response 响应
     * @param fileName 文件名
     * @throws IOException
     */
    public static void setResponseExcel(HttpServletResponse response, String fileName) throws IOException{
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String headerStr = "attachment;filename=" + fileName + ".xlsx";
        response.setHeader("Content-Disposition", new String(headerStr.getBytes("GBK"), StandardCharsets.ISO_8859_1));
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
        Map<String, List<Map<String, Object>>> dataMap = new LinkedHashMap<>();
        dataMap.put(sheetName, dataList);
        exportExcelMultiSheet(out, dataMap);
    }

    /**
     * 导出数据到Excel (多sheet页)
     *
     * @param out       输出流
     * @param dataMap  sheet名称 + 数据
     * @throws IOException
     */
    public static void exportExcelMultiSheet(OutputStream out, Map<String, List<Map<String, Object>>> dataMap) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        dataMap.forEach((sheetName, dataList)->{
            HSSFSheet sheet = workbook.createSheet(sheetName);
            sheet.setDefaultRowHeightInPoints((short) 20);
            if (dataList != null && dataList.size() > 0) {
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
                        final Object o = rowMap.get(tableHeaders[j]);
                        writeCell(cell, o);
                        cell.setCellStyle(styles.get("data2"));
                    }
                }
            }
        });
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
     * IndexedColors 颜色对照表
     * https://blog.csdn.net/shanghaojiabohetang/article/details/51837242
     * <p>
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

    /**
     * 数字到字母转换 从0开始
     * 0=>A
     * 1=>B
     * 2=>C
     * 3=>D
     * 4=>E
     * 5=>F
     * 6=>G
     * 7=>H
     * 8=>I
     * 9=>J
     * ...
     *
     * @param number 数字
     * @return {@link String}
     */
    public static String convertToLetter(int number) {
        if (number < 0) {
            return null;
        }
        number++;
        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            int mod = (number - 1) % 26;
            sb.insert(0, (char) (mod + 65));
            number = (number - mod) / 26;
        }
        return sb.toString();
    }

    /**
     * 创建单元格样式
     *
     * @param workbook workbook
     * @return {@link XSSFCellStyle}
     */
    public static XSSFCellStyle createStyle(XSSFWorkbook workbook) {
        return createStyle(workbook, null, null, false);
    }

    /**
     * 创建单元格样式
     *
     * @param workbook workbook
     * @param bgColor  背景色 默认无
     * @return {@link XSSFCellStyle}
     */
    public static XSSFCellStyle createStyle(XSSFWorkbook workbook, java.awt.Color bgColor) {
        return createStyle(workbook, bgColor, null, false);
    }

    /**
     * 创建单元格样式
     *
     * @param workbook  workbook
     * @param bgColor   背景色 默认无
     * @param fontColor 字体色 默认无
     * @return {@link XSSFCellStyle}
     */
    public static XSSFCellStyle createStyle(XSSFWorkbook workbook, java.awt.Color bgColor, java.awt.Color fontColor) {
        return createStyle(workbook, bgColor, fontColor, false);
    }

    /**
     * 创建单元格样式
     *
     * @param workbook  workbook
     * @param bgColor   背景色 默认无
     * @param fontColor 字体色 默认无
     * @param fontBlod  字体加粗 默认false
     * @return {@link XSSFCellStyle}
     */
    public static XSSFCellStyle createStyle(XSSFWorkbook workbook, java.awt.Color bgColor, java.awt.Color fontColor, Boolean fontBlod) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        if (bgColor != null) {
            //单元格背景色
            cellStyle.setFillForegroundColor(new XSSFColor(bgColor, new DefaultIndexedColorMap()));
            //单元格背景色 填充效果
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //文本显示
        cellStyle.setWrapText(true);
        //设置边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 8);
        //字体加粗
        font.setBold(false);
        font.setColor(IndexedColors.BLACK.getIndex());
        if (fontBlod != null) {
            font.setBold(fontBlod);
        }
        if (fontColor != null) {
            font.setColor(new XSSFColor(fontColor, new DefaultIndexedColorMap()));
        }
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 合并单元格 使用第一个单元格的样式
     * @param sheet 工作簿
     * @param cellAddresses 单元格范围
     */
    public static void mergedRegion(XSSFSheet sheet, CellRangeAddress cellAddresses) {
        //合并单元格 会使用第一个单元格的文字
        int firstRow = cellAddresses.getFirstRow();
        int lastRow = cellAddresses.getLastRow();
        int firstColumn = cellAddresses.getFirstColumn();
        int lastColumn = cellAddresses.getLastColumn();
        if (firstRow == lastRow && firstColumn == lastColumn) {
            //无需合并
        }else {
            sheet.addMergedRegion(cellAddresses);
        }
        XSSFCellStyle cellStyle = sheet.getRow(firstRow).getCell(firstColumn).getCellStyle();
        for (int i = firstRow; i < lastRow + 1; i++) {
            for (int j = firstColumn; j < lastColumn + 1; j++) {
                XSSFRow row = sheet.getRow(i);
                if (row == null) {
                    row = sheet.createRow(i);
                }
                XSSFCell cell = row.getCell(j);
                if (cell == null) {
                    cell = row.createCell(j);
                }
                cell.setCellStyle(cellStyle);
            }
        }
    }

    /**
     * 创建标题与执行合并单元格
     * <pre>
     *  ExcelUtil.createHeaderBuilder(workbook, sheet, row0)
     *    .createHeader("0-0-0-3(整体数据)[247,176,127||true]")
     *    .createHeader("0-0-4-17(当日数据)[197,224,179||true]")
     *    .createHeader("0-0-18-19(1. 视频翻译)[178,199,230||true]")
     *    .createHeader("0-0-20-21(1. 视频克隆)[178,199,230||true]")
     *    .createHeader("0-0-22-23(1. 模特视频)[178,199,230||true]")
     *    .createHeader("");
     * </pre>
     * @param workbook  表格
     * @param sheet     工作簿
     * @param str       字符
     */
    private static void createDataByStr(XSSFWorkbook workbook, XSSFSheet sheet, String str) {
        String part1 = str.substring(0, str.indexOf("("));
        String[] part1Array = part1.split("-");
        String index0 = part1Array[0];
        String index1 = part1Array[1];
        String index2 = part1Array[2];
        String index3 = part1Array[3];
        String part2 = str.substring(str.indexOf("(") + 1, str.lastIndexOf(")"));
        String part3 = str.substring(str.indexOf("[") + 1, str.indexOf("]"));
        String[] part3Array = part3.split("\\|");
        String part3Index0 = part3Array[0];
        String part3Index1 = part3Array[1];
        String part3Index2 = part3Array[2];
        java.awt.Color bgColor = null;
        java.awt.Color fontColor = null;
        if (part3Index0 != null && !"".equals(part3Index0.trim())) {
            String[] split = part3Index0.split(",");
            bgColor = new java.awt.Color(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        }
        if (part3Index1 != null && !"".equals(part3Index1.trim())) {
            String[] split = part3Index1.split(",");
            fontColor = new java.awt.Color(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        }
        XSSFRow rowindex0 = sheet.getRow(Integer.parseInt(index0));
        if (rowindex0 == null) {
            rowindex0 = sheet.createRow(Integer.parseInt(index0));
        }
        XSSFCell row0cell = rowindex0.createCell(Integer.parseInt(index2));
        row0cell.setCellValue(part2);
        row0cell.setCellStyle(ExcelUtil.createStyle(workbook, bgColor , fontColor, Boolean.valueOf(part3Index2)));
        ExcelUtil.mergedRegion(sheet, new CellRangeAddress(Integer.parseInt(index0), Integer.parseInt(index1), Integer.parseInt(index2), Integer.parseInt(index3)));
    }

    public static HeaderCreate createBuilder(XSSFWorkbook workbook, XSSFSheet sheet){
        return new HeaderCreate(workbook, sheet);
    }

    @AllArgsConstructor
    public static class HeaderCreate{
        private XSSFWorkbook workbook;
        private XSSFSheet sheet;

        public HeaderCreate createData(String str){
            if (str != null && !str.trim().equals("")) {
                ExcelUtil.createDataByStr(workbook, sheet, str);
            }
            return this;
        }

        public HeaderCreate createRow(int rowNum, Short height){
            XSSFRow row = sheet.createRow(rowNum);
            if (height != null) {
                row.setHeight(height);
            }
            return this;
        }

        public HeaderCreate writeData(int startRow, List<Map<String, Object>> dataList){
            // 写入数据
            for (int i = 0; i < dataList.size(); i++) {
                Map<String, Object> rowMap = dataList.get(i);
                XSSFRow row = sheet.createRow(i + startRow);
                AtomicInteger index = new AtomicInteger();
                rowMap.forEach((k,o)->{
                    XSSFCell cell = row.createCell(index.get());
                    writeCell(cell, o);
                    cell.setCellStyle(ExcelUtil.createStyle(workbook, null, null, false));
                    index.getAndIncrement();
                });
            }
            return this;
        }
    }

    public static void writeCell(CellBase cell, Object o){
        if (o != null) {
            if (o instanceof Number) {
                cell.setCellValue(new Double(o.toString()));
            } else if (o instanceof Boolean) {
                cell.setCellValue(Boolean.parseBoolean(o.toString()));
            } else if (o instanceof Date) {
                cell.setCellValue(DateUtil.getDateFormat((Date) o));
            } else {
                //字符串
                final String cellValueStr = o.toString();
                //cell.setCellFormula("SUM(A2:C2)"); 设置函数 如："FUN=SUM(C2:INDEX(C:C,ROW()-1))"
                if (cellValueStr.startsWith("FUN=")) {
                    cell.setCellFormula(cellValueStr.substring(4));
                } else if(cellValueStr.startsWith("IMG=")){
                    //设置图片
                    try {
                        writePicture(cell, cellValueStr.substring(4));
                    } catch (Exception e) {
                        cell.setCellValue(cellValueStr);
                    }
                } else {
                    cell.setCellValue(cellValueStr);
                }
            }
        } else {
            cell.setCellValue("");
        }
    }

    public static void writePicture(CellBase cellBase, String path) throws Exception{
        Sheet sheet = cellBase.getSheet();
        Workbook workbook = sheet.getWorkbook();
        InputStream inputStream;
        if (path.startsWith("http")) {
            inputStream = HttpUtil.openStream(path);
        }else {
            inputStream = new FileInputStream(path);
        }
        byte[] bytes = IOUtils.toByteArray(inputStream);
        int i1 = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
        inputStream.close();
        // 在单元格中插入图片
        CreationHelper helper = workbook.getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setRow1(cellBase.getRowIndex());
        anchor.setCol1(cellBase.getColumnIndex());
        anchor.setRow2(cellBase.getRowIndex() + 1);
        anchor.setCol2(cellBase.getColumnIndex() + 1);
        drawing.createPicture(anchor, i1);
    }

    public static XSSFWorkbook workbook(File file) {
        XSSFWorkbook workbook;
        if (file.exists()) {
            File fileNew = new File(file.getAbsolutePath().replace(".xlsx", "_back.xlsx"));
            FileUtil.rename(file, fileNew.getAbsolutePath(), true);
            try {
                workbook = new XSSFWorkbook(fileNew);
            } catch (Exception e) {
                workbook = new XSSFWorkbook();
            }
        } else {
            workbook = new XSSFWorkbook();
        }
        return workbook;
    }


    public static XSSFSheet getOrCreateSheet(XSSFWorkbook workbook, String sheetName) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }
        return sheet;
    }

    public static XSSFRow getOrCreateRow(XSSFSheet sheet, int rownum) {
        XSSFRow row = sheet.getRow(rownum);
        if (row == null) {
            row = sheet.createRow(rownum);
        }
        return row;
    }


    public static XSSFCell getOrCreateCell(XSSFRow row, int cellnum) {
        XSSFCell cell = row.getCell(cellnum);
        if (cell == null) {
            cell = row.createCell(cellnum);
        }
        return cell;
    }

}
