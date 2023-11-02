package cn.acyou.leo.framework.media.dto;

import lombok.Data;
import ws.schild.jave.info.AudioInfo;
import ws.schild.jave.info.VideoInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2022/12/20 10:22]
 **/
@Data
public class VideoInfoExt {

    /**
     * 格式
     */
    private String format = null;

    /**
     * 元数据
     */
    private Map<String, String> metadata = new HashMap<>();

    /**
     * 视频时长   13010
     */
    private long duration = -1;

    /**
     * 视频时长   00:00:13.010
     */
    private String durationStr = "";

    /**
     * 音频信息
     */
    private AudioInfo audio = null;

    /**
     * 文件大小：2110870
     */
    private long fileSize = 0;

    /**
     * 文件大小： 2.0 MB
     */
    private String fileSizeStr = "";

    /**
     * 视频信息
     */
    private VideoInfo video = null;

    /**
     * 分辨率  1920x1080
     */
    private String resolutionRatio;

    /**
     * 长宽比  16:9
     */
    private String resolution;

    /**
     * 帧率   29.97
     */
    private String frameRate;

    /**
     * 码率   1M
     */
    private String bitRate;
}
