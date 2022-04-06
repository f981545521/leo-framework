package cn.acyou.leo.media.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * @author youfang
 * @version [1.0.0, 2022/3/23 14:14]
 **/
@Slf4j
public class MediaUtil {
    /**
     * 获取ffmpeg加载器（可以获取ffmpeg.exe路径）
     */
    private final static DefaultFFMPEGLocator ffmpegLocator = new DefaultFFMPEGLocator();

    /**
     * 执行FFMPEG命令
     *
     * @param command 命令
     */
    private static void exec(String... command) {
        String[] args = ArrayUtils.addFirst(command, ffmpegLocator.getExecutablePath());
        log.info("执行FFMPEG 命令:{}", StringUtils.join(args, " "));
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(args);
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            log.error("执行FFMPEG出错了 命令:{}", StringUtils.join(args, " "));
            e.printStackTrace();
        }
    }

    /**
     * 剪切音频
     *
     * @param source     源  （可以是URL、可以是File的AbsolutePath）
     * @param ss         开始 （开始毫秒数）
     * @param to         结束 （结束毫秒数）
     * @param targetPath 目标路径 （File的AbsolutePath）
     */
    public static void cutAudio(String source, long ss, long to, String targetPath) {
        boolean mkdirs = new File(targetPath).getParentFile().mkdirs();
        log.info("剪切音频文件 params:[source:{}, ss:{}, to:{}, target:{}] 目标目录：{}", source, ss, to, targetPath, (mkdirs ? "创建成功" : "无需创建"));
        MediaUtil.exec("-y", "-i", source, "-ss", formatDuring(ss), "-to", formatDuring(to), "-c", "copy", targetPath);
    }

    /**
     * 合并音频和视频
     *
     * @param audioSource 音频源
     * @param videoSource 视频源
     * @param targetPath  目标路径（File的AbsolutePath）
     */
    public static void mergeAudioAndVideo(String audioSource, String videoSource, String targetPath) {
        boolean mkdirs = new File(targetPath).getParentFile().mkdirs();
        log.info("合并音/视频文件 params:[audioSource:{}, videoSource:{}, target:{}] 目标目录：{}", audioSource, videoSource, targetPath, (mkdirs ? "创建成功" : "无需创建"));
        MediaUtil.exec("-i", audioSource, "-i", videoSource, "-vcodec", "copy", "-acodec", "copy", targetPath);
    }

    /**
     * 格式化毫秒数
     *
     * <pre>
     *   System.out.println(formatDuring(30000));//00:00:30.000
     *   System.out.println(formatDuring(33000));//00:00:33.000
     *   System.out.println(formatDuring(33300));//00:00:33.300
     *   System.out.println(formatDuring(33330));//00:00:33.330
     *   System.out.println(formatDuring(33333));//00:00:33.333
     * </pre>
     *
     * @param mss 海量存储系统(mss)中
     * @return {@link String}
     */
    public static String formatDuring(long mss) {
        String hours = ((mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)) + "";
        hours = hours.length() == 1 ? 0 + hours : hours;
        String minutes = ((mss % (1000 * 60 * 60)) / (1000 * 60)) + "";
        minutes = minutes.length() == 1 ? 0 + minutes : minutes;
        String seconds = new BigDecimal((mss % (1000 * 60))).divide(new BigDecimal(1000), 3, RoundingMode.CEILING).toString();
        return hours + ":" + minutes + ":" + seconds;
    }

    /**
     * 提取帧
     * <p>
     * ffmpeg.exe -i .\991hsxtp3ctse4yg.mp4 -ss 1 -f image2 .\out1.jpg
     *
     * @param i          输入
     * @param ss         第ss帧
     * @param targetPath 输出目录
     */
    public static void extractFrame(String i, String ss, String targetPath) {
        boolean mkdirs = new File(targetPath).getParentFile().mkdirs();
        log.info("提取帧 params:[i:{}, ss:{}, target:{}] 目标目录：{}", i, ss, targetPath, (mkdirs ? "创建成功" : "无需创建"));
        MediaUtil.exec("-i", i, "-ss", ss, "-f", "image2", targetPath);
    }

    /**
     * 提取帧
     *
     * @param url        url
     * @param speedRatio 进度比例
     * @param targetDir  目标目录
     * @return {@link String[]} 目标文件路径列表
     * @throws Exception 异常
     */
    public static String[] extractFrameBySpeedRatio(String url, int[] speedRatio, String targetDir) throws Exception {
        String[] paths = new String[speedRatio.length];
        MultimediaObject object = new MultimediaObject(new URL(url));
        MultimediaInfo info = object.getInfo();
        long duration = info.getDuration();
        for (int i = 0; i < speedRatio.length; i++) {
            int t = (int) (duration * (speedRatio[i] * 0.01));
            String targetPath = targetDir + t + ".jpg";
            MediaUtil.extractFrame(url, DecimalFormat.getInstance().format(t * 0.001), targetPath);
            paths[i] = targetPath;
        }
        return paths;
    }
}
