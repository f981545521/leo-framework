package cn.acyou.leo.framework.wx.api;

import cn.acyou.leo.framework.wx.dto.JsapiTicket;
import cn.acyou.leo.framework.wx.dto.WxAccessTokenResp;
import cn.acyou.leo.framework.wx.dto.WxConfig;
import cn.acyou.leo.framework.wx.dto.qy.*;
import cn.acyou.leo.framework.wx.exception.WxServiceException;
import cn.acyou.leo.framework.wx.prop.WxConfigProperties;
import cn.acyou.leo.framework.wx.utils.DigestUtils;
import cn.acyou.leo.framework.wx.utils.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 企业微信方法
 *
 * @author youfang
 * @version [1.0.0, 2020-7-9 下午 11:23]
 **/
@Slf4j
public class WxQyAppApi {

    private static WxConfigProperties.QyWx qyWxConfig;

    public WxQyAppApi(WxConfigProperties wxConfigProperties) {
        log.info("WxQyAppUtil 初始化。");
        WxQyAppApi.qyWxConfig = wxConfigProperties.getQyWx();
    }

    private static final String ACCESS_TOKEN_REDIS_KEY = "WX:ACCESS:TOKEN:QyWxAppUtil";
    private static final String JSAPI_TICKET_APP = "WX:ACCESS:TOKEN:QyWxAppUtil_JSAPITicket_APP";
    private static final String JSAPI_TICKET_QY = "WX:ACCESS:TOKEN:QyWxAppUtil_JSAPITicket_QY";

    /**
     * 获取企业用户详情
     * <p>
     * 60111  -> userid not found
     *
     * @param userId 用户id
     * @return {@link QyUserDetailResp}
     */
    public QyUserDetailResp getQyUser(String userId) {
        String s = WxCommonCachePool.getAndCache("WxQyApp:getQyUser:" + userId, (k) -> {
            log.info("[企业微信APP] 获取企业成员详情：" + userId);
            String post = HttpUtil.get("https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=" + getAccessToken() + "&userid=" + userId);
            log.info("[企业微信APP] 获取企业成员详情结果：" + post);
            return post;
        });
        return JSON.parseObject(s, QyUserDetailResp.class);
    }

    /**
     * 获取客户详情
     * <p>
     * （企业需要使用系统应用“客户联系”或配置到“可调用应用”列表中的自建应用的secret所获取的accesstoken来调用）
     * 40096   ->    invalid external userid
     *
     * @param external_userid 外部用户标识
     * @return {@link ExternalContactDetailResp}
     */
    public ExternalContactDetailResp getExternalContactDetail(String external_userid) {
        String s = WxCommonCachePool.getAndCache("WxQyApp:getExternalContactDetail:" + external_userid, (k) -> {
            log.info("[企业微信APP] 获取客户详情：" + external_userid);
            String post = HttpUtil.post("https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get?access_token=" + getAccessToken() + "&external_userid=" + external_userid);
            log.info("[企业微信APP] 获取客户详情结果：" + post);
            return post;
        });
        return JSON.parseObject(s, ExternalContactDetailResp.class);
    }


    /**
     * 获取客户群列表
     * （超过的1000条需要使用cursor查询，以后再说。）
     * <p></p>
     * <a href="https://developer.work.weixin.qq.com/document/path/92120">官方文档</a>
     *
     * @param owner 群主
     * @return {@link ExternalGroupListResp}
     */
    public ExternalGroupListResp getGroupChatListByOwner(String owner) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status_filter", 0);
        JSONObject owner_filter_json = new JSONObject();
        owner_filter_json.put("userid_list", new JSONArray(Arrays.asList(owner)));
        jsonObject.put("owner_filter", owner_filter_json);
        jsonObject.put("cursor", "");
        jsonObject.put("limit", 1000);
        log.info("[企业微信APP] 获取{}的客户群列表。", owner);
        String res = HttpUtil.postJson("https://qyapi.weixin.qq.com/cgi-bin/externalcontact/groupchat/list?access_token=" + getAccessToken(), jsonObject.toJSONString());
        log.info("[企业微信APP] 获取{}的客户群列表 完成：{}。", owner, res);
        ExternalGroupListResp externalGroupListResp = JSON.parseObject(res, ExternalGroupListResp.class);
        if (externalGroupListResp.getErrcode() != null && externalGroupListResp.getErrcode() == 0) {
            List<ExternalGroupListResp.GroupChatList> group_chat_list = externalGroupListResp.getGroup_chat_list();
            if (!CollectionUtils.isEmpty(group_chat_list)) {
                for (ExternalGroupListResp.GroupChatList groupChatList : group_chat_list) {
                    groupChatList.setChat_name(getExternalGroupChatDetail(groupChatList.getChat_id()).getGroup_chat().getName());
                }
            }
        }
        return externalGroupListResp;
    }


    /**
     * 获取客户群详情（企业需要使用系统应用“客户联系”或配置到“可调用应用”列表中的自建应用的secret所获取的accesstoken来调用）
     *
     * @param chat_id 客户群ID
     * @return {@link ExternalGroupDetailResp}
     */
    public ExternalGroupDetailResp getExternalGroupChatDetail(String chat_id) {
        String s = WxCommonCachePool.getAndCache("WxQyApp:getExternalGroupChatDetail:" + chat_id, (k) -> {
            JSONObject paramObject = new JSONObject();
            paramObject.put("chat_id", chat_id);
            paramObject.put("need_name", 1);
            log.info("[企业微信APP] 获取客户群详情：" + paramObject.toJSONString());
            String url = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/groupchat/get?access_token=" + getAccessToken();
            String res = HttpUtil.postJson(url, paramObject.toJSONString());
            log.info("[企业微信APP] 获取客户群详情结果：" + res);
            return res;
        });
        return JSON.parseObject(s, ExternalGroupDetailResp.class);
    }


    /**
     * 企业微信JSSDK配置
     *
     * <p>官方文档：</p>
     * https://developer.work.weixin.qq.com/document/path/90546
     *
     * @param url url
     * @return 企业微信JSSDK配置
     */
    public QyWxAgentConfig getQyWxAgentConfig(String url) {
        if (url == null || url.trim().length() == 0) {
            throw new WxServiceException("[企业微信] 获取AgentConfig，URL:[{}]不能为空！", url);
        }
        QyWxAgentConfig qyWxAgentConfig = new QyWxAgentConfig();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String nonceStr = UUID.randomUUID().toString().trim().replaceAll("-", ""); // 必填，生成签名的随机串
        String jsapiTicket = getAppJsapiTicket();
        String signature;
        String encryption = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
        signature = DigestUtils.sha1DigestAsHex(encryption);
        qyWxAgentConfig.setCorpId(WxQyAppApi.qyWxConfig.getCorpId());
        qyWxAgentConfig.setAgentId(WxQyAppApi.qyWxConfig.getAppAgentId());
        qyWxAgentConfig.setNonceStr(nonceStr);
        qyWxAgentConfig.setTimestamp(timestamp);
        qyWxAgentConfig.setSignature(signature);
        log.info("[企业微信] 获取AgentConfig 的配置|：" + qyWxAgentConfig);
        return qyWxAgentConfig;
    }

    /**
     * 获取Config (agentConfig前调用)
     * <p>
     * https://developer.work.weixin.qq.com/document/path/90514
     *
     * @param url url
     * @return {@link WxConfig}
     */
    public WxConfig getWxQyConfig(String url) {
        if (url == null || url.trim().length() == 0) {
            throw new WxServiceException("[企业微信] 获取Config，URL:[{}]不能为空！", url);
        }
        WxConfig wxConfig = new WxConfig();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String nonceStr = UUID.randomUUID().toString().trim().replaceAll("-", ""); // 必填，生成签名的随机串
        String jsapiTicket = getCorpJsapiTicket();
        String signature;
        String encryption = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
        signature = DigestUtils.sha1DigestAsHex(encryption);
        wxConfig.setAppId(WxQyAppApi.qyWxConfig.getCorpId());
        wxConfig.setNonceStr(nonceStr);
        wxConfig.setTimestamp(timestamp);
        wxConfig.setSignature(signature);
        log.info("[企业微信] 获取 Config 的配置|：" + wxConfig);
        return wxConfig;
    }

    /**
     * 获取应用的jsapi_ticket
     * <p>
     * https://developer.work.weixin.qq.com/document/path/90506#%E8%8E%B7%E5%8F%96%E5%BA%94%E7%94%A8%E7%9A%84jsapi-ticket
     *
     * @return jsapi_ticket
     */
    private String getAppJsapiTicket() {
        //需要加缓存将ticket缓存起来
        return WxCommonCachePool.getAndCache(JSAPI_TICKET_APP, cacheKey -> {
            String apiUrl = "https://qyapi.weixin.qq.com/cgi-bin/ticket/get?access_token=" + getAccessToken() + "&type=agent_config";
            String responseStr = HttpUtil.get(apiUrl);
            log.info("[企业微信] 获取 app jsapi_ticket|(jsapiTicket) : {}", responseStr);
            JsapiTicket responseTicket = JSON.parseObject(responseStr, JsapiTicket.class);
            if (responseTicket.getTicket() == null || responseTicket.getTicket().length() == 0) {
                throw new WxServiceException("[企业微信] 获取 app jsapi_ticket|失败！");
            }
            return responseTicket.getTicket();
        });
    }

    /**
     * 获取企业的jsapi_ticket
     * <p>
     * https://developer.work.weixin.qq.com/document/path/90506#%E8%8E%B7%E5%8F%96%E4%BC%81%E4%B8%9A%E7%9A%84jsapi-ticket
     *
     * @return jsapi_ticket
     */
    private String getCorpJsapiTicket() {
        //需要加缓存将ticket缓存起来
        return WxCommonCachePool.getAndCache(JSAPI_TICKET_QY, cacheKey -> {
            String apiUrl = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=" + getAccessToken();
            String responseStr = HttpUtil.get(apiUrl);
            log.info("[企业微信] 获取 corp jsapi_ticket|(jsapiTicket) : {}", responseStr);
            JsapiTicket responseTicket = JSON.parseObject(responseStr, JsapiTicket.class);
            if (responseTicket.getTicket() == null || responseTicket.getTicket().length() == 0) {
                throw new WxServiceException("[企业微信] 获取 corp jsapi_ticket|失败！");
            }
            return responseTicket.getTicket();
        });
    }

    /**
     * 获取用户的会话密钥
     * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html
     *
     * @param code 代码
     * @return session_key
     */
    public QyWxCode2SessionResp getUserSessionKey(String code) {
        String code2SessionUrl = "https://qyapi.weixin.qq.com/cgi-bin/miniprogram/jscode2session?" +
                "access_token=" + getAccessToken() + "&" +
                "js_code=" + code + "&" +
                "grant_type=authorization_code";
        String code2SessionUrlResult = HttpUtil.get(code2SessionUrl);
        QyWxCode2SessionResp wxCode2SessionVo = JSON.parseObject(code2SessionUrlResult, QyWxCode2SessionResp.class);
        if (wxCode2SessionVo.getErrcode() != 0) {
            throw new WxServiceException(wxCode2SessionVo.getErrmsg());
        }
        return wxCode2SessionVo;
    }

    /**
     * 获取接口凭证
     *
     * @return accessToken
     */
    public String getAccessToken() {
        return WxCommonCachePool.getAndCache(ACCESS_TOKEN_REDIS_KEY, s -> {
            Map<String, String> tokenParams = new HashMap<>();
            tokenParams.put("corpid", qyWxConfig.getCorpId());
            tokenParams.put("corpsecret", qyWxConfig.getAppSecret());
            String getResult = HttpUtil.get("https://qyapi.weixin.qq.com/cgi-bin/gettoken", tokenParams, null);
            WxAccessTokenResp wxAccessTokenResp = JSON.parseObject(getResult, WxAccessTokenResp.class);
            if (wxAccessTokenResp.getErrcode() != 0) {
                throw new WxServiceException(wxAccessTokenResp.getErrmsg());
            }
            return wxAccessTokenResp.getAccess_token();
        });
    }

}
