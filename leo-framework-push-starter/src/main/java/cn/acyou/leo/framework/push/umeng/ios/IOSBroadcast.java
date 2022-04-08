package cn.acyou.leo.framework.push.umeng.ios;

import cn.acyou.leo.framework.push.umeng.IOSNotification;

public class IOSBroadcast extends IOSNotification {
    public IOSBroadcast(String appkey, String appMasterSecret) throws Exception {
        setAppMasterSecret(appMasterSecret);
        setPredefinedKeyValue("appkey", appkey);
        this.setPredefinedKeyValue("type", "broadcast");
    }
}
