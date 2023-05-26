package cn.acyou.leo.framework.wx.api;

import cn.acyou.leo.framework.wx.dto.*;
import cn.acyou.leo.framework.wx.dto.mp.WxMpTagUsers;
import cn.acyou.leo.framework.wx.dto.mp.WxMpTags;
import cn.acyou.leo.framework.wx.dto.mp.WxMpUsers;
import cn.acyou.leo.framework.wx.exception.WxServiceException;
import cn.acyou.leo.framework.wx.prop.WxConfigProperties;
import cn.acyou.leo.framework.wx.utils.DigestUtils;
import cn.acyou.leo.framework.wx.utils.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 微信公众号
 *
 * @author youfang
 * @version [1.0.0, 2021/1/25]
 **/
@Slf4j
public class WxMpApi {

    private static WxConfigProperties.Mp mpConfig;

    public WxMpApi(WxConfigProperties wxConfigProperties) {
        log.info("WxMpUtil初始化。");
        WxMpApi.mpConfig = wxConfigProperties.getMp();
    }

    /**
     * 微信 ACCESS_TOKEN 缓存
     */
    private static final String WX_MP_ACCESS_TOKEN = "wxCache:wxMP:ACCESS_TOKEN";

    /**
     * 微信JSAPI Ticket 缓存
     */
    private static final String WX_MP_JSAPI_TICKET = "wxCache:wxMP:wxTicket";

    /**
     * 公众号
     * 获取微信公众号 JSAPI 的配置
     *
     * @param url url
     * @return 公众号的JSSDK配置
     */
    public WxConfig getWxConfig(String url) {
        if (url == null || url.trim().length() == 0) {
            throw new WxServiceException("[微信公众号] 获取JSAPI的配置，URL:[{}]不能为空！", url);
        }
        WxConfig wxConfig = new WxConfig();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String nonceStr = UUID.randomUUID().toString().trim().replaceAll("-", ""); // 必填，生成签名的随机串
        String jsapiTicket = getJsapiTicket();
        String signature;
        String encryption = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
        signature = DigestUtils.sha1DigestAsHex(encryption);
        wxConfig.setAppId(mpConfig.getAppid());
        wxConfig.setNonceStr(nonceStr);
        wxConfig.setTimestamp(timestamp);
        wxConfig.setSignature(signature);
        log.info("[微信公众号] 获取 JSAPI 的配置|：" + wxConfig);
        return wxConfig;
    }


    /**
     * 获取微信重定向地址
     * 第一步： 引导用户进入授权页面同意授权，获取code（用户同意授权，获取code）
     *
     * @param wxCallback wx回调
     * @return {@link String}
     */
    public String getWxRedirectUrl(String wxCallback) {
        if (!StringUtils.hasText(wxCallback)) {
            throw new WxServiceException("[微信公众号] 获取微信重定向地址，回调URL:[{}]不能为空！", wxCallback);
        }
        String url = null;
        try {
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + mpConfig.getAppid()
                    + "&redirect_uri=" + URLEncoder.encode(wxCallback, "UTF-8")
                    + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        log.info("[微信公众号] 获取微信重定向地址|(url)：" + url);
        return url;
    }

    /**
     * 获取jsapi_ticket
     *
     * @return jsapi_ticket
     */
    private String getJsapiTicket() {
        //需要加缓存将ticket缓存起来
        return WxCommonCachePool.getAndCache(WX_MP_JSAPI_TICKET, cacheKey -> {
            //jsapi_ticket是公众号用于调用微信JS接口的临时票据。正常情况下，jsapi_ticket的有效期为7200秒，通过access_token来获取。
            //由于获取jsapi_ticket的api调用次数非常有限，频繁刷新jsapi_ticket会导致api调用受限，影响自身业务，开发者必须在自己的服务全局缓存jsapi_ticket 。
            String apiUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + getAccessToken() + "&type=jsapi";
            String responseStr = HttpUtil.get(apiUrl);
            log.info("[微信公众号] 获取jsapi_ticket|(jsapiTicket) : {}", responseStr);
            JsapiTicket responseTicket = JSON.parseObject(responseStr, JsapiTicket.class);
            if (responseTicket.getTicket() == null || responseTicket.getTicket().length() == 0) {
                throw new WxServiceException("[微信公众号] 获取jsapi_ticket|失败！");
            }
            return responseTicket.getTicket();
        });
    }

    /**
     * 公众号 网页授权
     * <p>
     * 第二步：通过code换取网页授权access_token
     *
     * @param code 代码
     * @return {@link WxMpWebAccessTokenResp}
     */
    public WxMpWebAccessTokenResp getWebAuthAccessTokenByCode(String code) {
        log.info("[微信公众号] 网页授权登录|(code)：" + code);
        if (!StringUtils.hasText(code)) {
            throw new WxServiceException("[微信公众号] 网页授权登录|code:[{}]不能为空！", code);
        }
        Map<String, String> tokenParams = new HashMap<>();
        tokenParams.put("appid", mpConfig.getAppid());
        tokenParams.put("secret", mpConfig.getAppsecret());
        tokenParams.put("code", code);
        tokenParams.put("grant_type", "authorization_code");
        String tokenResult = HttpUtil.get("https://api.weixin.qq.com/sns/oauth2/access_token", tokenParams, null);
        log.info("[微信公众号] 网页授权登录|(获取access_token)：" + tokenResult);
        WxMpWebAccessTokenResp wxMpWebAccessTokenResp = JSON.parseObject(tokenResult, WxMpWebAccessTokenResp.class);
        if (wxMpWebAccessTokenResp.getErrcode() != 0) {
            throw new WxServiceException(wxMpWebAccessTokenResp.getErrmsg());
        }
        return wxMpWebAccessTokenResp;
    }

    /**
     * 获取用户信息
     * 第四步：拉取用户信息(需scope为 snsapi_userinfo)
     * 如果网页授权作用域为snsapi_userinfo，则此时开发者可以通过access_token和openid拉取用户信息了。
     *
     * @param openId 开放id
     * @return {@link WxUserInfoResp}
     */
    public static WxUserInfoResp getWebAuthUserInfo(String accessToken, String openId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("access_token", accessToken);
        paramsMap.put("openid", openId);
        paramsMap.put("lang", "zh_CN");
        String requestRes = HttpUtil.get("https://api.weixin.qq.com/sns/userinfo", paramsMap, null);
        log.info("[微信公众号] 网页授权登录|获取用户信息：" + requestRes);
        return JSON.parseObject(requestRes, WxUserInfoResp.class);
    }

    /**
     * 获取普通AccessToken
     * <p>
     * 关于网页授权access_token和普通access_token的区别
     * <p>
     * 1、微信网页授权是通过OAuth2.0机制实现的，在用户授权给公众号后，公众号可以获取到一个网页授权特有的接口调用凭证（网页授权access_token），
     * 通过网页授权access_token可以进行授权后接口调用，如获取用户基本信息；
     * <p>
     * 2、其他微信接口，需要通过基础支持中的“获取access_token”接口来获取到的普通access_token调用。
     *
     * @return {@link WxAccessTokenResp}
     */
    private static String getAccessToken() {
        return WxCommonCachePool.getAndCache(WX_MP_ACCESS_TOKEN, s -> {
            Map<String, String> tokenParams = new HashMap<>();
            tokenParams.put("appid", mpConfig.getAppid());
            tokenParams.put("secret", mpConfig.getAppsecret());
            tokenParams.put("grant_type", "client_credential");
            String tokenResult = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token", tokenParams, null);
            WxAccessTokenResp wxAccessTokenResp = JSON.parseObject(tokenResult, WxAccessTokenResp.class);
            if (wxAccessTokenResp.getErrcode() != 0) {
                log.error("[微信公众号] 获取AccessToken失败：{}", wxAccessTokenResp);
                throw new WxServiceException(wxAccessTokenResp.getErrmsg());
            }
            return wxAccessTokenResp.getAccess_token();
        });
    }


    /**
     * 微信公众号模板消息
     * https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Template_Message_Interface.html
     * <p>
     *
     * @param wxMaUniformMessage 参数详情见:{@link WxMaUniformMessage}
     */
    public static void sendWxTemplateMessage(WxMaUniformMessage wxMaUniformMessage) {
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.convertUniformMessage(wxMaUniformMessage);
        sendWxTemplateMessage(templateMessage);
    }

    /**
     * 微信公众号模板消息
     * https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Template_Message_Interface.html
     * <p>
     *
     * @param templateMessage 参数
     */
    public static void sendWxTemplateMessage(WxMpTemplateMessage templateMessage) {
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + getAccessToken();
        String paramsStr = JSON.toJSONString(templateMessage);
        log.info("[微信公众号] 模板消息 发送中。URL:{}", url);
        log.info("[微信公众号] 模板消息 发送中。ParamJson:{}", paramsStr);
        if (!StringUtils.hasText(templateMessage.getTouser())) {
            log.error("[微信公众号] 模板消息 发送失败。touser 不能为空 ： {}", templateMessage.getTouser());
            return;
        }
        String resp = HttpUtil.postJson(url, paramsStr);
        WxMaUniformMessageResp messageSendResp = JSON.parseObject(resp, WxMaUniformMessageResp.class);
        if (messageSendResp.getErrcode() != 0) {
            log.error("[微信公众号] 模板消息 发送失败。{}", messageSendResp);
        } else {
            log.info("[微信公众号] 模板消息 发送成功。{}", messageSendResp);
        }
    }


    /**
     * https://developers.weixin.qq.com/doc/offiaccount/User_Management/Get_users_basic_information_UnionID.html#UinonId
     * 公众号用户管理：获取用户基本信息(UnionID机制)
     *
     * @param openId 开放id
     * @return {@link WxMpUserInfo}
     */
    public static WxMpUserInfo getUserInfo(String openId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("access_token", getAccessToken());
        paramsMap.put("openid", openId);
        paramsMap.put("lang", "zh_CN");
        String requestResul = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/user/info", paramsMap, null);
        log.info("[微信公众号] 获取用户基本信息(UnionID机制) {}", requestResul);
        return JSON.parseObject(requestResul, WxMpUserInfo.class);
    }

    /**
     * 用户标签管理
     * 创建标签
     *
     * @param tagName 标签名
     * @return {@link Integer} 标签ID or null
     */
    public static Integer createTags(String tagName) {
        WxMpTags wxMpTags = new WxMpTags(null, tagName);
        JSONObject paramObject = new JSONObject();
        paramObject.put("tag", wxMpTags);
        String url = "https://api.weixin.qq.com/cgi-bin/tags/create?access_token=" + getAccessToken();
        String res = HttpUtil.postJson(url, paramObject.toJSONString());
        log.info("[微信公众号] 用户标签管理-创建标签 {}", res);
        JSONObject tagNode = JSON.parseObject(res).getJSONObject("tag");
        if (tagNode != null) {
            return tagNode.getInteger("id");
        }
        return null;
    }

    /**
     * 用户标签管理
     * 编辑标签
     *
     * @param tag 标签(包含id、name)
     */
    public static void updateTags(WxMpTags tag) {
        JSONObject paramObject = new JSONObject();
        paramObject.put("tag", tag);
        String url = "https://api.weixin.qq.com/cgi-bin/tags/update?access_token=" + getAccessToken();
        String res = HttpUtil.postJson(url, paramObject.toJSONString());
        log.info("[微信公众号] 用户标签管理-编辑标签 {}", res);
        WxCommonResp commonResp = JSON.parseObject(res, WxCommonResp.class);
        if (commonResp.unSuccess()) {
            throw new WxServiceException(commonResp.getErrmsg());
        }
    }

    /**
     * 用户标签管理
     * 删除标签
     *
     * @param tagId 标签Id
     */
    public static void deleteTags(Integer tagId) {
        JSONObject paramObject = new JSONObject();
        paramObject.put("tag", new WxMpTags(tagId, null));
        String url = "https://api.weixin.qq.com/cgi-bin/tags/delete?access_token=" + getAccessToken();
        String res = HttpUtil.postJson(url, paramObject.toJSONString());
        log.info("[微信公众号] 用户标签管理-删除标签 {}", res);
        WxCommonResp commonResp = JSON.parseObject(res, WxCommonResp.class);
        if (commonResp.unSuccess()) {
            throw new WxServiceException(commonResp.getErrmsg());
        }
    }

    /**
     * 用户标签管理
     * 获取公众号已创建的标签
     *
     * @param tagName 标签名
     * @return {@link List} 标签列表 or null
     */
    public static List<WxMpTags> getTags(String tagName) {
        String url = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=" + getAccessToken();
        String res = HttpUtil.get(url);
        JSONArray tagArray = JSON.parseObject(res).getJSONArray("tags");
        log.info("[微信公众号] 用户标签管理-获取公众号已创建的标签 {}", res);
        if (tagArray != null) {
            return tagArray.toJavaList(WxMpTags.class);
        }
        return null;
    }

    /**
     * 5. 获取标签下粉丝列表
     *
     * @param tagId       标签id
     * @param next_openid //第一个拉取的OPENID，不填默认从头开始拉取
     * @return {@link WxMpTagUsers}
     */
    public static WxMpTagUsers getTagUsers(Integer tagId, String next_openid) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("tagid", tagId);
        if (next_openid != null) {
            paramJson.put("next_openid", next_openid);
        }
        String url = "https://api.weixin.qq.com/cgi-bin/user/tag/get?access_token=" + getAccessToken();
        String res = HttpUtil.postJson(url, paramJson.toJSONString());
        log.info("[微信公众号] 用户标签管理-获取标签下粉丝列表 {}", res);
        return JSON.parseObject(res, WxMpTagUsers.class);
    }

    /**
     * 用户管理 /获取用户列表
     *
     * <p>附：关注者数量超过10000时：</p>
     * <p>当公众号关注者数量超过10000时，可通过填写next_openid的值，从而多次拉取列表的方式来满足需求。</p>
     * <p>具体而言，就是在调用接口时，将上一次调用得到的返回中的next_openid值，作为下一次调用中的next_openid值。</p>
     *
     * @param next_openid //第一个拉取的OPENID，不填默认从头开始拉取
     * @return {@link WxMpUsers}
     */
    public static WxMpUsers getTotalUsers(String next_openid) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + getAccessToken() + "&next_openid=";
        if (next_openid != null) {
            url = url + next_openid;
        }
        String res = HttpUtil.get(url);
        log.info("[微信公众号] 用户管理-获取用户列表 {}", res);
        return JSON.parseObject(res, WxMpUsers.class);
    }

    /**
     * 用户管理 / 批量获取用户基本信息
     *
     * @param openIds 用户的OpenId列表
     * @return {@link WxMpUsers}
     */
    public static List<WxMpUserInfo> getUsersInfo(List<String> openIds) {
        if (openIds == null || openIds.size() == 0) {
            return new ArrayList<>();
        }
        JSONObject paramJson = new JSONObject();
        JSONArray userArray = new JSONArray();
        for (String openId : openIds) {
            JSONObject user = new JSONObject();
            user.put("openid", openId);
            user.put("lang", "zh_CN");
            userArray.add(user);
        }
        paramJson.put("user_list", userArray);
        String url = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=" + getAccessToken();
        String res = HttpUtil.postJson(url, paramJson.toJSONString());
        log.info("[微信公众号] 用户管理-批量获取用户基本信息 {}", res);
        return JSON.parseObject(res).getJSONArray("user_info_list").toJavaList(WxMpUserInfo.class);
    }

    /**
     * 客服接口-发消息
     *
     * @param openId  用户openId
     * @param content 内容
     */
    public static void sendCustomMessage(String openId, String content) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("touser", openId);
        paramJson.put("msgtype", "text");
        JSONObject textContent = new JSONObject();
        textContent.put("content", content);
        paramJson.put("text", textContent);
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + getAccessToken();
        String res = HttpUtil.postJson(url, paramJson.toJSONString());
        log.info("[微信公众号] 客服接口-发消息 {}", res);
        WxCommonResp commonResp = JSON.parseObject(res, WxCommonResp.class);
        if (commonResp.unSuccess()) {
            throw new WxServiceException(commonResp.getErrmsg());
        }
    }

    /**
     * 批量为用户打标签
     *
     * @param tagId          标签id
     * @param userOpenIdList 用户openid列表
     */
    public static void taggingUser(Integer tagId, List<String> userOpenIdList) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("openid_list", userOpenIdList);
        paramJson.put("tagid", tagId);
        String url = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=" + getAccessToken();
        String res = HttpUtil.postJson(url, paramJson.toJSONString());
        log.info("[微信公众号] 用户标签管理-批量为用户打标签 {}", res);
        WxCommonResp commonResp = JSON.parseObject(res, WxCommonResp.class);
        if (commonResp.unSuccess()) {
            throw new WxServiceException(commonResp.getErrmsg());
        }
    }

    /**
     * 2. 批量为用户取消标签
     *
     * @param tagId          标签id
     * @param userOpenIdList 用户openid列表
     */
    public static void unTaggingUser(Integer tagId, List<String> userOpenIdList) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("openid_list", userOpenIdList);
        paramJson.put("tagid", tagId);
        String url = "https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token=" + getAccessToken();
        String res = HttpUtil.postJson(url, paramJson.toJSONString());
        log.info("[微信公众号] 用户标签管理-批量为用户取消标签 {}", res);
        WxCommonResp commonResp = JSON.parseObject(res, WxCommonResp.class);
        if (commonResp.unSuccess()) {
            throw new WxServiceException(commonResp.getErrmsg());
        }
    }


    /**
     * 3. 获取用户身上的标签列表
     *
     * @param openId 用户openid
     */
    public static List<Integer> unTaggingUser(Integer openId) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("openid", openId);
        String url = "https://api.weixin.qq.com/cgi-bin/tags/getidlist?access_token=" + getAccessToken();
        String res = HttpUtil.postJson(url, paramJson.toJSONString());
        log.info("[微信公众号] 用户标签管理-获取用户身上的标签列表 {}", res);
        JSONObject jsonObject = JSON.parseObject(res);
        return jsonObject.getJSONArray("tagid_list").toJavaList(Integer.class);
    }


    /**
     * 清除缓存
     */
    public static void clearCache() {
        WxCommonCachePool.clearAllCache();
        log.info("[微信公众号] 清除缓存成功！");
    }

}
