import cn.acyou.leo.framework.obs.OBSUtil;
import cn.acyou.leo.framework.prop.OBSProperty;

import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/26 13:41]
 **/
public class ObsUtilTest {
    public static void main(String[] args) {
        OBSProperty obsProperty = new OBSProperty();
        obsProperty.setEndpoint("http://obs.cn-east-3.myhuaweicloud.com");
        obsProperty.setAccessKeyId("*");
        obsProperty.setAccessKeySecret("*");
        OBSUtil obsUtil = new OBSUtil(obsProperty);
        List<String> strings = obsUtil.listBuckets();
        System.out.println(strings);
    }
}
