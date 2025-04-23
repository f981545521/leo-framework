package cn.acyou.leo.tool.test.poi;


import cn.acyou.leo.framework.util.DateUtil;
import cn.acyou.leo.framework.util.RandomUtil;
import cn.acyou.leo.tool.test.poi.pojo.DemoData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
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
public class MainTest2001 {
    public static void main(String[] args) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);//表示内存中最多保留100行数据，超过的部分会写入临时文件，节省内存。
        workbook.setCompressTempFiles(true); // 启用临时文件压缩，进一步减少磁盘空间占用。
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Sheet sheet = workbook.createSheet("Large Data");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"序号", "姓名", "年龄", "生日"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(headers[i]);
        }
        int rowNum = 1; // 数据开始的行号
        AtomicLong serial = new AtomicLong(1);
        try {
            for (int i = 0; i < 1000; i++) {
                List<DemoData> dataList = createDataList(1000);
                // 将数据写入到Excel中
                for (DemoData data : dataList) {
                    Row row = sheet.createRow(rowNum++);
                    createCell(cellStyle, row, 0).setCellValue(serial.getAndIncrement());
                    createCell(cellStyle, row, 1).setCellValue(data.getName());
                    createCell(cellStyle, row, 2).setCellValue(data.getAge());
                    createCell(cellStyle, row, 3).setCellValue(DateUtil.getDateTimeFormat(data.getBirthday()));
                }
                // 处理完成一批数据后，可以选择清除缓存数据，防止内存溢出
                ((SXSSFSheet) sheet).flushRows(dataList.size()); // 每次写入一批数据后，通过flushRows(batchSize)将缓存的行从内存中清除，以控制内存占用。
            }
            // 将数据写入文件
            try (FileOutputStream fos = new FileOutputStream("D:\\temp\\AAAV3.xlsx")) {
                workbook.write(fos);
            }
            System.out.println("数据导出完成");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            workbook.dispose();//关闭workbook并删除临时文件
            workbook.close();
        }
    }

    private static void createExportExec(){

    }

    static class ExportExec{

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
