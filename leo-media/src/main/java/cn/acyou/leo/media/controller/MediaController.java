package cn.acyou.leo.media.controller;

import cn.acyou.leo.media.encoder.ExecProcess;
import cn.acyou.leo.media.encoder.MediaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author youfang
 * @version [1.0.0, 2022/2/24 16:33]
 **/
@Slf4j
@Controller
@RequestMapping("media")
public class MediaController {

    /**
     * 获取第一帧
     * http://localhost:8060/media/firstFrame?vPath=http://qiniu.acyou.cn/video/1.mp4
     *
     * @param vPath    地址
     * @param response res
     * @throws Exception ex
     */
    @RequestMapping(value = "firstFrame", method = {RequestMethod.GET})
    public void firstFrame(@RequestParam("vPath") String vPath, String offset, HttpServletResponse response) throws Exception {
        MultimediaObject object = new MultimediaObject(new URL(vPath));
        File firstFrameFile = File.createTempFile("FirstFrame", "firstFrameFile.jpg");
        try {
            MultimediaInfo multimediaInfo = object.getInfo();
            VideoInfo videoInfo = multimediaInfo.getVideo();
            VideoAttributes video = new VideoAttributes();
            video.setCodec("png");
            video.setSize(videoInfo.getSize());
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat("image2");
            //offset 未赋值的时候采用默认
            if (StringUtils.isNotBlank(offset)) {
                attrs.setOffset(Float.valueOf(offset));
            }
            attrs.setDuration(0.01f);//设置转码持续时间（1秒）
            attrs.setVideoAttributes(video);
            Encoder encoder = new Encoder();
            encoder.encode(object, firstFrameFile, attrs);
            IOUtils.copyLarge(new FileInputStream(firstFrameFile), response.getOutputStream());
        } catch (EncoderException e) {
            log.error(e.getMessage(), e);
        } finally {
            firstFrameFile.delete();
        }
    }


    /**
     * 从视频提取音频
     * http://localhost:8060/media/extractAudio?vPath=http://qiniu.acyou.cn/video/568b5fe3f98e35536fce71a7c7e07af6.mp4
     *
     * @param vPath    地址
     * @param response res
     * @throws Exception ex
     */
    @RequestMapping(value = "extractAudio", method = {RequestMethod.GET})
    public void extractAudio(@RequestParam("vPath") String vPath, HttpServletResponse response) throws Exception {
        File extractAudioFile = File.createTempFile("FirstFrame", "extractAudioFile.mp3");
        try {
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame");
            audio.setBitRate(128000);
            audio.setChannels(2);
            audio.setSamplingRate(44100);
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat("mp3");
            attrs.setAudioAttributes(audio);
            Encoder encoder = new Encoder();
            MultimediaObject mediaObject = new MultimediaObject(new URL(vPath));
            encoder.encode(mediaObject, extractAudioFile, attrs);
            IOUtils.copyLarge(new FileInputStream(extractAudioFile), response.getOutputStream());
        } catch (EncoderException e) {
            log.error(e.getMessage(), e);
        } finally {
            extractAudioFile.delete();
        }
    }


    /**
     * 从视频提取音频
     * https://guiyu-tici.oss-cn-shanghai.aliyuncs.com/tici/354-2-20220407180008326.aac
     * http://localhost:8060/media/getVideoInfo?vPath=http://qiniu.acyou.cn/video/568b5fe3f98e35536fce71a7c7e07af6.mp4
     *
     * @param vPath    地址
     * @param response res
     * @throws Exception ex
     */
    @RequestMapping(value = "getVideoInfo", method = {RequestMethod.GET})
    @ResponseBody
    public MultimediaInfo getVideoInfo(@RequestParam("vPath") String vPath, HttpServletResponse response) throws Exception {
        MultimediaInfo m = new MultimediaInfo();
        try {
            MultimediaObject MultimediaObject = new MultimediaObject(new URL(vPath));
            m = MultimediaObject.getInfo();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return m;
    }

    private final static ExecutorService executorService = Executors.newFixedThreadPool(10);

    @RequestMapping(value = "test1", method = {RequestMethod.GET})
    @ResponseBody
    public String test1() throws Exception {
        executorService.execute(() -> {
            List<String> commands = new ArrayList<>();
            commands.add("-i");
            commands.add("http://qiniu.acyou.cn/media/mobile/IMG_0026.MOV");
            commands.add("-vcodec");
            commands.add("h264");
            commands.add("-y");
            commands.add("E:\\media\\4\\T5.mp4");

            MediaUtil.instance(new ExecProcess() {
                @Override
                public void progress(long perm) {
                    System.out.println("进度：" + (new BigDecimal(perm).multiply(new BigDecimal("0.1"))));
                }
            }).exec(commands.toArray(new String[0]));
        });
        return "ok";
    }


}
