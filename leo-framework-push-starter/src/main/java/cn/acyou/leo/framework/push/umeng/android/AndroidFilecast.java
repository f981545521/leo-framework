package cn.acyou.leo.framework.push.umeng.android;

import cn.acyou.leo.framework.push.umeng.AndroidNotification;

public class AndroidFilecast extends AndroidNotification {
    public AndroidFilecast(String appkey, String appMasterSecret) throws Exception {
        setAppMasterSecret(appMasterSecret);
        setPredefinedKeyValue("appkey", appkey);
        this.setPredefinedKeyValue("type", "filecast");
    }

    public void setFileId(String fileId) throws Exception {
        setPredefinedKeyValue("file_id", fileId);
    }
}