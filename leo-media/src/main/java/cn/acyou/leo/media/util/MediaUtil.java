package cn.acyou.leo.media.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
    private static void exec(String... command){
        String[] args = ArrayUtils.addFirst(command, ffmpegLocator.getExecutablePath());
        log.info("执行FFMPEG 命令:{}",StringUtils.join(args, " "));
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(args);
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            log.error("执行FFMPEG出错了 命令:{}",StringUtils.join(args, " "));
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
        log.info("剪切音频文件 params:[source:{},ss:{},to:{},target:{}] 目标目录：{}", source, ss, to, targetPath, (mkdirs ? "创建成功" : "无需创建"));
        MediaUtil.exec("-y", "-i", source, "-ss", formatDuring(ss), "-to", formatDuring(to), "-c", "copy", targetPath);
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
        String hours = ((mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))+"";
        hours = hours.length() == 1 ? 0 + hours : hours;
        String minutes = ((mss % (1000 * 60 * 60)) / (1000 * 60)) + "";
        minutes = minutes.length() == 1 ? 0 + minutes : minutes;
        String seconds = new BigDecimal((mss % (1000 * 60))).divide(new BigDecimal(1000), 3, RoundingMode.CEILING).toString();
        return hours+ ":" + minutes + ":" + seconds;
    }

}
