package cn.acyou.leo.framework.push.umeng.android;

import cn.acyou.leo.framework.push.umeng.AndroidNotification;

public class AndroidBroadcast extends AndroidNotification {
    public AndroidBroadcast(String appkey, String appMasterSecret) throws Exception {
        setAppMasterSecret(appMasterSecret);
        setPredefinedKeyValue("appkey", appkey);
        this.setPredefinedKeyValue("type", "broadcast");
    }
}
