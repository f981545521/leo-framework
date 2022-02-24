package cn.acyou.leo.media.controller;

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
import java.net.URL;

/**
 * @author youfang
 * @version [1.0.0, 2022/2/24 16:33]
 **/
@Controller
@RequestMapping("media")
public class MediaController {

    /**
     * 获取第一帧
     * http://localhost:8060/media/firstFrame?vPath=http://qiniu.acyou.cn/video/568b5fe3f98e35536fce71a7c7e07af6.mp4
     *
     * @param vPath    地址
     * @param response res
     * @throws Exception ex
     */
    @RequestMapping(value = "firstFrame", method = {RequestMethod.GET})
    public void firstFrame(@RequestParam("vPath") String vPath, HttpServletResponse response) throws Exception {
        MultimediaObject object = new MultimediaObject(new URL(vPath));
        File firstFrameFile = File.createTempFile("FirstFrame", "firstFrameFile.jpg");
        try {
            MultimediaInfo multimediaInfo = object.getInfo();
            VideoInfo videoInfo = multimediaInfo.getVideo();
            VideoAttributes video = new VideoAttributes();
            video.setCodec("png");
            video.setSize(videoInfo.getSize());
            EncodingAttributes attrs = new EncodingAttributes();
            //VideoAttributes attrs = ecodeAttrs.getVideoAttributes().get();
            attrs.setOutputFormat("image2");
            attrs.setOffset(1f);//设置偏移位置，即开始转码位置（11秒）
            attrs.setDuration(0.01f);//设置转码持续时间（1秒）
            attrs.setVideoAttributes(video);
            Encoder encoder = new Encoder();
            encoder.encode(object, firstFrameFile, attrs);
            IOUtils.copyLarge(new FileInputStream(firstFrameFile), response.getOutputStream());
        } catch (EncoderException e) {
            e.printStackTrace();
        } finally {
            firstFrameFile.deleteOnExit();
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
            e.printStackTrace();
        } finally {
            extractAudioFile.deleteOnExit();
        }
    }


    /**
     * 从视频提取音频
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
            e.printStackTrace();
        }
        return m;
    }


}
