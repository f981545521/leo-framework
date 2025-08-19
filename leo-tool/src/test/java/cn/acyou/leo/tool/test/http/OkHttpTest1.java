package cn.acyou.leo.tool.test.http;


import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author youfang
 * @version [1.0.0, 2025/6/9 16:48]
 **/
public class OkHttpTest1 {
    public static void main(String[] args) {
        String res = OkHttpUtil.instance()
                .url("https://httpbin.org/anything")
                .addHeader("access-token", "1")
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .addBody("{\"page_size\":200,\"page_number\":0,\"t\":66666,\"create_date\":[\"2025-03-09T16:00:00.000Z\",\"2025-06-10\"]}")
                .post()
                .async();
        System.out.println(res);

        Request request = new Request.Builder().url("https://httpbin.org/anything")
                .addHeader("access-token", "1")
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{\n" +
                        "  \"t\": 1000,\n" +
                        "  \"member_id\": 100000191091\n" +
                        "}"))
                .build();

        Call call = OkHttpUtil.client().newCall(request);

    }
}
