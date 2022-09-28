package cn.acyou.leo.media.encoder;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileSystemUtils;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.filters.Filter;
import ws.schild.jave.filters.FilterChain;
import ws.schild.jave.filters.FilterGraph;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoInfo;
import ws.schild.jave.info.VideoSize;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * 必须有（否则存在卡住问题：要处理 prop.getErrorStream）
     */
    private ExecProcess execProcess;

    private MediaUtil(ExecProcess execProcess) {
        this.execProcess = execProcess;
    }

    /**
     * 实例
     *
     * @return {@link MediaUtil}
     */
    public static MediaUtil instance() {
        return new MediaUtil(new ExecProcess() {
        });
    }

    /**
     * 实例（进度）
     *
     * @param execProcess 执行过程
     * @return {@link MediaUtil}
     */
    public static MediaUtil instance(ExecProcess execProcess) {
        return new MediaUtil(execProcess);
    }

    /**
     * 执行FFMPEG命令
     *
     * @param commands 命令
     */
    public void exec(List<String> commands) {
        exec(commands.toArray(new String[0]));
    }

    /**
     * 执行FFMPEG命令
     *
     * @param command 命令
     */
    public void exec(String... command) {
        String[] args = ArrayUtils.addFirst(command, ffmpegLocator.getExecutablePath());
        log.info("执行FFMPEG开始 命令:{}", StringUtils.join(args, " "));
        List<?> objects = CollectionUtils.arrayToList(args);
        int i = objects.indexOf("-i");
        String input = "";
        if (i > 0) {
            input = (String) objects.get(i + 1);
        }
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(args);
            if (execProcess != null) {
                execProcess.handlerOutPut(input, proc.getErrorStream());
            }
            proc.waitFor();
            if (execProcess != null) {
                execProcess.progress(1000);
            }
            log.info("执行FFMPEG成功 命令:{}", StringUtils.join(args, " "));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("执行FFMPEG出错 命令:{}", StringUtils.join(args, " "));
            throw new RuntimeException("执行FFMPEG出错 命令:{}" + StringUtils.join(args, " "));
        }
    }

    /**
     * 获取视频帧数
     *
     * @param source 源
     * @return {@link Long} 帧数
     */
    public Long getFrameCount(String source) {
        log.info("获取视频帧数 params:[source:{}] ", source);
        final Long[] frameCount = {0L};
        this.execProcess = new ExecProcess() {
            @Override
            public void frame(Long frame) {
                frameCount[0] = frame;
            }
        };
        exec("-i", source, "-map", "0:v:0", "-c", "copy", "-f", "null", "-");
        return frameCount[0];
    }

    /**
     * 剪切音频
     *
     * @param source     源  （可以是URL、可以是File的AbsolutePath）
     * @param ss         开始 （开始毫秒数）
     * @param to         结束 （结束毫秒数）
     * @param targetPath 目标路径 （File的AbsolutePath）
     */
    public void cutAudio(String source, long ss, long to, String targetPath) {
        boolean mkdirs = new File(targetPath).getParentFile().mkdirs();
        log.info("剪切音频文件 params:[source:{}, ss:{}, to:{}, target:{}] 目标目录：{}", source, ss, to, targetPath, (mkdirs ? "创建成功" : "无需创建"));
        exec("-y", "-i", source, "-ss", formatDuration(ss), "-to", formatDuration(to), "-c", "copy", targetPath);
    }

    /**
     * 合并音频和视频
     *
     * @param audioSource 音频源
     * @param videoSource 视频源
     * @param targetPath  目标路径（File的AbsolutePath）
     */
    public void mergeAudioAndVideo(String audioSource, String videoSource, String targetPath) {
        boolean mkdirs = new File(targetPath).getParentFile().mkdirs();
        log.info("合并音/视频文件 params:[audioSource:{}, videoSource:{}, target:{}] 目标目录：{}", audioSource, videoSource, targetPath, (mkdirs ? "创建成功" : "无需创建"));
        exec("-y", "-i", audioSource, "-i", videoSource, "-vcodec", "copy", "-acodec", "copy", targetPath);
    }

    /**
     * 格式化毫秒数
     *
     * <pre>
     *   System.out.println(formatDuration(30000));//00:00:30.000
     *   System.out.println(formatDuration(33000));//00:00:33.000
     *   System.out.println(formatDuration(33300));//00:00:33.300
     *   System.out.println(formatDuration(33330));//00:00:33.330
     *   System.out.println(formatDuration(33333));//00:00:33.333
     * </pre>
     *
     * @param mss 毫秒
     * @return {@link String}
     */
    public static String formatDuration(long mss) {
        String hours = ((mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)) + "";
        hours = hours.length() == 1 ? 0 + hours : hours;
        String minutes = ((mss % (1000 * 60 * 60)) / (1000 * 60)) + "";
        minutes = minutes.length() == 1 ? 0 + minutes : minutes;
        String seconds = new BigDecimal((mss % (1000 * 60))).divide(new BigDecimal(1000), 3, RoundingMode.CEILING).toString();
        seconds = seconds.indexOf(".") == 1 ? 0 + seconds : seconds;
        return hours + ":" + minutes + ":" + seconds;
    }

    /**
     * 将媒体时间格式转换成毫秒数
     *
     * <pre>
     *     MediaUtil.parseDuration("01:21:51.100"); //4911100
     * </pre>
     *
     * @param str str
     * @return long
     */
    public static long parseDuration(String str) {
        String[] split = str.split("\\.");
        String hms = split[0];
        long ms = Long.parseLong(split[1]);
        String[] hmsArray = hms.split(":");
        long h = Long.parseLong(hmsArray[0]);
        long m = Long.parseLong(hmsArray[1]);
        long s = Long.parseLong(hmsArray[2]);
        return ms + (s * 1000) + (m * 60 * 1000) + (h * 60 * 60 * 1000);
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
    public void extractFrame(String i, String ss, String targetPath) {
        boolean mkdirs = new File(targetPath).getParentFile().mkdirs();
        log.info("提取帧 params:[i:{}, ss:{}, target:{}] 目标目录：{}", i, ss, targetPath, (mkdirs ? "创建成功" : "无需创建"));
        exec("-y", "-i", i, "-ss", ss, "-f", "image2", targetPath);
    }

    /**
     * 提取帧
     *
     * @param url        url
     * @param speedRatio 进度比例
     * @param targetDir  目标目录 无"/"结尾 。可以直接使用 getAbsolutePath()
     * @return String[] 目标文件路径列表
     * @throws Exception 异常
     */
    public String[] extractFrameBySpeedRatio(String url, int[] speedRatio, String targetDir) throws Exception {
        String[] paths = new String[speedRatio.length];
        MultimediaObject object = new MultimediaObject(new URL(url));
        MultimediaInfo info = object.getInfo();
        long duration = info.getDuration();
        for (int i = 0; i < speedRatio.length; i++) {
            int t = (int) (duration * (speedRatio[i] * 0.01));
            String targetPath = targetDir + File.separator + "frame_" + t + ".jpg";
            extractFrame(url, new BigDecimal(t).multiply(new BigDecimal("0.001")).toString(), targetPath);
            paths[i] = targetPath;
        }
        return paths;
    }

    /**
     * 分离音频通道
     *
     * <pre>
     * 参考命令：ffmpeg -i 1.wav -map_channel 0.0.0 output/1/1.wav -map_channel 0.0.1 output/2/1.wav
     * {@code
     * Map<String, String> param = new HashMap<>();
     * param.put("0.0.0", "D:\\temp\\channel\\1.wav");
     * param.put("0.0.1", "D:\\temp\\channel\\2.wav");
     * MediaUtil.separateAudioChannel("http://qiniu.acyou.cn/media/354-2-20220407180008326.aac", param);
     * }
     * </pre>
     *
     * @param url        url
     * @param channelOut 频道
     *                   0.0.0 -> D:\temp\channel\1.wav
     *                   0.0.1 -> D:\temp\channel\2.wav
     */
    public void separateAudioChannel(String url, Map<String, String> channelOut) {
        List<String> command = new ArrayList<>();
        command.add("-i");
        command.add(url);
        channelOut.forEach((k, v) -> {
            command.add("-map_channel");
            command.add(k);
            command.add(v);
        });
        exec(command.toArray(new String[0]));
    }

    public void extractCover(File source, File target) {
        extractCover(source.getAbsolutePath(), target, null);
    }

    public void extractCover(String source, File target, String offset) {
        MultimediaObject object = getMediaObject(source);
        if (object == null) {
            log.error("原对象不合法：{}", source);
        }
        try {
            VideoAttributes video = new VideoAttributes();
            video.setCodec("png");
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat("image2");
            //offset未赋值的时候采用默认
            attrs.setOffset(0.01f);//偏移量
            attrs.setDuration(0.01f);//设置转码持续时间
            if (StringUtils.isNotBlank(offset)) {
                attrs.setOffset(Float.valueOf(offset));
            }
            attrs.setVideoAttributes(video);
            Encoder encoder = new Encoder();
            encoder.encode(object, target, attrs);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * concat音频
     *
     * @param files  文件
     * @param target 目标
     * @return {@link File}
     */
    public File concatAudio(List<File> files, String target) {
        try {
            File file = new File(target);
            List<MultimediaObject> multimediaObjects = new ArrayList<>();
            files.forEach(s -> multimediaObjects.add(new MultimediaObject(s)));
            Encoder encoder = new Encoder();
            AudioAttributes audioAttributes = new AudioAttributes();
            VideoAttributes videoAttributes = new VideoAttributes();
            FilterGraph customFilter = new FilterGraph();
            FilterChain filterChain = new FilterChain();
            String s = buildString(files.size());
            filterChain.addFilter(new Filter(s));
            customFilter.addChain(filterChain);
            videoAttributes.setComplexFiltergraph(customFilter);
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setVideoAttributes(videoAttributes);
            attrs.setAudioAttributes(audioAttributes);
            encoder.encode(multimediaObjects, file, attrs);
            return file;
        } catch (Exception ex) {
            log.warn("mergeWav ex", ex);
        }
        return null;
    }

    private static String buildStringVideo(Integer num) {
        String build = "";
        for (int i = 0; i < num; i++) {
            build = build + "[" + i + ":0][" + i + ":1]";
        }
        return build + "concat=n=" + num + ":v=1:a=1";
    }

    private static String buildString(Integer num) {
        String build = "";
        for (int i = 0; i < num; i++) {
            build = build + "[" + i + ":0]";
        }
        return build + "concat=n=" + num + ":v=0:a=1";
    }

    /**
     * 拼接视频
     *
     * @param urls       url
     * @param targetPath 目标路径
     */
    public void concatVideo(List<String> urls, String targetPath) throws Exception {
        boolean mkdirs = new File(targetPath).getParentFile().mkdirs();
        log.info("拼接视频 params:[urls:{},  target:{}] 目标目录：{}", urls, targetPath, (mkdirs ? "创建成功" : "无需创建"));
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("MediaUtil_concatVideo_");
            List<String> tsList = new ArrayList<>();
            for (int i = 0; i < urls.size(); i++) {
                File tempTs = new File(tempDir.toFile(), i + ".ts");
                //转换成TS格式
                exec("-i", urls.get(i), "-c", "copy", "-bsf:v", "h264_mp4toannexb", "-f", "mpegts", tempTs.getAbsolutePath());
                tsList.add(tempTs.getAbsolutePath());
            }
            exec("-i", "concat:" + StringUtils.join(tsList, "|"), "-c", "copy", "-bsf:a", "aac_adtstoasc", "-movflags", "+faststart", targetPath);
        } finally {
            FileSystemUtils.deleteRecursively(tempDir);
        }
    }

    /**
     * 获取媒体时长信息
     *
     * @param i 文件绝对路径 或者 URL
     * @return {@link MultimediaInfo}
     */
    public long getMediaDuration(String i) {
        log.info("获取媒体时长信息：{}", i);
        try {
            MultimediaInfo mediaInfo = getMediaInfo(i);
            return mediaInfo.getDuration();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        throw new IllegalArgumentException("获取媒体时长信息：" + i);
    }

    /**
     * 得到媒体信息
     *
     * @param i 文件绝对路径 或者 URL
     * @return {@link MultimediaInfo}
     */
    public MultimediaInfo getMediaInfo(String i) {
        log.info("获取媒体信息：{}", i);
        try {
            if (StringUtils.isNotBlank(i)) {
                if (i.toLowerCase().startsWith("http")) {
                    return new MultimediaObject(new URL(i)).getInfo();
                } else {
                    return new MultimediaObject(new File(i)).getInfo();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        throw new IllegalArgumentException("获取媒体信息出错：" + i);
    }

    /**
     * 得到媒体信息
     *
     * @param i 文件绝对路径 或者 URL
     * @return {@link MultimediaInfo}
     */
    public MultimediaObject getMediaObject(String i) {
        log.info("获取媒体对象：{}", i);
        try {
            if (StringUtils.isNotBlank(i)) {
                if (i.toLowerCase().startsWith("http")) {
                    return new MultimediaObject(new URL(i));
                } else {
                    return new MultimediaObject(new File(i));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        throw new IllegalArgumentException("获取媒体对象出错：" + i);
    }

    /**
     * 获得真正的视频大小
     *
     * @param multimediaInfo 多媒体信息
     * @return {@link VideoSize}
     */
    public static VideoSize getRealVideoSize(MultimediaInfo multimediaInfo) {
        VideoInfo video = multimediaInfo.getVideo();
        Map<String, String> metadata = video.getMetadata();
        String rotate = metadata.get("rotate");
        if (StringUtils.isNotBlank(rotate)) {
            //旋转时纠正分辨率
            if ("90".equals(rotate) || "270".equals(rotate)) {
                VideoSize size = video.getSize();
                return new VideoSize(size.getHeight(), size.getWidth());
            }
        }
        return video.getSize();
    }
}
