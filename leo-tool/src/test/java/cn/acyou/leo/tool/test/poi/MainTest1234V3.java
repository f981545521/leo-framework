package cn.acyou.leo.tool.test.poi;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.acyou.leo.framework.media.encoder.MediaUtil;
import cn.acyou.leo.framework.util.*;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoSize;
import ws.schild.jave.process.ProcessWrapper;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/17 17:35]
 **/
@Slf4j
public class MainTest1234V3 {

    public static void main(String[] args) {
        MultimediaInfo mediaInfo = MediaUtil.instance().getMediaInfo("https://digital-public-toc.obs.cn-east-3.myhuaweicloud.com/resources/v1/动物/阿虎.mp4");
        VideoSize realVideoSize = MediaUtil.getRealVideoSize(mediaInfo);
        System.out.println(realVideoSize);
    }

    @Test
    public void coverDi2r() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger(DefaultFFMPEGLocator.class).setLevel(Level.ERROR);
        loggerContext.getLogger(MultimediaObject.class).setLevel(Level.ERROR);
        loggerContext.getLogger(ProcessWrapper.class).setLevel(Level.ERROR);
        File file = new File("C:\\Users\\1\\Downloads\\正确的");
        for (File listFile : file.listFiles()) {
            System.out.println(listFile.getAbsolutePath() + "时长：" + MediaUtil.instance().getMediaInfo(listFile.getAbsolutePath()).getDuration());
        }
    }


    @Test
    public void coverDir() {
        MediaUtil.instance().extractCoverDir(new File("C:\\Users\\1\\Downloads\\驱动图片"));
    }

    @Test
    public void 音色上架文案制作() throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\Guiji.cn-待上架模板视频\\Guiji.cn公共模特+声音_202409.xlsx"));
        List<Map<String, Object>> dataList = ExcelUtil.importData(workbook.getSheet("新增-公共音色"), 1);
        for (Map<String, Object> objectMap : dataList) {
            String speaker_id = objectMap.get("音色ID").toString();
            String status = StringUtils.toStr(objectMap.get("状态")).trim();
            if (StringUtils.isBlank(speaker_id)) {
                continue;
            }
            if (!status.contains("待上架")) {
                continue;
            }
            String name = objectMap.get("上线名称").toString();
            String audioUrlExcel = objectMap.get("音频路径").toString();
            String intro = objectMap.get("示例文案").toString();
            String res = HttpUtil.createPost("https://zh.api.guiji.cn/avatar2c/tool/sec_tts")
                    //.header("token", "754d6f8d032f401a85aa7914c679e462")
                    .body("{\n" +
                            "  \"text\":\""+intro+"\",\n" +
                            "  \"speaker_id\":\""+speaker_id+"\"\n" +
                            "}").execute().body();
            JSONObject jsonObject = JSON.parseObject(res);
            String audioUrl = jsonObject.getString("data");
            System.out.println("音频地址：" + audioUrl);

            if (StringUtils.isBlank(audioUrl)) {
                log.error("音频合成失败！" + "[" + name + "("+speaker_id+")]: " + intro);
            }else {
                System.out.println("合成成功 ===" + audioUrl );
                HttpUtil.downloadFile(audioUrl, "D:\\Guiji.cn-待上架模板视频\\上线音色\\" + name + ".wav");
                System.out.println("下载完成");
            }
        }
        System.out.println("解析完成");
    }

    @Test
    public void 音色上架() throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\Guiji.cn-待上架模板视频\\Guiji.cn公共模特+声音_202409.xlsx"));
        List<Map<String, Object>> dataList = ExcelUtil.importData(workbook.getSheet("新增-公共音色"), 1);
        PrintWriter printWriter = FileUtil.getPrintWriter("D:\\Guiji.cn-待上架模板视频\\Guiji.cn公共模特+声音_202409_exportTTS.sql", StandardCharsets.UTF_8, false);
        Set<String> classifySet = new HashSet<>();
        for (Map<String, Object> objectMap : dataList) {
            String speaker_id = objectMap.get("音色ID").toString();
            if (StringUtils.isBlank(speaker_id)) {
                continue;
            }
            classifySet.add(objectMap.get("所属分类").toString());
        }
        for (String classifyName : classifySet) {
            //分类SQL
            String sql = "INSERT INTO `ffo-toc`.`user_video_classify` ( `name`, `type`, `platform`, `status`) select '"+classifyName+"', 2, 'cn', 1 from dual WHERE NOT EXISTS ( SELECT id FROM `ffo-toc`.`user_video_classify` WHERE name = '"+classifyName+"' and type = 2);";
            printWriter.write(sql);
            printWriter.write("\r\n");
        }
        printWriter.write("--  数据\r\n");
        long currentTimeMillis = System.currentTimeMillis();
        for (Map<String, Object> objectMap : dataList) {
            String format = DateUtil.format(new Date(currentTimeMillis), "yyyy-MM-dd HH:mm:ss");
            String speaker_id = objectMap.get("音色ID").toString();
            String status = StringUtils.toStr(objectMap.get("状态")).trim();
            if (StringUtils.isBlank(speaker_id)) {
                continue;
            }
            if (!status.contains("待上架")) {
                continue;
            }
            String lang = objectMap.get("语种简称").toString();
            String langZh = objectMap.get("语种").toString();
            String name = objectMap.get("上线名称").toString();
            String audioUrl = objectMap.get("音频路径").toString();
            String audioText = objectMap.get("示例文案").toString();

            String sexStr = objectMap.get("性别").toString();
            String label = objectMap.get("标签").toString();
            String classifyName = objectMap.get("所属分类").toString();
            String sex = (sexStr.contains("男")?"male":"female");
            String coverUrl = "https://digital-public-toc.obs.cn-east-3.myhuaweicloud.com/common/ttsCover/default1.png";
            if ("female".equals(sex)) {
                coverUrl = "https://digital-public-toc.obs.cn-east-3.myhuaweicloud.com/common/ttsCover/female" + RandomUtil.randomRangeNumber(1,6) + ".png";
            }
            if ("male".equals(sex)) {
                coverUrl = "https://digital-public-toc.obs.cn-east-3.myhuaweicloud.com/common/ttsCover/male" + RandomUtil.randomRangeNumber(1,8) + ".png";
            }
            //https://digital-public.obs.cn-east-3.myhuaweicloud.com/anylang/anylang-video/resources/robot_public/20240305TTS/index/timbre/ar/m1/.DS_Store
            //String audioUrl = "https://digital-public.obs.cn-east-3.myhuaweicloud.com/anylang/anylang-video/resources/robot_public/20240305TTS" + path.replaceAll("\\\\", "/");
            String sql = "INSERT INTO `ffo-toc`.user_video_tts " +
                    "(user_id, speaker_id, speaker, audio_url, speaker_label, cover_url, audition_url, sex, lang, train_status, robot_id, `type`, free_type, ext, del_flag, create_time, update_time) " +
                    "VALUES( -1, '" + speaker_id + "', '" + name + "', '2024-09-14', '"+label+"', '" + coverUrl + "','" + audioUrl + "', '"+sex+"', '" + lang + "', 20, NULL, 2, 1, " +
                    "'{\"lang\":\"" + langZh + "\",\"ttsSource\":\"2\"}', 0, '" + format + "', '" + format + "'); ";
            printWriter.write(sql);
            printWriter.write("\r\n");

            String sql2 = "INSERT IGNORE INTO `ffo-toc`.`user_video_classify_relation` (`classify_id`, `bind_id`) " +
                    "SELECT * from ( SELECT id as classify_id FROM `ffo-toc`.`user_video_classify` WHERE name = '"+classifyName+"' and type = 2) t1, ( SELECT id as bind_id FROM `ffo-toc`.`user_video_tts` WHERE speaker = '"+name+"' and type = 2) t2;\n";

            printWriter.write(sql2);
            printWriter.write("\r\n");


            String recommend =  StringUtils.toStr(objectMap.get("是否推荐")).trim();

            if (recommend.contains("是")) {
                String sql3 = "INSERT IGNORE INTO `ffo-toc`.`user_video_classify_relation` (`classify_id`, `bind_id`) " +
                        "SELECT * from ( SELECT id as classify_id FROM `ffo-toc`.`user_video_classify` WHERE name = '推荐' and type = 2) t1, ( SELECT id as bind_id FROM `ffo-toc`.`user_video_tts` WHERE speaker = '"+name+"' and type = 2) t2;\n";

                printWriter.write(sql3);
                printWriter.write("\r\n");
            }
            currentTimeMillis = currentTimeMillis - (5 * 60 * 1000);
        }
        printWriter.flush();
        printWriter.close();
        System.out.println("解析完成");
    }

    @Test
    public void 形象上架() throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\Guiji.cn-待上架模板视频\\Guiji.cn公共模特+声音_202409.xlsx"));
        List<Map<String, Object>> dataList = ExcelUtil.importData(workbook.getSheet("公模-初筛选-待确认"), 1);
        PrintWriter printWriter = FileUtil.getPrintWriter("D:\\Guiji.cn-待上架模板视频\\Guiji.cn公共模特+声音_202409_exportRobot.sql", StandardCharsets.UTF_8, false);
        Set<String> classifySet = new HashSet<>();
        for (Map<String, Object> objectMap : dataList) {
            String name =  StringUtils.toStr(objectMap.get("模特名称"));
            if (StringUtils.isBlank(name)) {
                continue;
            }
            classifySet.add(objectMap.get("所属分类").toString());
        }
        for (String classifyName : classifySet) {
            //分类SQL
            String sql = "INSERT INTO `ffo-toc`.`user_video_classify` ( `name`, `type`, `platform`, `status`) select '"+classifyName+"', 1, 'cn', 1 from dual WHERE NOT EXISTS ( SELECT id FROM `ffo-toc`.`user_video_classify` WHERE name = '"+classifyName+"' and type = 1);";
            printWriter.write(sql);
            printWriter.write("\r\n");
        }
        printWriter.write("--  数据\r\n");
        printWriter.flush();
        long currentTimeMillis = new Date().getTime();// DateUtil.parseDateTime("2024-09-19 21:37:29").getTime();
        for (Map<String, Object> objectMap : dataList) {
            String format = DateUtil.format(new Date(currentTimeMillis), "yyyy-MM-dd HH:mm:ss");
            String name =  StringUtils.toStr(objectMap.get("模特名称")).trim();
            if (StringUtils.isBlank(name)) {
                continue;
            }
            String status = StringUtils.toStr(objectMap.get("是否上架")).trim();
            String jd = StringUtils.toStr(objectMap.get("进度")).trim();
            if (!status.contains("√") || !jd.contains("待上架")) {
                continue;
            }
            String age = objectMap.get("年龄").toString().replaceAll("岁", "");
            String classifyName = objectMap.get("所属分类").toString();
            String countryStr = objectMap.get("国别").toString();
            String country = (countryStr.contains("中")?"中国":(countryStr.contains("外")?"外国":countryStr));
            String sexStr = objectMap.get("性别").toString();
            String label = objectMap.get("标签").toString();
            String demoText = "";//StringUtils.toStr(objectMap.get("首页介绍话术")).trim().replaceAll("'", "\\'");
            String sex = (sexStr.contains("男")?"male":"female");
            String attitudeStr = objectMap.get("姿态").toString();
            String tts_match =  StringUtils.toStr(objectMap.get("配音")).trim();
            String attitude = "0";
            if ("站姿".equals(attitudeStr)) {
                attitude = "1";
            }
            if ("坐姿".equals(attitudeStr)) {
                attitude = "2";
            }
            String screenTypeStr = objectMap.get("实景/绿幕").toString();
            String screenType = "0";
            if ("绿幕".equals(attitudeStr)) {
                screenType = "1";
            }
            if ("实景".equals(attitudeStr)) {
                screenType = "2";
            }

            String videoUrl = "https://digital-public-toc.obs.cn-east-3.myhuaweicloud.com/resources/v6/" + name + ".mp4";
            if (!cn.acyou.leo.framework.util.HttpUtil.reachable(videoUrl)) {
                log.error("地址不可达：" + videoUrl);
                continue;
            }
            String coverUrl = videoUrl.substring(0, videoUrl.lastIndexOf(".")) + ".jpg";
            String videoDemoUrl = "https://digital-public-toc.obs.cn-east-3.myhuaweicloud.com/resources/v6/" + name + "_demo.mp4";
            String coverDemoUrl = videoUrl.substring(0, videoUrl.lastIndexOf(".")) + ".jpg";
            //String coverDemoUrl = videoUrl.substring(0, videoUrl.lastIndexOf(".")) + "_demo_cover.jpg";
            MultimediaInfo mediaInfo = MediaUtil.instance().getMediaInfo(videoUrl);
            long duration = mediaInfo.getDuration();
            long demoDuration = MediaUtil.instance().getMediaDuration(videoDemoUrl);
            VideoSize realVideoSize = MediaUtil.getRealVideoSize(MediaUtil.instance().getMediaInfo(videoDemoUrl));
            String animal_url = "null";
            String demo_text = StringUtils.toStr(objectMap.get("首页介绍话术")).trim().replaceAll("'", "\\\\'");
            // TODO {"demo_text":"","tts_match":"Tom"} ->  {"demoVideoDuration":4440} demo_duration单独字段
            // TODO 图片 or 视频 压缩JPG
            String sql = "INSERT INTO `ffo-toc`.`user_video_robot` (`user_id`, `robot_name`, `robot_code`, `scene_code`, `sex`, `label`, `country`, `attitude`, `age`, `screen_type`, `cover_url`," +
                    " `video_url`, `duration`, `vertical`, `horizontal`, `train_status`, `tts_id`, `demo_video_make_status`, `demo_text`, `demo_cover_url`, `demo_video_url`, `type`, `free_type`, `animal_url`, `silent_video_url`," +
                    " `ext`, `del_flag`, `create_time`, `update_time`, `remark`, `demo_duration`) VALUES " +
                    "( -1, '"+name+"', NULL, NULL, '"+sex+"', '"+label+"', '"+country+"', '"+attitude+"', '"+age+"', '"+screenType+"', '"+coverUrl+"', '"+videoUrl+"', '"+duration+"', " +
                    "'" + realVideoSize.getHeight() + "', '" + realVideoSize.getWidth() + "', " +
                    "20, (SELECT id FROM user_video_tts WHERE speaker = '"+tts_match+"' and type = 2 limit 1), '20', '"+demo_text+"', '"+coverDemoUrl+"', '"+videoDemoUrl+"', 2, 1, "+animal_url+", '"+videoUrl+"', '{\"demo_text\":\"" + demoText + "\",\"tts_match\":\"" + tts_match + "\"}', 0, " +
                    "'" + format + "', '" + format + "', NULL, '"+demoDuration+"');\n";
            printWriter.write(sql);
            String sql2 = "INSERT IGNORE INTO `ffo-toc`.`user_video_classify_relation` (`classify_id`, `bind_id`) " +
                    "SELECT * from ( SELECT id as classify_id FROM `ffo-toc`.`user_video_classify` WHERE name = '"+classifyName+"' and type = 1) t1, ( SELECT id as bind_id FROM `ffo-toc`.`user_video_robot` WHERE robot_name = '"+name+"' and type = 2) t2;\n";

            printWriter.write(sql2);

            String recommend =  StringUtils.toStr(objectMap.get("是否推荐")).trim();
            if (recommend.contains("是")) {
                String sql3 = "INSERT IGNORE INTO `ffo-toc`.`user_video_classify_relation` (`classify_id`, `bind_id`) " +
                        "SELECT * from ( SELECT id as classify_id FROM `ffo-toc`.`user_video_classify` WHERE name = '推荐' and type = 1) t1, ( SELECT id as bind_id FROM `ffo-toc`.`user_video_robot` WHERE robot_name = '"+name+"' and type = 2) t2;\n";
                printWriter.write(sql3);
            }
            printWriter.write("\r\n");

            printWriter.flush();
            currentTimeMillis = currentTimeMillis - (5 * 60 * 1000);
        }
        printWriter.flush();
        printWriter.close();
        System.out.println("解析完成");
    }

    @Test
    public void 形象上架动物IP() throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\Guiji.cn-待上架模板视频\\Guiji.cn公共模特+声音_202409.xlsx"));
        List<Map<String, Object>> dataList = ExcelUtil.importData(workbook.getSheet("动物IP"), 1);
        PrintWriter printWriter = FileUtil.getPrintWriter("D:\\Guiji.cn-待上架模板视频\\Guiji.cn公共模特+声音_202409_exportRobot.sql", StandardCharsets.UTF_8, false);
        Set<String> classifySet = new HashSet<>();
        for (Map<String, Object> objectMap : dataList) {
            String name =  StringUtils.toStr(objectMap.get("模特名称"));
            if (StringUtils.isBlank(name)) {
                continue;
            }
            classifySet.add(objectMap.get("所属分类").toString());
        }
        for (String classifyName : classifySet) {
            //分类SQL
            String sql = "INSERT INTO `ffo-toc`.`user_video_classify` ( `name`, `type`, `platform`, `status`) select '"+classifyName+"', 1, 'cn', 1 from dual WHERE NOT EXISTS ( SELECT id FROM `ffo-toc`.`user_video_classify` WHERE name = '"+classifyName+"' and type = 1);";
            printWriter.write(sql);
            printWriter.write("\r\n");
        }
        printWriter.write("--  数据\r\n");
        printWriter.flush();
        long currentTimeMillis = System.currentTimeMillis();
        for (Map<String, Object> objectMap : dataList) {
            String format = DateUtil.format(new Date(currentTimeMillis), "yyyy-MM-dd HH:mm:ss");
            String name =  StringUtils.toStr(objectMap.get("模特名称")).trim();
            if (StringUtils.isBlank(name)) {
                continue;
            }
            String status = StringUtils.toStr(objectMap.get("是否上架")).trim();
            String jd = StringUtils.toStr(objectMap.get("进度")).trim();
            if (!status.contains("√") || !jd.contains("待上架")) {
                continue;
            }
            String age = objectMap.get("年龄").toString().replaceAll("岁", "");
            String classifyName = objectMap.get("所属分类").toString();
            String countryStr = objectMap.get("国别").toString();
            String country = (countryStr.contains("中")?"中国":(countryStr.contains("外")?"外国":countryStr));
            String sexStr = objectMap.get("性别").toString();
            String label = objectMap.get("标签").toString();
            String demoText = "";//StringUtils.toStr(objectMap.get("首页介绍话术")).trim().replaceAll("'", "\\'");
            String sex = (sexStr.contains("男")?"male":"female");
            String attitudeStr = objectMap.get("姿态").toString();
            String tts_match =  StringUtils.toStr(objectMap.get("配音")).trim();
            String attitude = "0";
            if ("站姿".equals(attitudeStr)) {
                attitude = "1";
            }
            if ("坐姿".equals(attitudeStr)) {
                attitude = "2";
            }
            String screenTypeStr = objectMap.get("实景/绿幕").toString();
            String screenType = "0";
            if ("绿幕".equals(attitudeStr)) {
                screenType = "1";
            }
            if ("实景".equals(attitudeStr)) {
                screenType = "2";
            }

            String videoUrl = "https://digital-public-toc.obs.cn-east-3.myhuaweicloud.com/resources/animal-1008/" + name + ".mp4";
            if (!cn.acyou.leo.framework.util.HttpUtil.reachable(videoUrl)) {
                log.error("地址不可达：" + videoUrl);
                continue;
            }
            String coverUrl = videoUrl.substring(0, videoUrl.lastIndexOf(".")) + "_cover.jpg";
            String videoDemoUrl = "https://digital-public-toc.obs.cn-east-3.myhuaweicloud.com/resources/animal-1008/" + name + "_demo.mp4";
            String coverDemoUrl = videoUrl.substring(0, videoUrl.lastIndexOf(".")) + "_demo.jpg";
            //String coverDemoUrl = videoUrl.substring(0, videoUrl.lastIndexOf(".")) + "_demo_cover.jpg";
            MultimediaInfo mediaInfo = MediaUtil.instance().getMediaInfo(videoUrl);
            long duration = mediaInfo.getDuration();
            long demoDuration = MediaUtil.instance().getMediaDuration(videoDemoUrl);
            VideoSize realVideoSize = MediaUtil.getRealVideoSize(MediaUtil.instance().getMediaInfo(videoDemoUrl));
            //动物的驱动照片地址
            String animal_url = "'https://digital-public-toc.obs.cn-east-3.myhuaweicloud.com/resources/animal-1008/" + name + "_driver.jpg'";
            //String animal_url = "null";
            String demo_text = StringUtils.toStr(objectMap.get("首页介绍话术")).trim().replaceAll("'", "\\\\'");
            // TODO {"demo_text":"","tts_match":"Tom"} ->  {"demoVideoDuration":4440} demo_duration单独字段
            // TODO 图片 or 视频 压缩JPG
            String sql = "INSERT INTO `ffo-toc`.`user_video_robot` (`user_id`, `robot_name`, `robot_code`, `scene_code`, `sex`, `label`, `country`, `attitude`, `age`, `screen_type`, `cover_url`," +
                    " `video_url`, `duration`, `vertical`, `horizontal`, `train_status`, `tts_id`, `demo_video_make_status`, `demo_text`, `demo_cover_url`, `demo_video_url`, `type`, `free_type`, `animal_url`, `silent_video_url`," +
                    " `ext`, `del_flag`, `create_time`, `update_time`, `remark`, `demo_duration`) VALUES " +
                    "( -1, '"+name+"', NULL, NULL, '"+sex+"', '"+label+"', '"+country+"', '"+attitude+"', '"+age+"', '"+screenType+"', '"+coverUrl+"', '"+videoUrl+"', '"+duration+"', " +
                    "'" + realVideoSize.getHeight() + "', '" + realVideoSize.getWidth() + "', " +
                    "20, (SELECT id FROM user_video_tts WHERE speaker = '"+tts_match+"' and type = 2 limit 1), '20', '"+demo_text+"', '"+coverDemoUrl+"', '"+videoDemoUrl+"', 2, 1, "+animal_url+", '"+videoUrl+"', '{\"demo_text\":\"" + demoText + "\",\"tts_match\":\"" + tts_match + "\"}', 0, " +
                    "'" + format + "', '" + format + "', NULL, '"+demoDuration+"');\n";
            printWriter.write(sql);
            String sql2 = "INSERT IGNORE INTO `ffo-toc`.`user_video_classify_relation` (`classify_id`, `bind_id`) " +
                    "SELECT * from ( SELECT id as classify_id FROM `ffo-toc`.`user_video_classify` WHERE name = '"+classifyName+"' and type = 1) t1, ( SELECT id as bind_id FROM `ffo-toc`.`user_video_robot` WHERE robot_name = '"+name+"' and type = 2) t2;\n";

            printWriter.write(sql2);

            String recommend =  StringUtils.toStr(objectMap.get("是否推荐")).trim();
            if (recommend.contains("是")) {
                String sql3 = "INSERT IGNORE INTO `ffo-toc`.`user_video_classify_relation` (`classify_id`, `bind_id`) " +
                        "SELECT * from ( SELECT id as classify_id FROM `ffo-toc`.`user_video_classify` WHERE name = '推荐' and type = 1) t1, ( SELECT id as bind_id FROM `ffo-toc`.`user_video_robot` WHERE robot_name = '"+name+"' and type = 2) t2;\n";
                printWriter.write(sql3);
            }
            printWriter.write("\r\n");

            printWriter.flush();
            currentTimeMillis = currentTimeMillis - (5 * 60 * 1000);
        }
        printWriter.flush();
        printWriter.close();
        System.out.println("解析完成");
    }

    @Test
    public void 设置推荐分类() throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\Guiji.cn-待上架模板视频\\Guiji.cn公共模特+声音_202409.xlsx"));
        List<Map<String, Object>> dataList = ExcelUtil.importData(workbook.getSheet("初筛选-待确认"), 1);
        PrintWriter printWriter = FileUtil.getPrintWriter("D:\\Guiji.cn-待上架模板视频\\Guiji.cn公共模特+声音_202409_exportRecommend.sql", StandardCharsets.UTF_8, false);
        printWriter.write("--  数据\r\n");
        printWriter.flush();
        long currentTimeMillis = System.currentTimeMillis();
        for (Map<String, Object> objectMap : dataList) {
            String format = DateUtil.format(new Date(currentTimeMillis), "yyyy-MM-dd HH:mm:ss");
            String name =  StringUtils.toStr(objectMap.get("模特名称")).trim();
            String tts_match =  StringUtils.toStr(objectMap.get("配音")).trim();
            String recommend =  StringUtils.toStr(objectMap.get("是否推荐")).trim();
            if (StringUtils.isBlank(name)) {
                continue;
            }
            if (recommend.contains("是")) {
                //1是形象
                String sql1 = "INSERT IGNORE INTO `ffo-toc`.`user_video_classify_relation` (`classify_id`, `bind_id`) " +
                        "SELECT * from ( SELECT id as classify_id FROM `ffo-toc`.`user_video_classify` WHERE name = '推荐' and type = 1) t1, ( SELECT id as bind_id FROM `ffo-toc`.`user_video_robot` WHERE robot_name = '"+name+"' and type = 2) t2;\n";

                //2是音色
                String sql2 = "INSERT IGNORE INTO `ffo-toc`.`user_video_classify_relation` (`classify_id`, `bind_id`) " +
                        "SELECT * from ( SELECT id as classify_id FROM `ffo-toc`.`user_video_classify` WHERE name = '推荐' and type = 2) t1, ( SELECT id as bind_id FROM `ffo-toc`.`user_video_tts` WHERE speaker = '"+tts_match+"' and type = 2) t2;\n";

                printWriter.write(sql1);
                printWriter.write(sql2);
                printWriter.write("\r\n");
                printWriter.flush();
            }
            currentTimeMillis = currentTimeMillis - (5 * 60 * 1000);
        }
        printWriter.flush();
        printWriter.close();
        System.out.println("解析完成");
    }

    /**
     * 示例文案
     */
    @Test
    public void 示例文案_历史数据() throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\Guiji.cn-待上架模板视频\\Guiji.cn公共模特+声音_202409.xlsx"));
        List<Map<String, Object>> dataList = ExcelUtil.importData(workbook.getSheet("公模-初筛选-待确认"), 1);
        PrintWriter printWriter = FileUtil.getPrintWriter("D:\\Guiji.cn-待上架模板视频\\Guiji.cn公共模特+声音_202409_exportDemoText.sql", StandardCharsets.UTF_8, false);
        printWriter.write("--  数据\r\n");
        printWriter.flush();
        long currentTimeMillis = System.currentTimeMillis();
        for (Map<String, Object> objectMap : dataList) {
            String format = DateUtil.format(new Date(currentTimeMillis), "yyyy-MM-dd HH:mm:ss");
            String name =  StringUtils.toStr(objectMap.get("模特名称")).trim();
            String tts_match =  StringUtils.toStr(objectMap.get("配音")).trim();
            String recommend =  StringUtils.toStr(objectMap.get("是否推荐")).trim();
            String demoText = StringUtils.toStr(objectMap.get("首页介绍话术")).trim().replaceAll("'","\\\\'");
            if (StringUtils.isBlank(name)) {
                continue;
            }
            String sql1 = "UPDATE user_video_robot set demo_text = '"+demoText+"' where robot_name = '"+name+"' and type = 2 and demo_text is null;";
            printWriter.write(sql1);
            printWriter.write("\r\n");
            printWriter.flush();
            currentTimeMillis = currentTimeMillis - (5 * 60 * 1000);
        }
        printWriter.flush();
        printWriter.close();
        System.out.println("解析完成");
    }

    @Test
    public void run跑_公共形象示例视频() throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\Guiji.cn-待上架模板视频\\Guiji.cn公共模特+声音_202409.xlsx"));
        List<Map<String, Object>> dataListTts = ExcelUtil.importData(workbook.getSheet("新增-公共音色"), 1);
        Map<String, String> ttsMap = new HashMap<>();
        for (Map<String, Object> objectMap : dataListTts) {
            String speaker_id =StringUtils.toStr(objectMap.get("音色ID")).trim();
            String tts_name =StringUtils.toStr(objectMap.get("上线名称")).trim();
            ttsMap.put(tts_name.trim(), speaker_id);
        }

        List<Map<String, Object>> dataList = ExcelUtil.importData(workbook.getSheet("公模-初筛选-待确认"), 1);
        Map<String, String[]> introMap = new HashMap<>();

        for (Map<String, Object> objectMap : dataList) {
            String name =  StringUtils.toStr(objectMap.get("模特名称")).trim();
            String tts_name =  StringUtils.toStr(objectMap.get("配音")).trim();
            String intro =  StringUtils.toStr(objectMap.get("首页介绍话术")).trim();
            if (StringUtils.isBlank(name)) {
                continue;
            }
            introMap.put(name, new String[]{ttsMap.get(tts_name), intro});
        }

        //AtomicBoolean hasError = new AtomicBoolean(false);
        //introMap.forEach((k,v)->{
        //    String tts = v[0];
        //    String intro = v[1];
        //    if (StringUtils.isBlank(tts) || StringUtils.isBlank(intro)) {
        //        log.error("校验失败" + "[" + k + "("+tts+")]: " + intro);
        //        hasError.set(true);
        //    }
        //});
        //if (hasError.get()) {
        //    return;
        //}

        List<File> fileList = FileUtil.listFiles(new File("D:\\Guiji.cn-待上架模板视频\\guiji.cn-1017\\"));
        //File mp4Path = new File("D:\\Guiji.cn-待上架模板视频\\营销达人\\Layla.mp4");
        for (File mp4Path : fileList) {
            if (mp4Path.getAbsolutePath().contains("_demo.mp4")) {
                continue;
            }
            File mp4PathDemo = new File(mp4Path.getAbsolutePath().replaceAll(".mp4", "_demo.mp4"));
            if (mp4PathDemo.exists()) {
                continue;
            }
            String name = FileUtil.getBaseName(mp4Path.getAbsolutePath());
            String[] v = introMap.get(name);
            if (v == null) {
                continue;
            }
            String tts = v[0];
            String intro = v[1];
            if (StringUtils.isBlank(tts) || StringUtils.isBlank(intro)) {
                log.warn( name + "[" + tts + "]:" + intro);
                continue;
            }
            System.out.println("[" + name + "("+tts+")]: " + intro);
            String video = "https://digital-public-toc.obs.cn-east-3.myhuaweicloud.com/resources/guiji.cn-1017/"+name+".mp4";

            String res = HttpUtil.createPost("https://zh.api.guiji.cn/avatar2c/tool/sec_tts")
                    //.header("token", "754d6f8d032f401a85aa7914c679e462")
                    .body("{\n" +
                            "  \"text\":\""+intro+"\",\n" +
                            "  \"speaker_id\":\""+tts+"\"\n" +
                            "}").execute().body();
            JSONObject jsonObject = JSON.parseObject(res);
            String audioUrl = jsonObject.getString("data");
            System.out.println("音频地址：" + audioUrl);

            if (StringUtils.isBlank(audioUrl)) {
                log.error("音频合成失败！" + "[" + name + "("+tts+")]: " + intro);
                continue;
            }

            JSONObject param = new JSONObject();
            param.put("customerId", "0");
            param.put("audioUrl", audioUrl);
            param.put("videoUrl", video);
            param.put("captureVideoFrame", 1);
            param.put("hasWater", 0);
            param.put("digitalAuth", 0);
            param.put("chaofen", 0);
            param.put("groupName", "");
            String resV2 = HttpUtil.createPost("https://zh.api.guiji.cn/vpp-toc/vShow/3/vShowFace2FaceSynthProduct").body(param.toJSONString()).execute().body();
            System.out.println(resV2);
            JSONObject jsonObjectV2 = JSON.parseObject(resV2);
            String workCode = jsonObjectV2.getString("data");
            while (true) {
                WorkUtil.trySleep5000();
                String body = HttpUtil.createGet("https://zh.api.guiji.cn/vpp-toc/sys/queryWorkOrder?workCode=" + workCode).execute().body();
                JSONObject jsonObject1 = JSON.parseObject(body);
                JSONObject data = jsonObject1.getJSONObject("data");
                String coverUrl = data.getString("videoFrameImageUrl");
                String videoUrl = data.getString("videoUrl");
                if (StringUtils.isNotBlank(videoUrl)) {
                    System.out.println("合成成功 ===" + videoUrl + " | " + coverUrl);
                    HttpUtil.downloadFile(videoUrl, mp4PathDemo);
                    System.out.println("下载完成");
                    break;
                }
            }
        }
    }

    @Test
    public void test12345() throws Exception{
        String intro = "吾乃关羽，字云长。吾誓死追随吾兄刘备，共谋天下大业。";
        String tts = "491553792073138176";
        String res = HttpUtil.createPost("https://zh.api.guiji.cn/avatar2c/tool/sec_tts")
                .header("token", "754d6f8d032f401a85aa7914c679e462")
                .body("{\n" +
                        "  \"text\":\""+intro+"\",\n" +
                        "  \"speaker_id\":\""+tts+"\"\n" +
                        "}").execute().body();
        JSONObject jsonObject = JSON.parseObject(res);
        String audioUrl = jsonObject.getString("data");
        System.out.println("音频地址：" + audioUrl);

        if (StringUtils.isBlank(audioUrl)) {
            log.error("音频合成失败！");
            return;
        }
        String video = "https://digital-public-toc.obs.cn-east-3.myhuaweicloud.com/resources/v1/历史名人/关羽.mp4";
        JSONObject param = new JSONObject();
        param.put("customerId", "0");
        param.put("audioUrl", audioUrl);
        param.put("videoUrl", video);
        param.put("captureVideoFrame", 1);
        param.put("hasWater", 0);
        param.put("digitalAuth", 0);
        param.put("chaofen", 0);
        param.put("groupName", "");
        String resV2 = HttpUtil.createPost("https://zh.api.guiji.cn/vpp-toc/vShow/3/vShowFace2FaceSynthProduct").body(param.toJSONString()).execute().body();
        System.out.println(resV2);
        JSONObject jsonObjectV2 = JSON.parseObject(resV2);
        String workCode = jsonObjectV2.getString("data");
        String sourceImgUrl = "https://digital-public-toc.obs.cn-east-3.myhuaweicloud.com:443/anylang-video/2024/08/23/1826891109512433664.png";
        String driverVideoUrl = "";
        while (true) {
            WorkUtil.trySleep5000();
            String body = HttpUtil.createGet("https://zh.api.guiji.cn/vpp-toc/sys/queryWorkOrder?workCode=" + workCode).execute().body();
            JSONObject jsonObject1 = JSON.parseObject(body);
            JSONObject data = jsonObject1.getJSONObject("data");
            String coverUrl = data.getString("videoFrameImageUrl");
            String videoUrl = data.getString("videoUrl");
            if (StringUtils.isNotBlank(videoUrl)) {
                System.out.println("合成成功 ===" + videoUrl + " | " + coverUrl);
                driverVideoUrl = videoUrl;
                //HttpUtil.downloadFile(videoUrl, mp4PathDemo);
                //System.out.println("下载完成");
                break;
            }
        }


        JSONObject param2 = new JSONObject();
        param2.put("sourceImgUrl", sourceImgUrl);
        param2.put("driverVideo", driverVideoUrl);
        param2.put("customerId", "manager-100");
        param2.put("mp4Gif", 1);
        param2.put("captureVideoFrame", "1");




    }

    @Test
    public void test435(){
        String intro = "汪汪~大家好，我是豆豆，一只超级可爱的小白狗！我的毛发柔软得像棉花糖，眼睛圆溜溜的，总是充满好奇地四处张望。每次看到主人回家，我都会兴奋地摇起尾巴，蹦蹦跳跳地迎上去，想要得到一个大大的拥抱。除了和主人亲昵，我还特别喜欢在公园里和小伙伴们一起玩耍，追逐着彼此，享受着无忧无虑的快乐时光。希望以后能认识更多的小伙伴，一起分享生活的美好！";
        String tts = "91";
        String name = "豆豆";
        String video = "https://digital-public-toc.obs.cn-east-3.myhuaweicloud.com/resources/v3/"+name+".mp4";
        String sourceImg = "https://digital-public-toc.obs.cn-east-3.myhuaweicloud.com/resources/v3/"+name+"_cover.png";
        //下载地址
        File demo_a = new File("C:\\Users\\1\\Downloads\\" + UrlUtil.getBaseName(video) + "_demo_a.wav");
        File demo_v = new File("C:\\Users\\1\\Downloads\\" + UrlUtil.getBaseName(video) + "_demo_v." + UrlUtil.getExtension(video));
        File demo_res = new File("C:\\Users\\1\\Downloads\\" + UrlUtil.getBaseName(video) + "_demo." + UrlUtil.getExtension(video));
        String res = HttpUtil.createPost("https://zh.api.guiji.cn/avatar2c/tool/sec_tts")
                //.header("token", "754d6f8d032f401a85aa7914c679e462")
                .body("{\n" +
                        "  \"text\":\""+intro+"\",\n" +
                        "  \"speaker_id\":\""+tts+"\"\n" +
                        "}").execute().body();
        JSONObject jsonObject = JSON.parseObject(res);
        String audioUrl = jsonObject.getString("data");
        System.out.println("音频地址：" + audioUrl);
        if (StringUtils.isBlank(audioUrl)) {
            log.error("音频合成失败！");
            return;
        }
        HttpUtil.downloadFile(audioUrl, demo_a);
        System.out.println("下载完成");
        JSONObject param = new JSONObject();
        param.put("customerId", "0");
        param.put("audioUrl", audioUrl);
        param.put("videoUrl", video);
        param.put("captureVideoFrame", 1);
        param.put("hasWater", 0);
        param.put("digitalAuth", 0);
        param.put("chaofen", 0);
        param.put("groupName", "");
        String resV2 = HttpUtil.createPost("https://zh.api.guiji.cn/vpp-toc/vShow/3/vShowFace2FaceSynthProduct").body(param.toJSONString()).execute().body();
        System.out.println(resV2);
        JSONObject jsonObjectV2 = JSON.parseObject(resV2);
        String workCode = jsonObjectV2.getString("data");
        String driverVideoUrl = null;
        while (true) {
            WorkUtil.trySleep5000();
            String body = HttpUtil.createGet("https://zh.api.guiji.cn/vpp-toc/sys/queryWorkOrder?workCode=" + workCode).execute().body();
            JSONObject jsonObject1 = JSON.parseObject(body);
            JSONObject data = jsonObject1.getJSONObject("data");
            String coverUrl = data.getString("videoFrameImageUrl");
            String videoUrl = data.getString("videoUrl");
            if (StringUtils.isNotBlank(videoUrl)) {
                System.out.println("Face2Face 合成成功 ===" + videoUrl + " | " + coverUrl);
                driverVideoUrl = videoUrl;
                HttpUtil.downloadFile(videoUrl, demo_v);
                System.out.println("下载完成");
                break;
            }
        }


        JSONObject param2 = new JSONObject();
        param2.put("sourceImg", sourceImg);
        param2.put("driverVideo", driverVideoUrl);
        param2.put("customerId", "100");
        param2.put("groupName", "vshow");
        param2.put("mp4Gif", 0);
        param2.put("captureVideoFrame", "1");
        String resV3 = HttpUtil.createPost("https://guike.guiji.cn/vpp/vShow/3/vShowLivePortraitProduct").body(param2.toJSONString()).execute().body();
        System.out.println(resV3);
        JSONObject jsonObjectV3 = JSON.parseObject(resV3);
        String workCode2 = jsonObjectV3.getString("data");
        while (true) {
            WorkUtil.trySleep5000();
            String body = HttpUtil.createGet("https://guike.guiji.cn/vpp/sys/queryWorkOrder?workCode=" + workCode2).execute().body();
            JSONObject jsonObject1 = JSON.parseObject(body);
            JSONObject data = jsonObject1.getJSONObject("data");
            String coverUrl = data.getString("videoFrameImageUrl");
            String videoUrl = data.getString("videoUrl");
            if (StringUtils.isNotBlank(videoUrl)) {
                System.out.println("LivePortrait 合成成功 ===" + body);
                System.out.println("LivePortrait 合成成功 ===" + videoUrl + " | " + coverUrl);
                HttpUtil.downloadFile(videoUrl, demo_res);
                System.out.println("下载完成");
                break;
            }
        }
    }

    @Test
    public void test动物IP示例视频合成() throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\Guiji.cn-待上架模板视频\\Guiji.cn公共模特+声音_202409.xlsx"));
        List<Map<String, Object>> dataListTts = ExcelUtil.importData(workbook.getSheet("新增-公共音色"), 1);
        Map<String, String> ttsMap = new HashMap<>();
        for (Map<String, Object> objectMap : dataListTts) {
            String speaker_id =StringUtils.toStr(objectMap.get("音色ID")).trim();
            String tts_name =StringUtils.toStr(objectMap.get("上线名称")).trim();
            ttsMap.put(tts_name.trim(), speaker_id);
        }

        List<Map<String, Object>> dataList = ExcelUtil.importData(workbook.getSheet("动物IP"), 1);
        Map<String, String[]> introMap = new HashMap<>();

        for (Map<String, Object> objectMap : dataList) {
            String name =  StringUtils.toStr(objectMap.get("模特名称")).trim();
            String tts_name =  StringUtils.toStr(objectMap.get("配音")).trim();
            String intro =  StringUtils.toStr(objectMap.get("首页介绍话术")).trim();
            if (StringUtils.isBlank(name)) {
                continue;
            }
            introMap.put(name, new String[]{ttsMap.get(tts_name), intro});
        }
        List<File> fileList = FileUtil.listFiles(new File("D:\\Guiji.cn-待上架模板视频\\animal-1008\\"));
        //File mp4Path = new File("D:\\Guiji.cn-待上架模板视频\\营销达人\\Layla.mp4");
        for (File mp4Path : fileList) {
            if (mp4Path.getAbsolutePath().contains("_demo.mp4")) {
                continue;
            }
            File mp4PathDemo = new File(mp4Path.getAbsolutePath().replaceAll(".mp4", "_demo.mp4"));
            if (mp4PathDemo.exists()) {
                continue;
            }
            String name = FileUtil.getBaseName(mp4Path.getAbsolutePath());
            String[] v = introMap.get(name);
            if (v == null) {
                continue;
            }
            String tts = v[0];
            String intro = v[1];
            if (StringUtils.isBlank(tts) || StringUtils.isBlank(intro)) {
                log.warn(name + "[" + tts + "]:" + intro);
                continue;
            }
            System.out.println("[" + name + "(" + tts + ")]: " + intro);

            //String intro = "汪汪~大家好，我是豆豆，一只超级可爱的小白狗！我的毛发柔软得像棉花糖，眼睛圆溜溜的，总是充满好奇地四处张望。每次看到主人回家，我都会兴奋地摇起尾巴，蹦蹦跳跳地迎上去，想要得到一个大大的拥抱。除了和主人亲昵，我还特别喜欢在公园里和小伙伴们一起玩耍，追逐着彼此，享受着无忧无虑的快乐时光。希望以后能认识更多的小伙伴，一起分享生活的美好！";
            //String tts = "91";
            //String name = "豆豆";
            String path = mp4Path.getParentFile().getPath();

            String video = "https://digital-public-toc.obs.cn-east-3.myhuaweicloud.com/resources/animal-1008/"+name+".mp4";
            String sourceImg = "https://digital-public-toc.obs.cn-east-3.myhuaweicloud.com/resources/animal-1008/"+name+"_cover.jpg";
            //下载地址C:\Users\1\Videos\逍遥安卓视频
            //File demo_a = new File("C:\\Users\\1\\Videos\\" + UrlUtil.getBaseName(video) + "_demo_a.wav");
            File demo_v = new File("C:\\Users\\1\\Videos\\" + UrlUtil.getBaseName(video) + "_demo_v." + UrlUtil.getExtension(video));
            File demo_res = new File("C:\\Users\\1\\Videos\\" + UrlUtil.getBaseName(video) + "_demo." + UrlUtil.getExtension(video));
            intro = intro.replaceAll("\"", "");
            String res = HttpUtil.createPost("https://zh.api.guiji.cn/avatar2c/tool/sec_tts")
                    //.header("token", "754d6f8d032f401a85aa7914c679e462")
                    .body("{\n" +
                            "  \"text\":\""+intro+"\",\n" +
                            "  \"speaker_id\":\""+tts+"\"\n" +
                            "}").execute().body();
            JSONObject jsonObject = JSON.parseObject(res);
            String audioUrl = jsonObject.getString("data");
            System.out.println("音频地址：" + audioUrl);
            if (StringUtils.isBlank(audioUrl)) {
                log.error("音频合成失败！");
                continue;
            }
            //HttpUtil.downloadFile(audioUrl, demo_a);
            System.out.println("下载完成");
            JSONObject param = new JSONObject();
            param.put("customerId", "0");
            param.put("audioUrl", audioUrl);
            param.put("videoUrl", video);
            param.put("captureVideoFrame", 1);
            param.put("hasWater", 0);
            param.put("digitalAuth", 0);
            param.put("chaofen", 0);
            param.put("groupName", "");
            String resV2 = HttpUtil.createPost("https://zh.api.guiji.cn/vpp-toc/vShow/3/vShowFace2FaceSynthProduct").body(param.toJSONString()).execute().body();
            System.out.println(resV2);
            JSONObject jsonObjectV2 = JSON.parseObject(resV2);
            String workCode = jsonObjectV2.getString("data");
            String driverVideoUrl = null;
            while (true) {
                WorkUtil.trySleep5000();
                String body = HttpUtil.createGet("https://zh.api.guiji.cn/vpp-toc/sys/queryWorkOrder?workCode=" + workCode).execute().body();
                JSONObject jsonObject1 = JSON.parseObject(body);
                JSONObject data = jsonObject1.getJSONObject("data");
                String coverUrl = data.getString("videoFrameImageUrl");
                String videoUrl = data.getString("videoUrl");
                if (StringUtils.isNotBlank(videoUrl)) {
                    System.out.println("Face2Face 合成成功 ===" + videoUrl + " | " + coverUrl);
                    driverVideoUrl = videoUrl;
                    HttpUtil.downloadFile(videoUrl, demo_v);
                    System.out.println("下载完成");
                    break;
                }
            }


            JSONObject param2 = new JSONObject();
            param2.put("sourceImg", sourceImg);
            param2.put("driverVideo", driverVideoUrl);
            param2.put("customerId", "100");
            param2.put("groupName", "test");
            param2.put("mp4Gif", 0);
            param2.put("flag_stitching", "true");//拼接全身，传true。 不传或者false，则只有大头。
            param2.put("captureVideoFrame", "1");
            String url = "https://prevshow.guiji.ai"; //param2.put("groupName", "test");
            //String url = "https://guike.guiji.cn";  //param2.put("groupName", "vshow");
            String resV3 = HttpUtil.createPost(url + "/vpp/vShow/3/vShowLivePortraitProduct").body(param2.toJSONString()).execute().body();
            System.out.println(resV3);
            JSONObject jsonObjectV3 = JSON.parseObject(resV3);
            String workCode2 = jsonObjectV3.getString("data");
            while (true) {
                WorkUtil.trySleep5000();
                String body = HttpUtil.createGet(url + "/vpp/sys/queryWorkOrder?workCode=" + workCode2).execute().body();
                JSONObject jsonObject1 = JSON.parseObject(body);
                JSONObject data = jsonObject1.getJSONObject("data");
                String coverUrl = data.getString("videoFrameImageUrl");
                String videoUrl = data.getString("videoUrl");
                if (StringUtils.isNotBlank(videoUrl)) {
                    System.out.println("LivePortrait 合成成功 ===" + body);
                    System.out.println("LivePortrait 合成成功 ===" + videoUrl + " | " + coverUrl);
                    HttpUtil.downloadFile(videoUrl, demo_res);
                    System.out.println("下载完成");
                    break;
                }
            }
        }


    }


    @Test
    public void test3435(){
        String sourceImg = "https://digital-public-toc.obs.cn-east-3.myhuaweicloud.com/resources/v2/%E8%B1%86%E8%B1%86_cover.png";
        String driverVideoUrl = "https://digital-public-toc.obs.cn-east-3.myhuaweicloud.com/vpp/vpp/2024/09/24/1155812570637148160_ff_1.mp4";
        JSONObject param2 = new JSONObject();
        param2.put("sourceImg", sourceImg);
        param2.put("driverVideo", driverVideoUrl);
        param2.put("customerId", "100");
        param2.put("groupName", "test");
        param2.put("mp4Gif", 1);
        param2.put("captureVideoFrame", "1");
        String resV3 = HttpUtil.createPost("https://prevshow.guiji.ai/vpp/vShow/3/vShowLivePortraitProduct").body(param2.toJSONString()).execute().body();
        System.out.println(resV3);
        JSONObject jsonObjectV3 = JSON.parseObject(resV3);
        String workCode2 = jsonObjectV3.getString("data");
        while (true) {
            WorkUtil.trySleep5000();
            String body = HttpUtil.createGet("https://prevshow.guiji.ai/vpp/sys/queryWorkOrder?workCode=" + workCode2).execute().body();
            JSONObject jsonObject1 = JSON.parseObject(body);
            JSONObject data = jsonObject1.getJSONObject("data");
            String coverUrl = data.getString("videoFrameImageUrl");
            String videoUrl = data.getString("videoUrl");
            if (StringUtils.isNotBlank(videoUrl)) {
                System.out.println("LivePortrait 合成成功 ===" + body);
                System.out.println("LivePortrait 合成成功 ===" + videoUrl + " | " + coverUrl);
                System.out.println("下载完成");
                break;
            }
        }
    }



    @Test
    public void test已有数据() throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\Guiji.cn-待上架模板视频\\已有形象.xlsx"));
        List<Map<String, Object>> dataListTts = ExcelUtil.importData(workbook.getSheet("Sheet1"), 0);
        for (Map<String, Object> objectMap : dataListTts) {
            String id = StringUtils.toStr(objectMap.get("id")).trim();
            String robot_name =StringUtils.toStr(objectMap.get("robot_name")).trim();
            String silent_video_url =StringUtils.toStr(objectMap.get("silent_video_url")).trim();
            String demo_text = StringUtils.toStr(objectMap.get("demo_text")).trim();
            String speaker_id = StringUtils.toStr(objectMap.get("配音ID")).trim();
            //下载地址
            String name = robot_name + "_" + id;
            String res = HttpUtil.createPost("https://zh.api.guiji.cn/avatar2c/tool/sec_tts")
                    //.header("token", "754d6f8d032f401a85aa7914c679e462")
                    .body("{\n" +
                            "  \"text\":\""+demo_text+"\",\n" +
                            "  \"speaker_id\":\""+speaker_id+"\"\n" +
                            "}").execute().body();
            JSONObject jsonObject = JSON.parseObject(res);
            String audioUrl = jsonObject.getString("data");
            System.out.println("音频地址：" + audioUrl);
            if (StringUtils.isBlank(audioUrl)) {
                log.error("音频合成失败！");
                log.error(speaker_id + ":" + demo_text);
                continue;
            }

            JSONObject param = new JSONObject();
            param.put("customerId", "0");
            param.put("audioUrl", audioUrl);
            param.put("videoUrl", silent_video_url);
            param.put("captureVideoFrame", 1);
            param.put("hasWater", 0);
            param.put("digitalAuth", 0);
            param.put("chaofen", 0);
            param.put("groupName", "");
            String resV2 = HttpUtil.createPost("https://zh.api.guiji.cn/vpp-toc/vShow/3/vShowFace2FaceSynthProduct").body(param.toJSONString()).execute().body();
            System.out.println(resV2);
            JSONObject jsonObjectV2 = JSON.parseObject(resV2);
            String workCode = jsonObjectV2.getString("data");
            while (true) {
                WorkUtil.trySleep5000();
                String body = HttpUtil.createGet("https://zh.api.guiji.cn/vpp-toc/sys/queryWorkOrder?workCode=" + workCode).execute().body();
                JSONObject jsonObject1 = JSON.parseObject(body);
                JSONObject data = jsonObject1.getJSONObject("data");
                String coverUrl = data.getString("videoFrameImageUrl");
                String videoUrl = data.getString("videoUrl");
                if (StringUtils.isNotBlank(videoUrl)) {
                    System.out.println("Face2Face 合成成功 ===" + videoUrl + " | " + coverUrl);
                    File demo_res = new File("C:\\Users\\1\\Downloads\\" + name + "_demo.mp4");
                    HttpUtil.downloadFile(videoUrl, demo_res);
                    System.out.println("下载完成");
                    break;
                }
            }
        }


    }




    @Test
    public void 更新形象标签() throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\Guiji.cn-待上架模板视频\\Guiji.cn公共模特+声音_202409.xlsx"));
        List<Map<String, Object>> dataListTts = ExcelUtil.importData(workbook.getSheet("新增-公共音色"), 1);
        Map<String, String> ttsMap = new HashMap<>();
        for (Map<String, Object> objectMap : dataListTts) {
            String speaker_id =StringUtils.toStr(objectMap.get("音色ID")).trim();
            String tts_name =StringUtils.toStr(objectMap.get("上线名称")).trim();
            ttsMap.put(tts_name.trim(), speaker_id);
        }

        List<Map<String, Object>> dataList = ExcelUtil.importData(workbook.getSheet("公模-初筛选-待确认"), 1);
        Map<String, String[]> introMap = new HashMap<>();
        PrintWriter printWriter = FileUtil.getPrintWriter("D:\\Guiji.cn-待上架模板视频\\Guiji.cn公共模特+声音_202409_更新标签2.sql", StandardCharsets.UTF_8, false);

        for (Map<String, Object> objectMap : dataList) {
            String name =  StringUtils.toStr(objectMap.get("模特名称")).trim();
            String label =  StringUtils.toStr(objectMap.get("标签")).trim();
            String tts_name =  StringUtils.toStr(objectMap.get("配音")).trim();
            String intro =  StringUtils.toStr(objectMap.get("首页介绍话术")).trim();
            String resolution =  StringUtils.toStr(objectMap.get("比例")).trim();
            if (StringUtils.isBlank(name)) {
                continue;
            }
            String sql_and = ";";
            if ("16：9".equals(resolution)) {
                sql_and = "and horizontal > vertical;";
            }
            if ("9：16".equals(resolution)) {
                sql_and = "and vertical > horizontal;";
            }
            introMap.put(name, new String[]{ttsMap.get(tts_name), intro});

            String sql = "update user_video_robot set label = '"+label+"' where robot_name = '"+name+"' and type = 2 " + sql_and;
            printWriter.write(sql);
            printWriter.write("\r\n");
        }

        printWriter.flush();
        printWriter.close();
        System.out.println("解析完成");

    }



}
