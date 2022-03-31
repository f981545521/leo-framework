package cn.acyou.leo.framework.wx.auto;

import cn.acyou.leo.framework.wx.api.WxMiniProgramApi;
import cn.acyou.leo.framework.wx.api.WxMpApi;
import cn.acyou.leo.framework.wx.api.WxQyAppApi;
import cn.acyou.leo.framework.wx.api.WxQyArchiveApi;
import cn.acyou.leo.framework.wx.prop.WxConfigProperties;
import cn.acyou.leo.framework.wx.utils.qywx.WXBizMsgCrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置Bean
 *
 * @author fangyou
 * @version [1.0.0, 2021-08-03 17:59]
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(WxConfigProperties.class)
public class WxAutoConfiguration {

    /**
     * 微信公众号配置
     *
     * @param wxConfigProperties 配置属性
     * @return WxMpApi
     */
    @Bean
    @ConditionalOnProperty({"wx.mp.appid", "wx.mp.appsecret"})
    public WxMpApi wxMpUtil(WxConfigProperties wxConfigProperties) {
        return new WxMpApi(wxConfigProperties);
    }

    /**
     * 微信小程序配置
     *
     * @param wxConfigProperties 配置属性
     * @return WxMiniProgramApi
     */
    @Bean
    @ConditionalOnProperty({"wx.mini-program.appid", "wx.mini-program.appsecret"})
    public WxMiniProgramApi wxMiniProgramUtil(WxConfigProperties wxConfigProperties) {
        return new WxMiniProgramApi(wxConfigProperties);
    }

    /**
     * 企业微信接收消息解密
     *
     * @param wxConfigProperties 配置属性
     * @return WXBizMsgCrypt
     */
    @Bean
    @ConditionalOnProperty({"wx.qy-wx.corpid", "wx.qy-wx.callback-token", "wx.qy-wx.callback-encoding-a-e-s-key"})
    public WXBizMsgCrypt wxBizMsgCrypt(WxConfigProperties wxConfigProperties) {
        WxConfigProperties.QyWx qyWx = wxConfigProperties.getQyWx();
        return new WXBizMsgCrypt(qyWx.getCallbackToken(), qyWx.getCallbackEncodingAESKey(), qyWx.getCorpId());
    }

    /**
     * 企业微信应用接口
     *
     * @param wxConfigProperties 配置属性
     * @return WxQyAppApi
     */
    @Bean
    @ConditionalOnProperty({"wx.qy-wx.corpid", "wx.qy-wx.app-agent-id", "wx.qy-wx.app-secret"})
    public WxQyAppApi wxQyAppUtil(WxConfigProperties wxConfigProperties) {
        return new WxQyAppApi(wxConfigProperties);
    }

    /**
     * 企业微信会话存档接口
     *
     * @param wxConfigProperties 配置属性
     * @param wxQyAppApi         企业微信应用接口
     * @return WxQyArchiveApi
     */
    @Bean
    @ConditionalOnProperty({"wx.qy-wx.corpid", "wx.qy-wx.archive-secret", "wx.qy-wx.archive-private-key"})
    public WxQyArchiveApi wxQyArchiveApi(WxConfigProperties wxConfigProperties, WxQyAppApi wxQyAppApi) {
        return new WxQyArchiveApi(wxConfigProperties, wxQyAppApi);
    }

}
