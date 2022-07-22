package cn.acyou.leo.tool.retrofit;

import com.github.lianjiatech.retrofit.spring.boot.interceptor.BaseGlobalInterceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author youfang
 * @version [1.0.0, 2022/7/22 10:48]
 **/
@Component
public class RetrofitCommonInterceptor extends BaseGlobalInterceptor {
    @Override
    public Response doIntercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newReq = request.newBuilder()
                .addHeader("timestamp", System.currentTimeMillis() + "")
                .build();
        return chain.proceed(newReq);
    }
}