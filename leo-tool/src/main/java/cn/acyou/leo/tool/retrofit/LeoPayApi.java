package cn.acyou.leo.tool.retrofit;

import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.framework.retrofit.RetrofitApi;
import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2022/7/14 10:16]
 **/
@RetrofitClient(baseUrl = "${api.leo-pay-api-url}")
public interface LeoPayApi {

    @RetrofitApi("远程调用")
    @POST(value = "sys/common/printRequest")
    Result<Object> printRequest(@Query("id") Long id, @Body Map<String, Object> reqMap, @Header("token") String token);
}
