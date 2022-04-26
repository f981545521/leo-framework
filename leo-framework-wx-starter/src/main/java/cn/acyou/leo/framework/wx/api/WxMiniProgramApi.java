package cn.acyou.leo.framework.wx.api;

import cn.acyou.leo.framework.wx.dto.miniprogram.*;
import cn.acyou.leo.framework.wx.exception.WxServiceException;
import cn.acyou.leo.framework.wx.prop.WxConfigProperties;
import cn.acyou.leo.framework.wx.utils.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * wx.miniProgram.appid=xx
 * wx.miniProgram.secret=xxx
 * <p>
 * 获取access_token：
 * <p>
 * https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
 * <p>
 * 获取session_key    "xxx"
 * <pre>
 *     WXML：
 *     {@code
 *     <button class='bottom' type='primary' open-type="getPhoneNumber" lang="zh_CN" bindgetphonenumber="getPhoneNumber">
 *         授权登录（手机号）
 *     </button>
 *     }
 *
 *     JS：
 *     getPhoneNumber: function(e){
 *         console.log(e.detail.errMsg)
 *         console.log(e.detail.iv)
 *         console.log(e.detail.encryptedData)
 *         var _this = this;
 *         wx.login({
 *             success: res => {
 *                 console.log(res.code)
 *             }
 *         })
 *     },
 * </pre>
 * https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
 * 请求参数：
 * 属性	类型	默认值	必填	说明
 * appid	string		是	    小程序 appId
 * secret	string		是	    小程序 appSecret
 * js_code	string		是	    登录时获取的 code
 * grant_type	string	是	    授权类型，此处只需填写 authorization_code
 * <p>
 * 返回值：
 * openid	string	用户唯一标识
 * session_key	string	会话密钥
 * unionid	string	用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 UnionID 机制说明。
 * errcode	number	错误码
 * errmsg	string	错误信息
 * <p>
 *
 * @author youfang
 * @version [1.0.0, 2020-7-9 下午 11:23]
 **/
@Slf4j
public class WxMiniProgramApi {

    /**
     * 微信公众号和小程序绑定时，可以发送跳转到小程序的消息
     */
    private static WxConfigProperties wxConfigProperties;

    public WxMiniProgramApi(WxConfigProperties wxConfigProperties) {
        log.info("WxMiniProgramUtil 初始化。");
        WxMiniProgramApi.wxConfigProperties = wxConfigProperties;
    }

    /**
     * 微信小程序ACCESS_TOKEN 缓存
     */
    private static final String WX_MINI_PROGRAM_ACCESS_TOKEN_ = "wxCache:wxMiniProgram:ACCESS_TOKEN";

    /**
     * 清除缓存
     */
    public void clearCache() {
        WxCommonCachePool.clearAllCache();
    }


    /**
     * 解密用户信息
     * <p>注意：企业微信中无法解密用户手机号信息</p>
     * <p>https://developers.weixin.qq.com/community/develop/doc/0008caf41c0408dc0d79cf40851000</p>
     *
     * @param encryptedData 加密的数据
     * @param sessionKey    会话密钥
     * @param iv            四世
     * @return {@link JSONObject}
     */
    public static WxUserEncryptInfo decryptUserInfo(String encryptedData, String sessionKey, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.getDecoder().decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.getDecoder().decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.getDecoder().decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + 1;
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, StandardCharsets.UTF_8);
                return JSONObject.parseObject(result, WxUserEncryptInfo.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new WxServiceException("微信数据解密失败！");
    }

    /**
     * 获取用户的会话密钥
     * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html
     *
     * @param code 代码
     * @return {@link WxCode2SessionResp}
     */
    public WxCode2SessionResp getUserSessionKey(String code) {
        String code2SessionUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=" + wxConfigProperties.getMiniProgram().getAppid() +
                "&secret=" + wxConfigProperties.getMiniProgram().getAppsecret() + "&js_code="
                + code + "&grant_type=authorization_code";
        String code2SessionUrlResult = HttpUtil.get(code2SessionUrl);
        WxCode2SessionResp wxCode2SessionVo = JSON.parseObject(code2SessionUrlResult, WxCode2SessionResp.class);
        if (wxCode2SessionVo.getErrcode() == 40163) {
            throw new WxServiceException("[微信小程序] 小程序登录Code已经被使用，请退出后重新登录。");
        }
        if (wxCode2SessionVo.getErrcode() != 0) {
            throw new WxServiceException(wxCode2SessionVo.getErrmsg());
        }
        return wxCode2SessionVo;
    }

    /**
     * 获取接口凭证
     * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/access-token/auth.getAccessToken.html
     *
     * @return accessToken
     */
    private static String getAccessToken() {
        return WxCommonCachePool.getAndCache(WX_MINI_PROGRAM_ACCESS_TOKEN_, s -> {
            Map<String, String> tokenParams = new HashMap<>();
            tokenParams.put("grant_type", "client_credential");
            tokenParams.put("appid", wxConfigProperties.getMiniProgram().getAppid());
            tokenParams.put("secret", wxConfigProperties.getMiniProgram().getAppsecret());
            String getResult = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token", tokenParams, null);
            WxAccessTokenResp wxAccessTokenResp = JSON.parseObject(getResult, WxAccessTokenResp.class);
            if (wxAccessTokenResp.getErrcode() != 0) {
                throw new WxServiceException(wxAccessTokenResp.getErrmsg());
            }
            return wxAccessTokenResp.getAccess_token();
        });

    }

    /**
     * 微信统一服务消息
     * 发送微信公众号/小程序消息
     * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/uniform-message/uniformMessage.send.html
     * <p>
     *
     * @param wxMaUniformMessage 参数详情见:{@link WxMaUniformMessage}
     */
    public WxMaUniformMessageResp sendWxUniformMessage(WxMaUniformMessage wxMaUniformMessage) {
        wxMaUniformMessage.getMp_template_msg().setAppid(wxConfigProperties.getMp().getAppid());
        if (wxMaUniformMessage.getMp_template_msg() != null) {
            if (wxMaUniformMessage.getMp_template_msg().getMiniprogram() != null) {
                wxMaUniformMessage.getMp_template_msg().getMiniprogram().setAppid(wxConfigProperties.getMiniProgram().getAppid());
            }
        }
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/uniform_send?access_token=" + getAccessToken();
        String paramsStr = JSON.toJSONString(wxMaUniformMessage);
        log.info("[微信小程序] 微信统一服务消息发送中。URL:{}", url);
        log.info("[微信小程序] 微信统一服务消息发送中。ParamJson:{}", paramsStr);
        String resp = HttpUtil.postJson(url, paramsStr);
        WxMaUniformMessageResp messageSendResp = JSON.parseObject(resp, WxMaUniformMessageResp.class);
        if (messageSendResp.getErrcode() != 0) {
            log.error("[微信小程序] 微信统一服务消息发送失败。{}", messageSendResp);
        } else {
            log.info("[微信小程序] 微信统一服务消息发送成功。{}", messageSendResp);
        }
        return messageSendResp;
    }

    /**
     * https://developers.weixin.qq.com/minigame/dev/api-backend/open-api/qr-code/wxacode.getUnlimited.html
     * <p>
     * 获取小程序码，适用于需要的码数量极多的业务场景。通过该接口生成的小程序码，永久有效，数量暂无限制。
     *
     * @param scene 最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，
     *              其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式）
     * @param page  必须是已经发布的小程序存在的页面（否则报错），例如 pages/index/index, 根路径前不要填加 /,
     *              不能携带参数（参数请放在scene字段里），如果不填写这个字段，默认跳主页面
     * @return 如果调用成功，会直接返回图片二进制内容，如果请求失败，会返回 JSON 格式的数据。
     */
    public WxGetUnlimitedResp getUnlimitedResp(String scene, String page) {
        Map<String, String> params = new HashMap<>();
        params.put("scene", scene);
        params.put("page", page);
        String resp = HttpUtil.postJson("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + getAccessToken(), params);
        WxGetUnlimitedResp unlimitedResp = JSON.parseObject(resp, WxGetUnlimitedResp.class);
        log.info("[微信小程序] 获取小程序码。{}", unlimitedResp);
        return unlimitedResp;
    }

}
