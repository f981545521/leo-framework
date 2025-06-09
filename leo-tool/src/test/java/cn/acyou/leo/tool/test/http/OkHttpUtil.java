package cn.acyou.leo.tool.test.http;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
/**
 * @author youfang
 * @version [1.0.0, 2025/6/9 16:50]
 **/
public class OkHttpUtil {
    private static volatile OkHttpClient okHttpClient = null;
    private static volatile Semaphore semaphore = null;
    private Map<String, String> headerMap;
    private Map<String, String> paramMap;
    private String url;
    private Request.Builder request;
    private RequestBody requestBody;

    /**
     * 初始化okHttpClient，并且允许https访问
     */
    private OkHttpUtil(Proxy proxy) {
        if (okHttpClient == null) {
            synchronized (OkHttpClient.class) {
                if (okHttpClient == null) {
                    TrustManager[] trustManagers = buildTrustManagers();
                    okHttpClient = new OkHttpClient.Builder()
                            //设置连接超时时间
                            .connectTimeout(5, TimeUnit.MINUTES)
                            //写入超时时间
                            .writeTimeout(5, TimeUnit.MINUTES)
                            //从连接成功到响应的总时间
                            .readTimeout(5, TimeUnit.MINUTES)
                            //跳过ssl认证(https)
                            .sslSocketFactory(createSSLSocketFactory(trustManagers), (X509TrustManager) trustManagers[0])
                            .hostnameVerifier((hostName, session) -> true)
                            .retryOnConnectionFailure(true)
                            .proxy(proxy)//代理ip
                            //设置连接池  最大连接数量  , 持续存活的连接
                            .connectionPool(new ConnectionPool(50, 10, TimeUnit.MINUTES))
                            .build();
                    addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
                }
            }
        }
    }

    /**
     * 用于异步请求时，控制访问线程数，返回结果
     *
     */
    private static Semaphore getSemaphoreInstance() {
        //只能1个线程同时访问
        synchronized (OkHttpUtil.class) {
            if (semaphore == null) {
                semaphore = new Semaphore(0);
            }
        }
        return semaphore;
    }

    /**
     * 创建OkHttpUtils
     *
     */
    public static OkHttpUtil create() {
        return new OkHttpUtil(null);
    }
    /**
     * 创建OkHttpUtils
     *
     */
    public static OkHttpUtil createWithProxy(Proxy proxy) {
        return new OkHttpUtil(proxy);
    }
    /**
     * 创建OkHttpUtils
     *
     */
    public static OkHttpUtil createWithProxy(String inetSocketAddress) {
        String[] split = inetSocketAddress.split(":");
        return createWithProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(split[0], Integer.parseInt(split[1]))));
    }

    /**
     * 添加url
     *
     * @param url 地址
     */
    public OkHttpUtil url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 添加参数
     *
     * @param key   参数名
     * @param value 参数值
     */
    public OkHttpUtil addParam(String key, String value) {
        if (paramMap == null) {
            paramMap = new LinkedHashMap<>(16);
        }
        paramMap.put(key, value);
        return this;
    }

    /**
     * 添加请求头
     *
     * @param key   参数名
     * @param value 参数值
     */
    public OkHttpUtil addHeader(String key, String value) {
        if (headerMap == null) {
            headerMap = new LinkedHashMap<>(16);
        }
        headerMap.put(key, value);
        return this;
    }

    /**
     * 添加请求头
     *
     * @param json 参数值
     */
    public OkHttpUtil addBody(String json) {
        requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return this;
    }

    /**
     * 初始化get方法
     *
     */
    public OkHttpUtil get() {
        request = new Request.Builder().get();
        StringBuilder urlBuilder = new StringBuilder(url);
        if (paramMap != null) {
            urlBuilder.append("?");
            try {
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    urlBuilder.append(URLEncoder.encode(entry.getKey(), "utf-8")).
                            append("=").
                            append(URLEncoder.encode(entry.getValue(), "utf-8")).
                            append("&");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        request.url(urlBuilder.toString());
        return this;
    }

    /**
     * 初始化post方法
     *
     */
    public OkHttpUtil post() {
        request = new Request.Builder().post(requestBody).url(url);
        return this;
    }

    public OkHttpUtil put() {
        String json = "";
        if (paramMap != null) {
            json = JSON.toJSONString(paramMap);
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        request = new Request.Builder().put(requestBody).url(url);
        return this;
    }

    public OkHttpUtil delete() {
        String json = "";
        if (paramMap != null) {
            json = JSON.toJSONString(paramMap);
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        request = new Request.Builder().delete(requestBody).url(url);
        return this;
    }

    /**
     * 同步请求
     *
     */
    public String sync() {
        setHeader(request);
        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            assert response.body() != null;
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
            return "请求失败：" + e.getMessage();
        }
    }

    /**
     * 异步请求，有返回值
     */
    public String async() {
        StringBuilder buffer = new StringBuilder("");
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                buffer.append("请求出错：").append(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                buffer.append(Objects.requireNonNull(response.body()).string());
                getSemaphoreInstance().release();
            }
        });
        try {
            getSemaphoreInstance().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 异步请求，带有接口回调
     *
     * @param callBack 回调
     */
    public void async(ICallBack callBack) {
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callBack.onFailure(call, e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                callBack.onSuccessful(call, Objects.requireNonNull(response.body()).string());
            }
        });
    }

    /**
     * 为request添加请求头
     *
     * @param request 请求
     */
    private void setHeader(Request.Builder request) {
        if (headerMap != null) {
            try {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     *
     * @return SSLSocketFactory
     */
    private static SSLSocketFactory createSSLSocketFactory(TrustManager[] trustAllCerts) {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }

    private static TrustManager[] buildTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
    }

    /**
     * 自定义一个接口回调 - 发送异步请求时可以实现这个接口
     */
    public interface ICallBack {
        /**
         * 接口正常调用返回的内容
         *
         * @param call 回调
         * @param data 返回数据
         */
        void onSuccessful(Call call, String data);

        /**
         * 接口错误返回的内容
         *
         * @param call     回调
         * @param errorMsg 错误信息
         */
        void onFailure(Call call, String errorMsg);

    }
}
