package cn.acyou.leo.media.util;

import org.apache.commons.lang3.ArrayUtils;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2022/3/23 14:14]
 **/
public class FFmpeg {
    /**
     * 可以获取ffmpeg.exe路径
     */
    private final static DefaultFFMPEGLocator ffmpegLocator = new DefaultFFMPEGLocator();

    public static void exec(String... command){
        String[] args = ArrayUtils.addFirst(command, ffmpegLocator.getExecutablePath());
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(args);
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
