package cn.acyou.leo.media.encoder;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileSystemUtils;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

import java.io.File;
import java.io.IOException;
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

    private ExecProcess execProcess;

    public static MediaUtil instance() {
        return new MediaUtil();
    }

    public static MediaUtil instance(ExecProcess execProcess) {
        MediaUtil mediaUtil = new MediaUtil();
        mediaUtil.setExecProcess(execProcess);
        return mediaUtil;
    }

    private void setExecProcess(ExecProcess execProcess) {
        this.execProcess = execProcess;
    }

    /**
     * 执行FFMPEG命令
     *
     * @param command 命令
     */
    private void exec(String... command) {
        String[] args = ArrayUtils.addFirst(command, ffmpegLocator.getExecutablePath());
        log.info("执行FFMPEG 命令:{}", StringUtils.join(args, " "));
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
            execProcess.progress(1000);
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
    public void cutAudio(String source, long ss, long to, String targetPath) {
        boolean mkdirs = new File(targetPath).getParentFile().mkdirs();
        log.info("剪切音频文件 params:[source:{}, ss:{}, to:{}, target:{}] 目标目录：{}", source, ss, to, targetPath, (mkdirs ? "创建成功" : "无需创建"));
        MediaUtil.instance().exec("-y", "-i", source, "-ss", formatDuring(ss), "-to", formatDuring(to), "-c", "copy", targetPath);
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
        MediaUtil.instance().exec("-i", audioSource, "-i", videoSource, "-vcodec", "copy", "-acodec", "copy", targetPath);
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
    public String formatDuring(long mss) {
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
    public void extractFrame(String i, String ss, String targetPath) {
        boolean mkdirs = new File(targetPath).getParentFile().mkdirs();
        log.info("提取帧 params:[i:{}, ss:{}, target:{}] 目标目录：{}", i, ss, targetPath, (mkdirs ? "创建成功" : "无需创建"));
        MediaUtil.instance().exec("-i", i, "-ss", ss, "-f", "image2", targetPath);
    }

    /**
     * 提取帧
     *
     * @param url        url
     * @param speedRatio 进度比例
     * @param targetDir  目标目录 无"/"结尾 。可以直接使用 getAbsolutePath()
     * @return {@link String[]} 目标文件路径列表
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
            MediaUtil.instance().extractFrame(url, new BigDecimal(t).multiply(new BigDecimal("0.001")).toString(), targetPath);
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
        MediaUtil.instance().exec(command.toArray(new String[0]));
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
                MediaUtil.instance().exec("-i", urls.get(i), "-c", "copy", "-bsf:v", "h264_mp4toannexb", "-f", "mpegts", tempTs.getAbsolutePath());
                tsList.add(tempTs.getAbsolutePath());
            }
            MediaUtil.instance().exec("-i", "concat:" + StringUtils.join(tsList, "|"), "-c", "copy", "-bsf:a", "aac_adtstoasc", "-movflags", "+faststart", targetPath);
        } finally {
            FileSystemUtils.deleteRecursively(tempDir);
        }
    }


    public static void main2(String[] args) {
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

    public static void main22(String[] args) {
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


    public static void main(String[] args) {
        //拆分视频序列帧
        //-i https://guiyu-tici.oss-cn-shanghai.aliyuncs.com/tici/643-1-202204221554549.MOV -vcodec h264 -y D:\ToUpload\4\T1.mp4
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
