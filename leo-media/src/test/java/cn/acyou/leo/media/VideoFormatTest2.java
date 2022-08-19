package cn.acyou.leo.media;

import cn.acyou.leo.media.encoder.ExecProcess;
import cn.acyou.leo.media.encoder.MediaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoInfo;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2022-4-22]
 **/
@Slf4j
public class VideoFormatTest2 {

    @Test
    public void testMain() throws Exception {
        MultimediaObject object = new MultimediaObject(new URL("https://guiyu-tici.oss-cn-shanghai.aliyuncs.com/tici/634-1-202204211746399.mp4"));
        File firstFrameFile = new File("E:\\media\\5\\1.jpg");
        try {
            MultimediaInfo multimediaInfo = object.getInfo();
            VideoInfo videoInfo = multimediaInfo.getVideo();
            VideoAttributes video = new VideoAttributes();
            video.setCodec("png");
            video.setSize(videoInfo.getSize());
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat("image2");
            attrs.setDuration(0.01f);//设置转码持续时间（1秒）
            attrs.setVideoAttributes(video);
            Encoder encoder = new Encoder();
            encoder.encode(object, firstFrameFile, attrs);
        } catch (EncoderException e) {
            log.error(e.getMessage(), e);
        } finally {
            firstFrameFile.delete();
        }
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
        commands.add("https://guiyu-tici.oss-cn-shanghai.aliyuncs.com/tici/643-1-202204221851007.MOV");
        commands.add("-vcodec");
        commands.add("h264");
        commands.add("-y");
        commands.add("E:\\media\\4\\BIG6.mp4");

        System.out.println(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        MediaUtil.instance(new ExecProcess() {
            @Override
            public void progress(long perm) {
                System.out.println("进度：" + (new BigDecimal(perm).multiply(new BigDecimal("0.1"))));
            }
        }).exec(commands);
        System.out.println(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        System.out.println("ok");
    }

    @Test
    public void test21() {
        System.out.println(MediaUtil.formatDuration(60000));
        System.out.println(MediaUtil.formatDuration(420000));
        System.out.println(MediaUtil.formatDuration(600000));
        MultimediaInfo mediaInfo = MediaUtil.instance().getMediaInfo("http://guiyu-tici.oss-cn-shanghai.aliyuncs.com/resources/aiTestAudio/female/Aixia_10mins.wav");
        System.out.println(mediaInfo.getDuration());
        System.out.println(MediaUtil.formatDuration(mediaInfo.getDuration()));
    }

    @Test
    public void test22() {
        System.out.println(MediaUtil.formatDuration(4911100));
        System.out.println(MediaUtil.parseDuration("01:21:51.100"));
    }

}
