package cn.acyou.leo.tool.test.poi;


import cn.acyou.leo.framework.util.DateUtil;
import cn.acyou.leo.framework.util.RandomUtil;
import cn.acyou.leo.tool.test.poi.pojo.DemoData;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author youfang
 * @version [1.0.0, 2025/4/23 9:50]
 **/
public class MainTest2002 {
    public static void main(String[] args) throws Exception {
        ExportExec exportExec = createExportExec("LargeData", 3333333L);
        Sheet sheet = exportExec.getWorkbook().createSheet("LargeData1");
        exportExec.createHeader(sheet, new String[][]{});


        int rowNum = 1; // 数据开始的行号

        try {
            for (int i = 0; i < 10; i++) {
                List<DemoData> dataList = createDataList(1000);
                // 将数据写入到Excel中
                for (DemoData data : dataList) {
                    Row row = sheet.createRow(rowNum++);
                    createCell(exportExec.getDataCellStyle(), row, 0).setCellValue(exportExec.getSerial().getAndIncrement());
                    createCell(exportExec.getDataCellStyle(), row, 1).setCellValue(data.getName());
                    createCell(exportExec.getDataCellStyle(), row, 2).setCellValue(data.getAge());
                    createCell(exportExec.getDataCellStyle(), row, 3).setCellValue(DateUtil.getDateTimeFormat(data.getBirthday()));
                }
                // 处理完成一批数据后，可以选择清除缓存数据，防止内存溢出
                ((SXSSFSheet) sheet).flushRows(dataList.size()); // 每次写入一批数据后，通过flushRows(batchSize)将缓存的行从内存中清除，以控制内存占用。
            }
            // 将数据写入文件
            try (FileOutputStream fos = new FileOutputStream("D:\\temp\\AAAV4.xlsx")) {
                exportExec.getWorkbook().write(fos);
            }
            System.out.println("数据导出完成");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            exportExec.close();
        }
    }

    private static ExportExec createExportExec(String sheetName, long totalCount) {
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);//表示内存中最多保留100行数据，超过的部分会写入临时文件，节省内存。
        workbook.setCompressTempFiles(true); // 启用临时文件压缩，进一步减少磁盘空间占用。
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        ExportExec exportExec = new ExportExec(workbook, cellStyle, cellStyle);
        exportExec.setTotalCount(totalCount);
        if (sheetName != null) {
            exportExec.setSheetName(sheetName);
        }
        return exportExec;
    }

    @Data
    static class ExportExec{
        SXSSFWorkbook workbook;
        CellStyle headCellStyle;
        CellStyle dataCellStyle;
        Long totalCount;

        String sheetName = "sheet";
        AtomicLong serial = new AtomicLong(1);
        static Integer Max_Total = 1000000;//最大大小

        public ExportExec(SXSSFWorkbook workbook, CellStyle headCellStyle, CellStyle dataCellStyle) {
            this.workbook = workbook;
            this.headCellStyle = headCellStyle;
            this.dataCellStyle = dataCellStyle;
        }

        public void close() {
            workbook.dispose();//关闭workbook并删除临时文件
            try {
                workbook.close();
            } catch (IOException e) {
                //ignore
            }
        }

        public void createHeader(Sheet sheet, String[][] headers) {
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellStyle(headCellStyle);
                cell.setCellValue(headers[i][1]);
            }
        }
    }

    private static Cell createCell(CellStyle cellStyle, Row row,  int column) {
        Cell cell = row.createCell(column);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    private static List<DemoData> createDataList(int count) {
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            DemoData data = new DemoData();
            data.setName(RandomUtil.randomUserName());
            data.setAge(RandomUtil.randomAge());
            data.setBirthday(DateUtil.randomDate());
            list.add(data);
        }
        return list;
    }
}
