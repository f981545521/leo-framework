package cn.acyou.leo.media;

import cn.acyou.leo.media.encoder.ExecProcess;
import cn.acyou.leo.media.encoder.MediaUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2022-4-22]
 **/
public class VideoFormatTest2 {

    @Test
    public void testMain() throws Exception {

    }

    /**
     * 从视频中提取音频文件
     */
    @Test
    public void test1() throws Exception {
        List<String> commands = new ArrayList<>();
        commands.add("-i");
        commands.add("D:\\ToUpload\\WeChat_20220421161548.mp4");
        commands.add("-vn");
        commands.add("-acodec");
        commands.add("libmp3lame");
        commands.add("-ab");
        commands.add("128000");
        commands.add("-ac");
        commands.add("2");
        commands.add("-ar");
        commands.add("44100");
        commands.add("-f");
        commands.add("mp3");
        commands.add("-y");
        commands.add("D:\\ToUpload\\WeChat_20220421161548_3.mp3");

        MediaUtil.instance(new ExecProcess() {
            @Override
            public void progress(long perm) {
                System.out.println("进度：" + (new BigDecimal(perm).multiply(new BigDecimal("0.1"))));
            }
        }).exec(commands.toArray(new String[0]));

        System.out.println("ok");
    }

    /**
     * 拆分视频为序列帧图组
     */
    @Test
    public void test2() throws Exception {
        //拆分视频序列帧
        //ffmpeg.exe -i .\333.mp4  -q:v 2 .\frames\222222_%03d.png
        List<String> commands = new ArrayList<>();
        commands.add("-i");
        commands.add("D:\\ToUpload\\3\\333.mp4");
        commands.add("-q:v");
        commands.add("2");
        commands.add("D:\\ToUpload\\3\\frames\\333_%03d.png");

        MediaUtil.instance(new ExecProcess() {
            @Override
            public void progress(long perm) {
                System.out.println("进度：" + (new BigDecimal(perm).multiply(new BigDecimal("0.1"))));
            }
        }).exec(commands.toArray(new String[0]));

        System.out.println("ok");
    }

    /**
     * 视频文件转码为 h264 的mp4格式
     */
    @Test
    public void test3() throws Exception {
        List<String> commands = new ArrayList<>();
        commands.add("-i");
        commands.add("http://qiniu.acyou.cn/media/mobile/IMG_0026.MOV");
        commands.add("-vcodec");
        commands.add("h264");
        commands.add("-y");
        commands.add("E:\\media\\4\\T2.mp4");

        MediaUtil.instance(new ExecProcess() {
            @Override
            public void progress(long perm) {
                System.out.println("进度：" + (new BigDecimal(perm).multiply(new BigDecimal("0.1"))));
            }
        }).exec(commands.toArray(new String[0]));

        System.out.println("ok");
    }
}
