package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.media.encoder.MediaUtil;
import cn.acyou.leo.framework.media.util.ImageUtil;

import java.io.File;

/**
 * @author youfang
 * @version [1.0.0, 2024/9/20 14:23]
 **/
public class CompressTest1 {
    public static void main2(String[] args) throws Exception{
        ImageUtil.compressImage(new File("C:\\Users\\1\\Downloads\\知心姐姐.jpg"), new File("C:\\Users\\1\\Downloads\\知心姐姐_c2.jpg"));
    }
    public static void main(String[] args) throws Exception{
        MediaUtil.instance()
                .compressVideo("https://gy.cdn.guiji.cn/resources/v1/%E5%8E%86%E5%8F%B2%E5%90%8D%E4%BA%BA/%E5%AD%9F%E5%AD%90_demo.mp4",
                        "", "", "","",
                        "C:\\Users\\1\\Videos\\孟子_压缩1.mp4");

        MediaUtil.instance()
                .compressVideo("https://gy.cdn.guiji.cn/resources/v1/%E5%8E%86%E5%8F%B2%E5%90%8D%E4%BA%BA/%E5%AD%9F%E5%AD%90_demo.mp4",
                        new File("C:\\Users\\1\\Videos\\孟子_压缩2.mp4"), 1);
    }
}
