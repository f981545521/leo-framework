package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.util.HttpUtil;
import cn.acyou.leo.framework.util.Md5Util;
import cn.acyou.leo.framework.util.SHAUtil;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author youfang
 * @version [1.0.0, 2023/6/13 18:14]
 **/
public class MainTest3 {
    public static void main(String[] args) throws Exception {
        //File file = new File("D:\\temp\\video\\公司抖音作品列表.txt");
        //List<String> content = FileUtil.readLines(file, StandardCharsets.UTF_8);
        //PrintWriter printWriter = FileUtil.getPrintWriter("D:\\temp\\video\\公司抖音作品列表2.txt", StandardCharsets.UTF_8, true);
        //System.out.println("ok");
        //a8e16a9711205c1e445c295f03af7e540a1bc0d924d29587039554d5b72bf3d3
        String key = "9bf3d247bc517a054964872cdc3bea85:3173a0ef88db4999eebbc6ed4fbe0a45";
        System.out.println(SHAUtil.getSHA256(key));
        System.out.println(DigestUtils.sha256Hex(key));
        System.out.println("a8e16a9711205c1e445c295f03af7e540a1bc0d924d29587039554d5b72bf3d3");
        System.out.println(DigestUtils.sha1Hex(key));
        System.out.println(SHAUtil.getSHA1(key));
        System.out.println(org.springframework.data.redis.core.script.DigestUtils.sha1DigestAsHex(key));
        System.out.println("==============================================");
        System.out.println(DigestUtils.md5Hex(key));
        System.out.println(SHAUtil.getMD5(key));
        System.out.println(Md5Util.md5(key));

        System.out.println(HttpUtil.getContentLength("https://prevshow.guiji.ai/nfs/tici/digitalLife/4572-1-20230704110106459.wav"));
        System.out.println(HttpUtil.getContentLength("https://prevshow.guiji.ai/nfs/tici/digitalLife/4572-1-20230704173431385.wav"));

        System.out.println(SHAUtil.getSHA1("abc", "121343264815", "jkl"));

    }
}
