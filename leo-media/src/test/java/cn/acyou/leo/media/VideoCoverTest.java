package cn.acyou.leo.media;

import cn.acyou.leo.media.encoder.MediaUtil;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/30 11:06]
 **/
public class VideoCoverTest {
    @Test
    public void test1() throws Exception {
        String vPath = "C:\\Users\\1\\Downloads\\1(2)(1)\\1-案例二.mp4";
        String v2Path = "C:\\Users\\1\\Downloads\\1(2)(1)\\1-案例二_cover.png";
        MediaUtil.instance().extractCover(new File(vPath), new File(v2Path), "0.01");
    }

    @Test
    public void test2345() {
        String s = MediaUtil.formatDuration(1 * 60 * 60 * 1000);
        System.out.println(s);
    }
}
