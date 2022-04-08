package cn.acyou.leo.framework.push.umeng;

import cn.acyou.leo.framework.ClientEnum;
import cn.acyou.leo.framework.push.prop.UmengProperties;
import cn.acyou.leo.framework.push.umeng.android.AndroidCustomizedcast;
import cn.acyou.leo.framework.push.umeng.ios.IOSCustomizedcast;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.util.Map;

/**
 * 消息推送工具类
 */
@Slf4j
public class PushUtils {

    private static final PushClient client = new PushClient();

    private UmengProperties umengProperties;

    public PushUtils(UmengProperties umengProperties) {
        this.umengProperties = umengProperties;
        log.info("友盟推送 配置完成：{}", umengProperties);
    }

    /**
     * 推送消息
     *
     * @param param 消息体
     */
    public void sendCustomizedcast(CustomizedcastDTO param) {
        log.info("友盟推送参数：{}", JSON.toJSONString(param));
        ClientEnum deviceType = param.getDeviceType();
        CustomizedcastBo customizedcastBo = new CustomizedcastBo();
        customizedcastBo.setTicker(param.getTicker());
        customizedcastBo.setTitle(param.getTitle());
        customizedcastBo.setText(param.getText());
        customizedcastBo.setCustomField(param.getCustomField());
        customizedcastBo.setTest(umengProperties.isTest());
        customizedcastBo.setAliasType("userId");
        customizedcastBo.setAlias(String.valueOf(param.getUserId()));
        switch (deviceType) {
            case ANDROID:
                customizedcastBo.setAppkey(umengProperties.getAndroid().getAppkey());
                customizedcastBo.setAppMasterSecret(umengProperties.getAndroid().getAppMasterSecret());
                sendAndroidCustomizedcast(customizedcastBo);
                break;
            case IOS:
                customizedcastBo.setAppkey(umengProperties.getIos().getAppkey());
                customizedcastBo.setAppMasterSecret(umengProperties.getIos().getAppMasterSecret());
                sendIOSCustomizedcast(customizedcastBo);
                break;
            default:
        }
    }


    /**
     * 定制化推送信息给IOS用户
     *
     * @param vo
     */
    public static void sendIOSCustomizedcast(CustomizedcastBo vo) {
        try {
            IOSCustomizedcast customizedcast = new IOSCustomizedcast(
                    vo.getAppkey(), vo.getAppMasterSecret());
            customizedcast.setAlias(vo.getAlias(), vo.getAliasType());
            customizedcast.setAlert(vo.getTitle(), "", vo.getText());
            customizedcast.setBadge(0);
            customizedcast.setSound("default");
            //设置额外字段
            if (vo.getCustomField() != null && !vo.getCustomField().isEmpty()) {
                for (Map.Entry<String, String> entry : vo.getCustomField().entrySet()) {
                    customizedcast.setCustomizedField(entry.getKey(), entry.getValue());
                }
            }
            if (vo.isTest()) {
                customizedcast.setTestMode();
            } else {
                customizedcast.setProductionMode();
            }
            client.send(customizedcast);
            log.info("IOS 推送信息给IOS用户完成 userId:{}", vo.getUserId());
        } catch (Exception e) {
            log.error("IOS 推送信息给IOS用户异常，异常信息：{}", e.getMessage());
        }
    }


    /**
     * 定制化推送信息给安卓用户
     *
     * @param vo
     */
    public static void sendAndroidCustomizedcast(CustomizedcastBo vo) {
        try {
            AndroidCustomizedcast customizedcast =
                    new AndroidCustomizedcast(vo.getAppkey(), vo.getAppMasterSecret());
            customizedcast.setAlias(vo.getAlias(), vo.getAliasType());
            customizedcast.setTicker(vo.getTicker());
            customizedcast.setTitle(vo.getTitle());
            customizedcast.setText(vo.getText());
            customizedcast.goAppAfterOpen();
            customizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
            //设置额外字段
            if (vo.getCustomField() != null && !vo.getCustomField().isEmpty()) {
                JSONObject custom = new JSONObject();
                for (Map.Entry<String, String> entry : vo.getCustomField().entrySet()) {
                    custom.put(entry.getKey(), entry.getValue());
                }
                customizedcast.setCustomField(custom);
            }
            if (vo.isTest()) {
                customizedcast.setTestMode();
            } else {
                customizedcast.setProductionMode();
            }
            client.send(customizedcast);
            log.info("Android 推送信息给Android用户完成 userId:{}", vo.getUserId());
        } catch (Exception e) {
            log.error("Android 推送信息给Android用户异常，异常信息：{}", e.getMessage());
        }
    }

}
