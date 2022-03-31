package cn.acyou.leo.framework.wx.api;

import cn.acyou.leo.framework.wx.dto.qy.ExternalContactDetailResp;
import cn.acyou.leo.framework.wx.dto.qy.ExternalGroupDetailResp;
import cn.acyou.leo.framework.wx.dto.qy.QyUserDetailResp;
import cn.acyou.leo.framework.wx.dto.qy.QyWxArchiveVo;
import cn.acyou.leo.framework.wx.prop.WxConfigProperties;
import cn.acyou.leo.framework.wx.utils.RSAUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.wework.Finance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 企业微信会话内容存档API
 *
 * @author youfang
 * @version [1.0.0, 2022/1/14 13:42]
 **/
@Slf4j
public class WxQyArchiveApi {
    private static WxConfigProperties.QyWx qyWxConfig;

    private final WxQyAppApi wxQyAppApi;

    private static long sdk;

    public WxQyArchiveApi(WxConfigProperties wxConfigProperties, WxQyAppApi wxQyAppApi) {
        log.info("WxQyArchiveApi 初始化。");
        WxQyArchiveApi.qyWxConfig = wxConfigProperties.getQyWx();
        this.wxQyAppApi = wxQyAppApi;
        WxQyArchiveApi.sdk = Finance.NewSdk();
        int ret = Finance.Init(sdk, qyWxConfig.getCorpId(), qyWxConfig.getArchiveSecret());
        if (ret != 0) {
            Finance.DestroySdk(sdk);
            System.out.println("WxQyArchiveApi 初始化sdk失败： " + ret);
        } else {
            log.info("WxQyArchiveApi 初始化sdk成功");
        }
    }

    /**
     * 拉取存档的消息
     *
     * @param seq   seq
     * @param limit 限制
     * @return {@link List}<{@link QyWxArchiveVo}>
     * @throws Exception 异常
     */
    public List<QyWxArchiveVo> pullMessages(long seq, int limit) throws Exception {
        List<QyWxArchiveVo> qyWxArchiveVoList = new ArrayList<>();
        String proxy = "";
        String passwd = "";
        int timeout = 10000;
        //每次使用GetChatData拉取存档前需要调用NewSlice获取一个slice，在使用完slice中数据后，还需要调用FreeSlice释放。
        long slice = Finance.NewSlice();
        int ret = Finance.GetChatData(sdk, seq, limit, proxy, passwd, timeout, slice);
        if (ret != 0) {
            log.error("[企业微信会话存档] 获取会话内容失败 ret " + ret);
            Finance.FreeSlice(slice);
        }
        String s = Finance.GetContentFromSlice(slice);
        Finance.FreeSlice(slice);
        log.info("[企业微信会话存档] 获取会话内容成功 :" + s);
        JSONObject jsonObject = JSON.parseObject(s);
        Integer errcode = jsonObject.getInteger("errcode");
        if (errcode != null && errcode == 0) {
            JSONArray chatdata = jsonObject.getJSONArray("chatdata");
            for (int i = 0; i < chatdata.size(); i++) {
                long chatdataSeq = chatdata.getJSONObject(i).getLong("seq");
                String msgid = chatdata.getJSONObject(i).getString("msgid");
                //消息的公钥版本，此处要用对应的版本去解密
                Integer publickey_ver = chatdata.getJSONObject(i).getInteger("publickey_ver");
                String encrypt_random_key = chatdata.getJSONObject(i).getString("encrypt_random_key");
                String encrypt_chat_msg = chatdata.getJSONObject(i).getString("encrypt_chat_msg");
                if (!Objects.equals(publickey_ver, qyWxConfig.getArchivePrivateKeyVersion())) {
                    log.warn("公钥版本不一致，消息被丢弃！当前：{} 已配置的：{}", publickey_ver, qyWxConfig.getArchivePrivateKeyVersion());
                    continue;
                }
                String encrypt_key = RSAUtils.decrypt(encrypt_random_key, qyWxConfig.getArchivePrivateKey());
                String realMsg = decryptData(encrypt_key, encrypt_chat_msg);
                if (StringUtils.hasText(realMsg)) {
                    JSONObject realMsgJsonObject = JSON.parseObject(realMsg);
                    String realMsgId = realMsgJsonObject.getString("msgid");
                    String action = realMsgJsonObject.getString("action");
                    String from = realMsgJsonObject.getString("from");
                    String tolist = realMsgJsonObject.getString("tolist");
                    String roomid = realMsgJsonObject.getString("roomid");
                    String msgtime = realMsgJsonObject.getString("msgtime");
                    String msgtype = realMsgJsonObject.getString("msgtype");
                    String content = realMsgJsonObject.getString(msgtype);
                    QyWxArchiveVo build = QyWxArchiveVo.builder()
                            .seq(chatdataSeq)
                            .msgid(msgid)
                            .from(from)
                            .from_name(getFromName(from))
                            .action(action)
                            .msgtype(msgtype)
                            .tolist(tolist)
                            .roomid(roomid)
                            .room_name(getRoomName(roomid))
                            .msgtime(msgtime)
                            .msgcontent(content)
                            .build();
                    qyWxArchiveVoList.add(build);
                }
            }
        }
        return qyWxArchiveVoList;
    }


    /**
     * 获取客户群ID
     *
     * @param roomid roomid
     * @return {@link String}
     */
    private String getRoomName(String roomid) {
        ExternalGroupDetailResp externalGroupChatDetail = wxQyAppApi.getExternalGroupChatDetail(roomid);
        if (externalGroupChatDetail.getErrcode() == 0) {
            return externalGroupChatDetail.getGroup_chat().getName();
        }
        return roomid;
    }

    /**
     * 获取用户名 企业内部用户/企业外部用户
     *
     * @param user 用户
     * @return {@link String}
     */
    private String getFromName(String user) {
        //企业内部用户
        QyUserDetailResp qyUser = wxQyAppApi.getQyUser(user);
        if (qyUser.getErrcode() == 0) {
            return qyUser.getName();
        }
        //外部用户
        ExternalContactDetailResp externalContactDetail = wxQyAppApi.getExternalContactDetail(user);
        if (externalContactDetail.getErrcode() == 0) {
            return externalContactDetail.getExternal_contact().getName();
        }
        return user;
    }

    /**
     * 解密数据
     *
     * @param encrypt_key      加密密钥
     * @param encrypt_chat_msg 加密的聊天消息
     * @return {@link String}
     */
    public String decryptData(String encrypt_key, String encrypt_chat_msg) {
        //每次使用DecryptData解密会话存档前需要调用NewSlice获取一个slice，在使用完slice中数据后，还需要调用FreeSlice释放。
        long msg = Finance.NewSlice();
        int ret = Finance.DecryptData(sdk, encrypt_key, encrypt_chat_msg, msg);
        if (ret != 0) {
            log.error("[企业微信会话存档] 解密消息失败： " + ret);
            Finance.FreeSlice(msg);
            return "";
        }
        String content = Finance.GetContentFromSlice(msg);
        log.info("[企业微信会话存档] 解密消息成功 ret:{} msg:{} ", ret, content);
        Finance.FreeSlice(msg);
        return content;
    }


    @PreDestroy
    public void destroy() {
        Finance.DestroySdk(sdk);
    }
}
