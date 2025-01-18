package cn.acyou.leo.framework.retrofit;

import cn.acyou.leo.framework.model.Result;
import com.alibaba.fastjson.JSON;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.BaseLoggingInterceptor;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.LogLevel;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.LogStrategy;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Invocation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author youfang
 * @version [1.0.0, 2022/3/9 17:41]
 **/
public class RetrofitClientRecords extends BaseLoggingInterceptor {
    private final static Logger logger = LoggerFactory.getLogger(RetrofitClientRecords.class);
    private HttpLoggingInterceptor.Logger log;

    public RetrofitClientRecords(LogLevel logLevel, LogStrategy logStrategy) {
        super(logLevel, logStrategy);
        log = httpLoggingInterceptorLogger(logLevel);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String apiOperation = "";
        String classMethodName = "";
        boolean checkResult = false;
        int responseLength = 2000;
        final Invocation tag = chain.request().tag(Invocation.class);
        if (tag != null) {
            final RetrofitApi annotation = tag.method().getAnnotation(RetrofitApi.class);
            if (annotation != null) {
                apiOperation = annotation.value() + " | ";
                checkResult = annotation.checkResult();
                responseLength = annotation.responseLength();
            }
            classMethodName = tag.method().getDeclaringClass().getSimpleName() + ":" + tag.method().getName();
        }
        Request request = chain.request();
        final String method = request.method();
        final String url = request.url().toString();
        String requestBodyStr = "";
        Buffer requestBuffer = new Buffer();
        final RequestBody requestBody = request.body();
        if (requestBody != null) {
            requestBody.writeTo(requestBuffer);
            requestBodyStr = requestBuffer.readString(StandardCharsets.UTF_8);
        }

        log.log(String.format("%s[%s] -> 发送请求：%s %s", apiOperation, classMethodName, method, url) + ((requestBodyStr.length() > 0) ? (" | 参数： " + requestBodyStr) : ""));
        Response response = chain.proceed(request);
        long takeTimes = response.receivedResponseAtMillis() - response.sentRequestAtMillis();
        Headers headers = response.headers();
        ResponseBody body = response.body();
        if (body != null) {
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer responseBuffer = source.getBuffer();
            if ("gzip".equalsIgnoreCase(headers.get("Content-Encoding"))) {
                try (GzipSource gzippedResponseBody = new GzipSource(responseBuffer.clone())) {
                    responseBuffer = new Buffer();
                    responseBuffer.writeAll(gzippedResponseBody);
                }
            }
            final String responseStr = responseBuffer.clone().readString(StandardCharsets.UTF_8);
            log.log(String.format("%s[%s] <- 请求结束：%s 耗时：%s ms", apiOperation, classMethodName,
                    //subString when too long
                    (responseLength > 0 && responseStr.length() > responseLength) ? responseStr.substring(0, responseLength) + "..." : responseStr,
                    takeTimes));
            if (checkResult) {
                Result<?> resObj = JSON.parseObject(responseStr, Result.class);
                if (resObj.unsuccessful()) {
                    throw new RetrofitApiCheckException(resObj.getMsg());
                }
            }
        } else {
            log.log(String.format("%s请求结束：body is empty.", apiOperation));
        }
        return response;
    }


    public HttpLoggingInterceptor.Logger httpLoggingInterceptorLogger(LogLevel level) {
        if (level == LogLevel.DEBUG) {
            return logger::debug;
        } else if (level == LogLevel.ERROR) {
            return logger::error;
        } else if (level == LogLevel.INFO) {
            return logger::info;
        } else if (level == LogLevel.WARN) {
            return logger::warn;
        }
        throw new UnsupportedOperationException("We don't support this log level currently.");
    }
}
