package cn.acyou.leo.tool.retrofit;

import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.retrofit.RetrofitApi;
import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import retrofit2.http.*;

import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2022/7/14 10:16]
 **/
@RetrofitClient(baseUrl = "${api.leo-pay-api-url}")
public interface LeoPayApi {

    @RetrofitApi("远程调用")
    @POST(value = "sys/common/printRequest")
    @Headers({
            "X-Foo: Bar",
            "X-Ping: Pong"
    })
    Result<Object> printRequest(@Query("id") Long id, @Body Map<String, Object> reqMap, @Header("token") String token);
}
