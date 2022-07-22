package cn.acyou.leo.tool.retrofit;

import cn.acyou.leo.framework.util.StringUtils;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.BasePathMatchInterceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author youfang
 * @version [1.0.0, 2022/7/22 11:00]
 **/
@Component
public class AccessTokenInterceptor extends BasePathMatchInterceptor {
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Autowired
    private Environment environment;

    @Override
    public Response doIntercept(Chain chain) throws IOException {
        Request request = chain.request();

        Request newReq = request.newBuilder()
                .addHeader("access_token", accessToken)
                //方法二：直接使用environment对象来获取配置的属性
                .addHeader("access_token_2", StringUtils.isNotBlank(environment.getProperty("api.access-token"), ""))
                .build();
        return chain.proceed(newReq);
    }
}
