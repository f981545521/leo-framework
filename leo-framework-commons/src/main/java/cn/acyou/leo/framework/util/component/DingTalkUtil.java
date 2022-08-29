package cn.acyou.leo.framework.util.component;

import cn.acyou.leo.framework.prop.DingTalkProperty;
import cn.acyou.leo.framework.prop.DingTalkRobot;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 钉钉消息发送
 * <p>
 * <a href="https://open.dingtalk.com/document/group/custom-robot-access">官方参考文档地址</a>
 *
 * @author youfang
 */
@Slf4j
public class DingTalkUtil {
    private final Map<String, DingTalkRobot> dingTalkRobotMap;

    public DingTalkUtil(DingTalkProperty dingTalkProperty) {
        dingTalkRobotMap = dingTalkProperty.getDingTalkRobotMap();
    }

    /**
     * 发送通知  当isAtAll=false时mobileList起效
     *
     * @param atMobileList 单独通知(@1,@2) ->手机号
     * @param content      消息内容
     */
    public void sendMessage(String groupId, List<String> atMobileList, String content) {
        DingTalkRobot dingTalkRobot = dingTalkRobotMap.get(groupId);
        if (dingTalkRobot == null) {
            log.warn("未找到机器人：{}", groupId);
            return;
        }
        //消息内容
        Map<String, String> contentMap = new LinkedHashMap<>();
        contentMap.put("content", content);
        //通知人
        Map<String, Object> atMap = new LinkedHashMap<>();
        //1.是否通知所有人 是否全员通知(@所有人)
        atMap.put("isAtAll", dingTalkRobot.getAtAll());
        //2.通知具体人的手机号码列表
        atMap.put("atMobiles", atMobileList);
        //请求参数
        Map<String, Object> reqMap = new LinkedHashMap<>();
        reqMap.put("msgtype", "text");
        reqMap.put("text", contentMap);
        reqMap.put("at", atMap);
        //url
        Long timestamp = System.currentTimeMillis();
        String url = dingTalkRobot.getRobotHookUrl() + "&timestamp=" + timestamp + "&sign=" + getSignature(dingTalkRobot.getRobotSecret(), timestamp);
        log.info("准备发送钉钉消息：{}", url);
        String res = HttpUtil.post(url, JSON.toJSONString(reqMap));
        log.info("发送钉钉消息结果:{}", res);
    }

    /**
     * 获取签名信息
     *
     * @return 签名
     */
    private String getSignature(String secret, Long timestamp) {
        String signa = "";
        try {
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            signa = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return signa;
    }


}
