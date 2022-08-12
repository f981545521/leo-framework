package cn.acyou.leo.tool.retrofit;

import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.retrofit.RetrofitApi;
import com.alibaba.fastjson.JSONObject;
import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import retrofit2.http.*;

import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2022/7/14 10:16]
 **/
@RetrofitClient(baseUrl = "${api.leo-pay-api-url}")
@AccessToken(accessToken = "${api.access-token}")
public interface LeoPayApi {

    @RetrofitApi("远程调用")
    @POST(value = "sys/common/printRequest")
    @Headers({
            "X-Foo: Bar",
            "X-Ping: Pong"
    })
    Result<JSONObject> printRequest(@Query("id") Long id, @Body Map<String, Object> reqMap, @Header("token") String token);

    @RetrofitApi("远程调用 test1")
    @GET(value = "api/test1")
    Result<JSONObject> test1();

    /**
     * 测试返回类型
     *
     * @return 这里返回的Object是为Map类型 ！！！
     */
    @RetrofitApi("远程调用 test2")
    @GET(value = "api/test2")
    Result<Object> test2();
}
