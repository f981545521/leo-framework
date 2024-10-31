package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.media.encoder.MediaUtil;
import cn.acyou.leo.framework.media.util.ImageUtil;
import org.assertj.core.util.Lists;

import java.io.File;

/**
 * @author youfang
 * @version [1.0.0, 2024/9/20 14:23]
 **/
public class CompressTest1 {
    public static void main(String[] args) throws Exception{
        String s = "https://digital-public-dev.obs.cn-east-3.myhuaweicloud.com/vpp/vpp/2024/10/30/1168845027959513088_ff_1.mp4\n" +
                "https://digital-public-dev.obs.cn-east-3.myhuaweicloud.com/vpp/vpp/2024/10/30/1168845027682689024_ff_1.mp4\n" +
                "https://digital-public-dev.obs.cn-east-3.myhuaweicloud.com/vpp/vpp/2024/10/30/1168845027498139648_ff_1.mp4\n" +
                "https://digital-public-dev.obs.cn-east-3.myhuaweicloud.com/vpp/vpp/2024/10/30/1168845026927714304_ff_1.mp4\n" +
                "https://digital-public-dev.obs.cn-east-3.myhuaweicloud.com/vpp/vpp/2024/10/30/1168845026420203520_ff_1.mp4\n" +
                "https://digital-public-dev.obs.cn-east-3.myhuaweicloud.com/vpp/vpp/2024/10/30/1168845026214682624_ff_1.mp4\n" +
                "https://digital-public-dev.obs.cn-east-3.myhuaweicloud.com/vpp/vpp/2024/10/30/1168845026000773120_ff_1.mp4\n" +
                "https://digital-public-dev.obs.cn-east-3.myhuaweicloud.com/vpp/vpp/2024/10/30/1168845025640062976_ff_1.mp4\n" +
                "https://digital-public-dev.obs.cn-east-3.myhuaweicloud.com/vpp/vpp/2024/10/30/1168845025451319296_ff_1.mp4\n" +
                "https://digital-public-dev.obs.cn-east-3.myhuaweicloud.com/vpp/vpp/2024/10/30/1168845025166106624_ff_1.mp4\n" +
                "https://digital-public-dev.obs.cn-east-3.myhuaweicloud.com/vpp/vpp/2024/10/30/1168845024532766720_ff_1.mp4\n" +
                "https://digital-public-dev.obs.cn-east-3.myhuaweicloud.com/vpp/vpp/2024/10/30/1168845024050421760_ff_1.mp4\n" +
                "https://digital-public-dev.obs.cn-east-3.myhuaweicloud.com/vpp/vpp/2024/10/30/1168845023047983104_ff_1.mp4\n" +
                "https://digital-public-dev.obs.cn-east-3.myhuaweicloud.com/vpp/vpp/2024/10/30/1168845022544666624_ff_1.mp4\n" +
                "https://digital-public-dev.obs.cn-east-3.myhuaweicloud.com/vpp/vpp/2024/10/30/1168845021022134272_ff_1.mp4";
        String[] split = s.split("\n");
        MediaUtil.instance().concatVideo(Lists.newArrayList(split), "C:\\Users\\1\\Videos\\1.mp4");
        MediaUtil.instance().concatVideo2(Lists.newArrayList(split), "C:\\Users\\1\\Videos\\2.mp4");
    }
    public static void main2(String[] args) throws Exception{
        ImageUtil.compressImage(new File("C:\\Users\\1\\Downloads\\知心姐姐.jpg"), new File("C:\\Users\\1\\Downloads\\知心姐姐_c2.jpg"));
    }
    public static void main3(String[] args) throws Exception{
        MediaUtil.instance()
                .compressVideo("https://gy.cdn.guiji.cn/resources/v1/%E5%8E%86%E5%8F%B2%E5%90%8D%E4%BA%BA/%E5%AD%9F%E5%AD%90_demo.mp4",
                        "", "", "","",
                        "C:\\Users\\1\\Videos\\孟子_压缩1.mp4");

        MediaUtil.instance()
                .compressVideo("https://gy.cdn.guiji.cn/resources/v1/%E5%8E%86%E5%8F%B2%E5%90%8D%E4%BA%BA/%E5%AD%9F%E5%AD%90_demo.mp4",
                        new File("C:\\Users\\1\\Videos\\孟子_压缩2.mp4"), 1);
    }
}
