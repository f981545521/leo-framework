package cn.acyou.leo.tool.test.poi;

import cn.acyou.leo.framework.media.encoder.MediaUtil;
import cn.acyou.leo.framework.util.ExcelUtil;
import cn.acyou.leo.framework.util.FileUtil;
import cn.acyou.leo.framework.util.RandomUtil;
import cn.acyou.leo.framework.util.StringUtils;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.Data;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoSize;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/17 17:35]
 **/
public class MainTest1234 {

    public static void main123(String[] args) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\poi\\分段1.xlsx"));
        XSSFSheet sheet = workbook.getSheet("Sheet1");
        List<String> s = new ArrayList<>();
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(1);
            if (row != null) {
                XSSFCell cell = row.getCell(0);
                String o = cell.getStringCellValue();
                if (o != null && !o.toString().isEmpty()) {
                    s.add(o.toString());
                }
            }

        }
        System.out.println("'" + StringUtils.join(s, "','") + "'");
    }




    public static void main(String[] args) throws Exception {
        Map<String, String> table1Map = new HashMap<>();

        XSSFWorkbook workbook2 = new XSSFWorkbook(new File("D:\\poi2\\uni1.xlsx"));
        List<Map<String, Object>> dataList2 = ExcelUtil.importData(workbook2.getSheetAt(0));
        PrintWriter printWriter = FileUtil.getPrintWriter("D:\\poi2\\数据修正_ALL.sql", StandardCharsets.UTF_8, false);
        for (int i = 0; i < dataList2.size(); i++) {
            String openid = dataList2.get(i).get("openid").toString();
            String unionid = dataList2.get(i).get("unionid").toString();
            table1Map.put(openid, unionid);
        }


        for (int i = 1; i <= 5; i++) {
            XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\poi2\\"+i+".xlsx"));
            List<Map<String, Object>> dataList = ExcelUtil.importData(workbook.getSheetAt(0));

            for (int j = 0; j < dataList.size(); j++) {
                String openid = dataList.get(j).get("openid").toString();
                String customer_id = dataList.get(j).get("customer_id").toString();
                String unionid = "";
                String s = table1Map.get(openid);
                if (s != null) {
                    unionid = s;
                }
                String sql = "update member set openid = '%s', unionid = '%s' where external_member_id = %s;\r\n";
                String format = String.format(sql, openid, unionid, customer_id);
                printWriter.write(format);

            }
        }

        printWriter.flush();
        printWriter.close();
        System.out.println("解析完成");
    }



    public static void main1(String[] args) throws Exception {
        Map<String, String> table1Map = new HashMap<>();

        for (int i = 1; i <= 5; i++) {
            XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\poi2\\"+i+".xlsx"));
            List<Map<String, Object>> dataList = ExcelUtil.importData(workbook.getSheetAt(0));

            for (int j = 0; j < dataList.size(); j++) {
                String openid = dataList.get(j).get("openid").toString();
                String customer_id = dataList.get(j).get("customer_id").toString();
                table1Map.put(openid, customer_id);
            }
        }


        XSSFWorkbook workbook2 = new XSSFWorkbook(new File("D:\\poi2\\uni1.xlsx"));
        List<Map<String, Object>> dataList2 = ExcelUtil.importData(workbook2.getSheetAt(0));
        PrintWriter printWriter = FileUtil.getPrintWriter("D:\\poi2\\数据修正.sql", StandardCharsets.UTF_8, false);

        for (int i = 0; i < dataList2.size(); i++) {
            String openid = dataList2.get(i).get("openid").toString();
            String unionid = dataList2.get(i).get("unionid").toString();
            String customer_id = "";
            String s = table1Map.get(openid);
            if (s != null) {
                customer_id = s;
            }
            String sql = "update member set openid = '%s', unionid = '%s' where external_member_id = %s;\r\n";
            String format = String.format(sql, openid, unionid, customer_id);
            printWriter.write(format);
        }

        printWriter.flush();
        printWriter.close();
        System.out.println("解析完成");
    }
















    @Test
    public void test32324() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\temp\\poi2\\0.1.10周二上线版本公共模特及音色.xlsx"));
        List<Map<String, Object>> dataList = ExcelUtil.importData(workbook.getSheetAt(0));
        PrintWriter printWriter = FileUtil.getPrintWriter("D:\\temp\\poi2\\0.1.10周二上线版本公共模特及音色_export2.sql", StandardCharsets.UTF_8, false);
        String name = null;
        long currentTimeMillis = System.currentTimeMillis();
        long startId = 200;
        for (Map<String, Object> objectMap : dataList) {
            Object nameItem = objectMap.get("名字");
            if (nameItem != null) {
                name = nameItem.toString();
            }
            if (objectMap.get("模特视频位置") == null || StringUtils.isBlank(objectMap.get("模特视频位置").toString())) {
                continue;
            }
            String format = DateUtil.format(new Date(currentTimeMillis), "yyyy-MM-dd HH:mm:ss");
            String videoPath = objectMap.get("模特视频位置").toString();
            //https://anylang.obs.ap-southeast-3.myhuaweicloud.com/anylang-video/resources/robot_public/index/avatar/cn/m1_16_9.mp4
            String videoUrl = "https://anylang.obs.ap-southeast-3.myhuaweicloud.com/anylang-video/resources/robot_public" + videoPath.replaceAll("\\\\", "/");
            String coverUrl = videoUrl.substring(0, videoUrl.lastIndexOf(".")) + "_cover.png";
            MultimediaInfo mediaInfo = MediaUtil.instance().getMediaInfo(videoUrl);
            VideoSize realVideoSize = MediaUtil.getRealVideoSize(mediaInfo);
            String sql = "INSERT INTO ffo.user_video_robot " +
                    "(id, user_id, robot_name, robot_code, scene_code, cover_url, video_url, duration, vertical, horizontal, train_status, " +
                    "tts_id, demo_video_make_status, demo_cover_url, demo_video_url, `type`, ext, del_flag, create_time, update_time) " +
                    "VALUES(" + startId + ", -1, '" + name + "', NULL, NULL, '" + coverUrl + "', '" + videoUrl + "', " + mediaInfo.getDuration() + ", '" + realVideoSize.getHeight() + "', '" + realVideoSize.getWidth() + "', 20, NULL," +
                    " '20', '" + coverUrl + "', '" + videoUrl + "', 2, NULL, 0, '" + format + "', '" + format + "');\r\n";
            printWriter.write(sql);
            startId++;
            currentTimeMillis = currentTimeMillis - (5 * 60 * 1000);
        }
        printWriter.flush();
        printWriter.close();
        System.out.println("解析完成");
    }

    @Test
    public void testRead1() {
        List<Map<Integer, Object>> objects = EasyExcel.read("D:\\temp\\poi2\\0.1.10周二上线版本公共模特及音色.xlsx")
                .sheet(0).doReadSync();
        System.out.println(objects);
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
        ExcelUtil.exportExcel(new FileOutputStream("D:\\temp\\统计数据V3_" + System.currentTimeMillis() + ".xlsx"), objects, "列表");
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
