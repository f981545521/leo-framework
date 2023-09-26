package cn.acyou.leo.tool.test.poi;

import cn.acyou.leo.framework.util.RandomUtil;
import cn.acyou.leo.tool.test.util.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.Data;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/17 17:35]
 **/
public class MainTest1234 {

    public static void main(String[] args) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\temp\\poi\\TTS导入-模版.xlsx"));
        int numberOfSheets = workbook.getNumberOfSheets();
        System.out.println("共有 " + numberOfSheets + " Sheet页");
        List<String> sheetNameList = new ArrayList<>();
        for (int i = 0; i < numberOfSheets; i++) {
            sheetNameList.add(workbook.getSheetAt(i).getSheetName());
        }
        System.out.println(sheetNameList);
        XSSFSheet sheet = workbook.getSheet(sheetNameList.get(0));
        // 返回数据
        List<Map<String, Object>> ls = ExcelUtil.transferData(sheet);
        System.out.println(ls);
    }

    @Test
    public void test283() throws Exception {
        List<Map<String, Object>> objects = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("id", 1000 + i);
            data.put("name", RandomUtil.randomUserName());
            data.put("age", RandomUtil.randomAge());
            objects.add(data);
        }
        ExcelUtil.exportExcel(new FileOutputStream("D:\\poi\\111-11.xlsx"), objects, "列表");
    }

    @Test
    public void test1() {
        List<Map<String, String>> objects = EasyExcel.read(new File("D:\\temp\\poi\\TTS导入-模版.xlsx")).sheet(0).doReadSync();
        System.out.println(objects);
    }

    @Test
    public void test2() {
        List<Map<String, String>> objects = EasyExcel.read(new File("D:\\temp\\poi\\TTS导入-模版.xlsx"), Map.class, new ReadListener() {
            @Override
            public void invoke(Object data, AnalysisContext context) {

            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {

            }
        }).sheet(0).doReadSync();
        System.out.println(objects);
    }


    @Data
    public static class IndexOrNameData {
        @ExcelProperty("tts_speaker")
        private String tts_speaker;
        @ExcelProperty("speaker")
        private String speaker;
        @ExcelProperty("分类")
        private String type;
        @ExcelProperty("display")
        private String display;
        @ExcelProperty("dis_type")
        private String dis_type;
        @ExcelProperty("lang")
        private String lang;
        @ExcelProperty("img_url")
        private String img_url;
        @ExcelProperty("audition_url")
        private String audition_url;
        @ExcelProperty("ActionArgs")
        private String ActionArgs;
        @ExcelProperty("features")
        private String features;
    }

}
