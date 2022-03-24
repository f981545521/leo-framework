package cn.acyou.leo.media.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author youfang
 * @version [1.0.0, 2022/3/23 14:14]
 **/
public class MediaUtil {
    /**
     * 可以获取ffmpeg.exe路径
     */
    private final static DefaultFFMPEGLocator ffmpegLocator = new DefaultFFMPEGLocator();

    public static void exec(String... command){
        String[] args = ArrayUtils.addFirst(command, ffmpegLocator.getExecutablePath());
        System.out.println(StringUtils.join(args, " "));
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(args);
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void cutAudio(String source, long ss, long to, String targetPath){
        MediaUtil.exec("-y", "-i", source, "-ss", formatDuring(ss), "-to", formatDuring(to), "-c", "copy", targetPath);
    }

    public static String formatDuring(long mss) {
        String hours = ((mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))+"";
        hours = hours.length() == 1 ? 0 + hours : hours;
        String minutes = ((mss % (1000 * 60 * 60)) / (1000 * 60)) + "";
        minutes = minutes.length() == 1 ? 0 + minutes : minutes;
        String seconds = new BigDecimal((mss % (1000 * 60))).divide(new BigDecimal(1000), 3, RoundingMode.CEILING).toString();
        return hours+ ":" + minutes + ":" + seconds;
    }

}
