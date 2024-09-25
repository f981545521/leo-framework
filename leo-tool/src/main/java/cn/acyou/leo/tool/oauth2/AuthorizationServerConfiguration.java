package cn.acyou.leo.tool.oauth2;

import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.util.StringUtils;
import cn.acyou.leo.tool.security.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author youfang
 * @version [1.0.0, 2024/9/23 16:48]
 **/
@Slf4j
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private static final String DEMO_RESOURCE_ID = "order";

    @Autowired(required = false)
    AuthenticationManager authenticationManager;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ClientService clientService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .exceptionTranslator(new WebResponseExceptionTranslator() {
                    @Override
                    public ResponseEntity<Result<?>> translate(Exception e) throws Exception {
                        ResponseEntity.BodyBuilder status = ResponseEntity.status(HttpStatus.UNAUTHORIZED);
                        String message = "认证失败";
                        log.error(message, e);
                        if (e instanceof UnsupportedGrantTypeException) {
                            message = "不支持该认证类型";
                        }
                        if (e instanceof InvalidTokenException
                                && StringUtils.containsIgnoreCase(e.getMessage(), "Invalid refresh token (expired)")) {
                            message = "刷新令牌已过期，请重新登录";
                        }
                        if (e instanceof InvalidScopeException) {
                            message = "不是有效的scope值";
                        }
                        if (e instanceof InvalidGrantException) {
                            if (StringUtils.containsIgnoreCase(e.getMessage(), "Invalid refresh token")) {
                                message = "refresh token无效";
                            }
                            if (StringUtils.containsIgnoreCase(e.getMessage(), "Invalid authorization code")) {
                                message = "authorization code无效";
                            }
                            if (StringUtils.containsIgnoreCase(e.getMessage(), "locked")) {
                                message = "用户已被锁定，请联系管理员";
                            }
                            message = "用户名或密码错误";
                        }
                        return status.body(Result.error(message));
                    }
                })
                .tokenStore(new RedisTokenStore(redisConnectionFactory))
                .authenticationManager(authenticationManager);
    }


    /**
     * 配置：安全检查流程,用来配置令牌端点（Token Endpoint）的安全与权限访问
     * 默认过滤器：BasicAuthenticationFilter
     * 1、oauth_client_details表中clientSecret字段加密【ClientDetails属性secret】
     * 2、CheckEndpoint类的接口 oauth/check_token 无需经过过滤器过滤，默认值：denyAll()
     * 对以下的几个端点进行权限配置：
     * /oauth/authorize：授权端点
     * /oauth/token：令牌端点
     * /oauth/confirm_access：用户确认授权提交端点
     * /oauth/error：授权服务错误信息端点
     * /oauth/check_token：用于资源服务访问的令牌解析端点
     * /oauth/token_key：提供公有密匙的端点，如果使用JWT令牌的话
     **/
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        //允许表单认证
        oauthServer.allowFormAuthenticationForClients();
    }

}
