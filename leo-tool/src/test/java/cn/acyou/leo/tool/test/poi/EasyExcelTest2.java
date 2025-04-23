package cn.acyou.leo.tool.test.poi;


import cn.acyou.leo.framework.util.DateUtil;
import cn.acyou.leo.framework.util.RandomUtil;
import cn.acyou.leo.tool.test.poi.pojo.DemoData;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author youfang
 * @version [1.0.0, 2025/4/19 17:17]
 **/
public class EasyExcelTest2 {
    @Test
    public void repeatedWrite() {
        String fileName = "D:\\temp\\easy_excel\\AAA" + System.currentTimeMillis() + ".xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet(0, "模板" + 0).build();
        for (int i = 0; i < 100; i++) {
            List<DemoData> data = createDataList(10000);
            //java.lang.IllegalArgumentException: Invalid row number (1048576) outside allowable range (0..1048575)
            excelWriter.write(data, writeSheet);
        }
        excelWriter.finish();
    }

    @Test
    public void writeAll() {
        String fileName = "D:\\temp\\easy_excel\\AAA" + System.currentTimeMillis() + ".xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build();
        //for (int i = 0; i < 100; i++) {
        //    List<DemoData> data = createDataList(12000);
        //    //java.lang.IllegalArgumentException: Invalid row number (1048576) outside allowable range (0..1048575)
        //    writeExcel(excelWriter, data);
        //}

        List<DemoData> data = createDataList(12000);
        writeExcel(excelWriter, data);
        excelWriter.finish();
    }

    final AtomicLong count = new AtomicLong(0);
    final long everySheetMaxCount = 5000;
    private void writeExcel(ExcelWriter excelWriter, List<DemoData> data) {
        Map<WriteSheet, List<DemoData>> sheetDataMap = new HashMap<>();
        while ((data.size() + count.get()) > everySheetMaxCount) {
            long s = everySheetMaxCount - count.get();
            List<DemoData> partList = data.subList(0, Math.toIntExact(s));
            int sheetIndex = (int) ((partList.size() + count.get())/everySheetMaxCount) - 1;
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetIndex, "模板" + sheetIndex).build();
            sheetDataMap.put(writeSheet, partList);
            count.getAndAdd(Math.toIntExact(s));
            data.removeAll(partList);
        }
        sheetDataMap.forEach((sheet, dataList) -> excelWriter.write(dataList, sheet));
    }

    private List<DemoData> createDataList(int count) {
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
