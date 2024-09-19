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
        MediaUtil.instance().extractCoverDir(new File("C:\\Users\\1\\Downloads\\cn上架外模\\cn上架外模"));
    }

    @Test
    public void ttsSQLGenerate() throws Exception{
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
            if (StringUtils.isBlank(speaker_id)) {
                continue;
            }
            String lang = objectMap.get("语种简称").toString();
            String langZh = objectMap.get("语种").toString();
            String name = objectMap.get("上线名称").toString();
            String audioUrl = objectMap.get("音频路径").toString();
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

            String sql2 = "INSERT INTO `ffo-toc`.`user_video_classify_relation` (`classify_id`, `bind_id`) " +
                    "SELECT * from ( SELECT id as classify_id FROM `ffo-toc`.`user_video_classify` WHERE name = '"+classifyName+"' and type = 2) t1, ( SELECT id as bind_id FROM `ffo-toc`.`user_video_tts` WHERE speaker = '"+name+"' and type = 2) t2;\n";

            printWriter.write(sql2);
            printWriter.write("\r\n");
            currentTimeMillis = currentTimeMillis - (5 * 60 * 1000);
        }
        printWriter.flush();
        printWriter.close();
        System.out.println("解析完成");
    }

    @Test
    public void robotSQLGenerate() throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\Guiji.cn-待上架模板视频\\Guiji.cn公共模特+声音_202409.xlsx"));
        List<Map<String, Object>> dataList = ExcelUtil.importData(workbook.getSheet("初筛选-待确认"), 1);
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
            String name =  StringUtils.toStr(objectMap.get("模特名称"));
            if (StringUtils.isBlank(name)) {
                continue;
            }
            String age = objectMap.get("年龄").toString().replaceAll("岁", "");
            String classifyName = objectMap.get("所属分类").toString();
            String countryStr = objectMap.get("中模/外模").toString();
            String country = (countryStr.contains("中")?"中国":"外国");
            String sexStr = objectMap.get("性别").toString();
            String label = objectMap.get("标签").toString();
            String demoText = objectMap.get("首页介绍话术").toString();
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

            String videoUrl = "https://gy.cdn.guiji.cn/resources/v1/"+classifyName+"/" + name + ".mp4";
            if (!cn.acyou.leo.framework.util.HttpUtil.reachable(videoUrl)) {
                log.error("地址不可达：" + videoUrl);
                continue;
            }
            String coverUrl = videoUrl.substring(0, videoUrl.lastIndexOf(".")) + "_cover.png";
            String videoDemoUrl = "https://gy.cdn.guiji.cn/resources/v1/"+classifyName+"/" + name + "_demo.mp4";
            String coverDemoUrl = videoUrl.substring(0, videoUrl.lastIndexOf(".")) + "_demo_cover.png";
            MultimediaInfo mediaInfo = MediaUtil.instance().getMediaInfo(videoUrl);
            VideoSize realVideoSize = MediaUtil.getRealVideoSize(mediaInfo);


            String sql = "INSERT INTO `ffo-toc`.`user_video_robot` (`user_id`, `robot_name`, `robot_code`, `scene_code`, `sex`, `label`, `country`, `attitude`, `age`, `screen_type`, `cover_url`," +
                    " `video_url`, `duration`, `vertical`, `horizontal`, `train_status`, `tts_id`, `demo_video_make_status`, `demo_cover_url`, `demo_video_url`, `type`, `free_type`, `silent_video_url`," +
                    " `ext`, `del_flag`, `create_time`, `update_time`, `remark`) VALUES " +
                    "( -1, '"+name+"', NULL, NULL, '"+sex+"', '"+label+"', '"+country+"', '"+attitude+"', '"+age+"', '"+screenType+"', '"+coverUrl+"', '"+videoUrl+"', '"+mediaInfo.getDuration()+"', " +
                    "'" + realVideoSize.getHeight() + "', '" + realVideoSize.getWidth() + "', " +
                    "20, NULL, '20', '"+coverDemoUrl+"', '"+videoDemoUrl+"', 2, 1, '"+videoUrl+"', '{\"demo_text\":\"" + demoText + "\",\"tts_match\":\"" + tts_match + "\"}', 0, '" + format + "', '" + format + "', NULL);\n";
            printWriter.write(sql);
            String sql2 = "INSERT INTO `ffo-toc`.`user_video_classify_relation` (`classify_id`, `bind_id`) " +
                    "SELECT * from ( SELECT id as classify_id FROM `ffo-toc`.`user_video_classify` WHERE name = '"+classifyName+"' and type = 1) t1, ( SELECT id as bind_id FROM `ffo-toc`.`user_video_robot` WHERE robot_name = '"+name+"' and type = 2) t2;\n";

            printWriter.write(sql2);
            printWriter.write("\r\n");
            printWriter.flush();
            currentTimeMillis = currentTimeMillis - (5 * 60 * 1000);
        }
        printWriter.flush();
        printWriter.close();
        System.out.println("解析完成");
    }

    @Test
    public void test1234() throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\Guiji.cn-待上架模板视频\\Guiji.cn公共模特+声音_202409.xlsx"));
        List<Map<String, Object>> dataListTts = ExcelUtil.importData(workbook.getSheet("新增-公共音色"), 1);
        Map<String, String> ttsMap = new HashMap<>();
        for (Map<String, Object> objectMap : dataListTts) {
            String speaker_id =StringUtils.toStr(objectMap.get("音色ID")).trim();
            String tts_name =StringUtils.toStr(objectMap.get("上线名称")).trim();
            ttsMap.put(tts_name.trim(), speaker_id);
        }

        List<Map<String, Object>> dataList = ExcelUtil.importData(workbook.getSheet("初筛选-待确认"), 1);
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

        List<File> fileList = FileUtil.listFiles(new File("D:\\Guiji.cn-待上架模板视频\\历史名人\\"));
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
            String video = "https://gy.cdn.guiji.cn/resources/v1/历史名人/"+name+".mp4";

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
}
