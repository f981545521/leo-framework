package cn.acyou.leo.media.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoInfo;
import ws.schild.jave.info.VideoSize;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * @author youfang
 * @version [1.0.0, 2022/2/22 19:19]
 **/
public class VideoFormatTest {

    public static Logger Log = LoggerFactory.getLogger(VideoFormatTest.class);

    /**
     * 视频文件转音频文件
     * @param videoPath
     * @param audioPath
     * @return
     */
    public static boolean videoToAudio(String videoPath, String audioPath) {
        File fileMp4=new File(videoPath);
        File fileMp3=new File(audioPath);

        //Audio Attributes
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(128000);
        audio.setChannels(2);
        audio.setSamplingRate(44100);

        //Encoding attributes
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp3");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        MultimediaObject mediaObject=new MultimediaObject(fileMp4);
        try {
            encoder.encode(mediaObject, fileMp3, attrs);
            Log.info("File MP4 convertito in MP3");
            return true;
        } catch (Exception e) {
            Log.error("File non convertito");
            Log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 获取视频的基本信息，视频长宽高，视频的大小等
     * @param fileSource
     * @return
     */
    public static Map<String, Object> getVideoInfo(String fileSource) {
        // String filePath =
        // Utils.class.getClassLoader().getResource(fileSource).getPath();
        File source = new File(fileSource);
        //Encoder encoder = new Encoder();
        FileInputStream fis = null;
        FileChannel fc = null;
        Map<String, Object> videoInfo = new HashMap<>();
        try {
            MultimediaObject MultimediaObject=new MultimediaObject(source);
            MultimediaInfo m = MultimediaObject.getInfo();
            fis = new FileInputStream(source);
            fc = fis.getChannel();
            videoInfo.put("width", m.getVideo().getSize().getWidth());
            videoInfo.put("heigth", m.getVideo().getSize().getHeight());
            videoInfo.put("size", fc.size());
            videoInfo.put("duration", m.getDuration());
            videoInfo.put("format", m.getFormat());
            System.out.println(videoInfo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fc) {
                try {
                    fc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return videoInfo;
    }

    /**
     * 截取视频中某一帧作为图片
     * @param videoPath
     * @param imagePath
     * @return
     */
    public static boolean getVideoProcessImage(String videoPath,String imagePath){
        File videoSource = new File(videoPath);
        File imageTarget = new File(imagePath);
        MultimediaObject object = new MultimediaObject(videoSource);
        try {
            MultimediaInfo multimediaInfo = object.getInfo();
            VideoInfo videoInfo=multimediaInfo.getVideo();
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
            encoder.encode(object,imageTarget,attrs);
            return true;
        } catch (EncoderException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * m4r音频格式转换为mp3，audioPath可更换为要转换的音频格式
     * @param audioPath
     * @param mp3Path
     */
    public static void m4rToMp3(String audioPath,String mp3Path){
        File source = new File(audioPath);
        File target = new File(mp3Path);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(new Integer(128000));
        audio.setChannels(new Integer(2));
        audio.setSamplingRate(new Integer(44100));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp3");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(source), target, attrs);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从和视频中提取音频wav
     * @param aviPath
     * @param targetWavPath
     */
    public static void videoExtractAudio(String aviPath,String targetWavPath){
        File source = new File(aviPath);
        File target = new File(targetWavPath);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("pcm_s16le");
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("wav");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(source), target, attrs);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    /**
     * 视频转换为手机可播放的格式
     * @param sourceVideo sourceVideo.avi
     * @param targetVideo targetVideo.3gp
     */
    public static void videoToMobileVideo(String sourceVideo, String targetVideo){
        File source = new File("source.avi");
        File target = new File("target.3gp");
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libfaac");
        audio.setBitRate(new Integer(128000));
        audio.setSamplingRate(new Integer(44100));
        audio.setChannels(new Integer(2));
        VideoAttributes video = new VideoAttributes();
        video.setCodec("mpeg4");
        video.setBitRate(new Integer(160000));
        video.setFrameRate(new Integer(15));
        video.setSize(new VideoSize(176, 144));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("3gp");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(source), target, attrs);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    public static void main4(String[] args) {
        File source = new File("C:\\Users\\1\\Music\\汤倩 - 随便吧.mp3");
        File target = new File("C:\\Users\\1\\Music\\汤倩 - 随便吧_2.wav");
        MultimediaObject multimediaObject = new MultimediaObject(source);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("pcm_s16le");
        audio.setSamplingRate(16000);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("wav");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        try {
            encoder.encode(multimediaObject, target, attrs);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    /**
     * 可以获取ffmpeg.exe路径
     */
    private final static DefaultFFMPEGLocator ffmpegLocator=new DefaultFFMPEGLocator();

    /**
     * FFMPEG 截取音频指定部分
     *
     * @param sourcePath 源路径
     * @param targetPath 目标路径
     * @param start      开始
     * @param end        结束
     * @return boolean
     * @throws RuntimeException 运行时异常
     */
    public static boolean cutByFfmpeg(String sourcePath, String targetPath, int start, int end) throws RuntimeException {
        if (start < 0 || end <= 0 ||  start >= end) {
            return false;
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Log.info("开始剪辑文件{}，{}ms-{}ms", sourcePath, start, end);
        File targetFile = new File(targetPath);
        File sourceFile = new File(sourcePath);
        Process proc;
        String[] args = new String[]{ffmpegLocator.getExecutablePath(), "-y", "-i", sourceFile.getAbsolutePath(),"-ss", MediaUtil.formatDuring(start),"-to", MediaUtil.formatDuring(end), "-c","copy", targetFile.getAbsolutePath()};

        System.out.println(Arrays.toString(args));
        try {
            //先删除，后面好判断是否剪辑成功
            if (targetFile.exists()) {
                targetFile.delete();
            }
            proc = Runtime.getRuntime().exec(args);
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        Log.info("剪辑文件{}完成，{}ms-{}ms，耗时{}秒", sourcePath, start, end, stopWatch.getTotalTimeSeconds());
        Log.info("剪辑文件为{}", targetPath);
        return targetFile.exists();
    }

    public static void main(String[] args) {
        //cutByFfmpeg("C:\\Users\\1\\Music\\G.E.M.邓紫棋 - A.I.N.Y..mp3", "C:\\Users\\1\\Music\\G.E.M.邓紫棋 - A.I.N.Y._1.mp3", 30000, 40000);
        //System.out.println("end");

        //MediaUtil.exec("-y", "-i", "F:\\KuGou\\柳爽 - 漠河舞厅.mp3", "-ss", "00:00:40.000", "-to", "00:00:50.000", "-c", "copy", "F:\\KuGou\\柳爽 - 漠河舞厅_2.mp3");
        MediaUtil.cutAudio("http://qiniu.acyou.cn/audio/1.mp3", 10000, 20000, "E:\\KuGou2\\柳爽 - 漠河舞厅_54.mp3");
    }

}
