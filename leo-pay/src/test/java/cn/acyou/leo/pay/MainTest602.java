package cn.acyou.leo.pay;


import com.google.common.collect.Maps;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.WxPayKit;

import java.util.HashMap;

/**
 * @author youfang
 * @version [1.0.0, 2025/7/14 11:18]
 **/
public class MainTest602 {
    public static void main(String[] args) {
        HashMap<String, String> params = Maps.newHashMap();
        params.put("timestamp", System.currentTimeMillis() + "");
        params.put("app_id", "1");
        params.put("app_request_id", "2");
        params.put("sig", "3");
        String linkString = PayKit.createLinkString(params);
        System.out.println(linkString);
        String sign = WxPayKit.createSign(params, "4");
        System.out.println(sign);
    }
}
