package cn.acyou.leo.tool.test.poi;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.acyou.leo.framework.media.encoder.MediaUtil;
import cn.acyou.leo.framework.util.ExcelUtil;
import cn.acyou.leo.framework.util.FileUtil;
import cn.acyou.leo.framework.util.StringUtils;
import cn.hutool.core.date.DateUtil;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/17 17:35]
 **/
public class MainTest1234V2 {

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
        MediaUtil.instance().extractCoverDir(new File("C:\\Users\\1\\Downloads\\6.High-definition enhancement（已处理首尾相连）"));
    }

    @Test
    public void test32324() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\temp\\poi3\\模特列表.xlsx"));
        List<Map<String, Object>> dataList = ExcelUtil.importData(workbook.getSheetAt(0));
        PrintWriter printWriter = FileUtil.getPrintWriter("D:\\temp\\poi3\\模特列表_export2.sql", StandardCharsets.UTF_8, false);
        String name = null;
        long currentTimeMillis = System.currentTimeMillis();
        long startId = 240;
        for (Map<String, Object> objectMap : dataList) {
            Object nameItem = objectMap.get("模特名称");
            if (nameItem != null) {
                name = nameItem.toString();
            }
            if (objectMap.get("模特名称") == null || StringUtils.isBlank(objectMap.get("模特视频位置").toString())) {
                continue;
            }
            String ttsId = "221";
            String format = DateUtil.format(new Date(currentTimeMillis), "yyyy-MM-dd HH:mm:ss");
            String videoPath = objectMap.get("模特视频位置").toString();
            //https://anylang.obs.ap-southeast-3.myhuaweicloud.com/anylang-video/resources/robot_public/20240305/1.mp4
            String videoUrl = "https://digital-public.obs.cn-east-3.myhuaweicloud.com/anylang/anylang-video/resources/robot_public/20240305/" + videoPath.replaceAll("\\\\", "/");
            String coverUrl = videoUrl.substring(0, videoUrl.lastIndexOf(".")) + "_cover.png";
            MultimediaInfo mediaInfo = MediaUtil.instance().getMediaInfo(videoUrl);
            VideoSize realVideoSize = MediaUtil.getRealVideoSize(mediaInfo);
            String sql = "INSERT INTO `ffo-toc`.user_video_robot " +
                    "(id, user_id, robot_name, robot_code, scene_code, cover_url, video_url, duration, vertical, horizontal, train_status, " +
                    "tts_id, demo_video_make_status, demo_cover_url, demo_video_url, `type` ,silent_video_url, ext, del_flag, create_time, update_time) " +
                    "VALUES(" + startId + ", -1, '" + name + "', NULL, NULL, '" + coverUrl + "', '" + videoUrl + "', " + mediaInfo.getDuration() + ", '" + realVideoSize.getHeight() + "', '" + realVideoSize.getWidth() +
                    "', 20, '" + ttsId + "'," +
                    " '20', '" + coverUrl + "', '" + videoUrl + "', 2, '" + videoUrl + "', NULL, 0, '" + format + "', '" + format + "');\r\n";
            printWriter.write(sql);
            startId++;
            currentTimeMillis = currentTimeMillis - (5 * 60 * 1000);
        }
        printWriter.flush();
        printWriter.close();
        System.out.println("解析完成");
    }

    @Test
    public void exportTts() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(new File("D:\\temp\\poi3\\0.1.10周二上线版本公共模特及音色.xlsx"));
        List<Map<String, Object>> dataList = ExcelUtil.importData(workbook.getSheetAt(0));
        PrintWriter printWriter = FileUtil.getPrintWriter("D:\\temp\\poi3\\0.1.10周二上线版本公共模特及音色_export2.sql", StandardCharsets.UTF_8, false);
        long currentTimeMillis = System.currentTimeMillis();
        long startId = 240;
        for (Map<String, Object> objectMap : dataList) {
            String format = DateUtil.format(new Date(currentTimeMillis), "yyyy-MM-dd HH:mm:ss");
            String speaker_id = objectMap.get("音色ID").toString();
            String lang = objectMap.get("语种简称").toString();
            String langZh = objectMap.get("语种").toString();
            String name = objectMap.get("硅语AI知识创业平台 微软音色名称").toString();
            String path = objectMap.get("硅语企业版微软音色文件名（含路径）").toString();
            //https://digital-public.obs.cn-east-3.myhuaweicloud.com/anylang/anylang-video/resources/robot_public/20240305TTS/index/timbre/ar/m1/.DS_Store
            String audioUrl = "https://digital-public.obs.cn-east-3.myhuaweicloud.com/anylang/anylang-video/resources/robot_public/20240305TTS" + path.replaceAll("\\\\", "/");
            String sql = "INSERT INTO `ffo-toc`.user_video_tts\n" +
                    "(id, user_id, speaker_id, speaker, audio_url, speaker_label, audition_url, sex, lang, train_status, robot_id, `type`, free_type, ext, del_flag, create_time, update_time)\n" +
                    "VALUES(" + startId + ", -1, '" + speaker_id + "', '" + name + "', '', 'From Market', '" + audioUrl + "', '', '" + lang + "', 20, NULL, 2, 1, " +
                    "'{\"lang\":\"" + langZh + "\",\"ttsSource\":\"2\"}', 0, '" + format + "', '" + format + "');";
            printWriter.write(sql);
            startId++;
            currentTimeMillis = currentTimeMillis - (5 * 60 * 1000);
        }
        printWriter.flush();
        printWriter.close();
        System.out.println("解析完成");
    }

}