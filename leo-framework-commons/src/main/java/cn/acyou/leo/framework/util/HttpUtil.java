package cn.acyou.leo.framework.util;

import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * HTTP Client
 *
 * @author youfang
 */
@Slf4j
public class HttpUtil {

    private static PoolingHttpClientConnectionManager cm;
    private static IdleConnectionMonitorThread monitor;

    private HttpRequestBase request;
    private final HttpClientBuilder clientBuilder;
    private final CookieStore cookieStore;
    /**
     * 请求的相关配置
     */
    private final RequestConfig.Builder configBuilder;

    private static PoolingHttpClientConnectionManager getHttpClientConnectionManager() {
        if (cm == null) {
            cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(200);
            cm.setDefaultMaxPerRoute(20);
            SocketConfig config = SocketConfig.custom().setSoTimeout(10000).build();
            cm.setDefaultSocketConfig(config);

            monitor = new IdleConnectionMonitorThread(cm);
            monitor.start();
        }
        return cm;
    }

    private HttpUtil() {
        this.clientBuilder = HttpClients.custom().setConnectionManager(getHttpClientConnectionManager());
        this.configBuilder = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(10000).setCookieSpec(CookieSpecs.STANDARD);
        this.cookieStore = new BasicCookieStore();
    }

    private static List<NameValuePair> convertParameters(Multimap<String, String> params) {
        List<NameValuePair> pairs = new ArrayList<>(params.size());
        for (Entry<String, String> entry : params.entries()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return pairs;
    }

    public static HttpUtil newInstance() {
        return new HttpUtil();
    }

    public static void shutdown() {
        monitor.shutdown();
        cm.shutdown();
    }

    public static HttpUtil getHttp(String url, Multimap<String, String> paramMap, Map<String, String> headerMap) {
        HttpUtil httpUtil = newInstance();
        URIBuilder uriBuilder = new URIBuilder(URI.create(url));
        uriBuilder.setCharset(StandardCharsets.UTF_8);
        if (paramMap != null) {
            List<NameValuePair> pairs = convertParameters(paramMap);
            uriBuilder.setParameters(pairs);
        }
        HttpGet request = new HttpGet(uriBuilder.toString());
        if (headerMap != null && headerMap.size() > 0) {
            for (Entry<String, String> param : headerMap.entrySet()) {
                request.addHeader(param.getKey(), param.getValue());
            }
        }

        httpUtil.request = request;
        return httpUtil;
    }

    public static String get(String url) {
        HttpUtil http = getHttp(url, null, null);
        return http.execute().getString();
    }

    public static String get(String url, Multimap<String, String> paramMap) {
        HttpUtil http = getHttp(url, paramMap, null);
        return http.execute().getString();
    }

    public static String get(String url, Multimap<String, String> paramMap, Map<String, String> headerMap) {
        HttpUtil httputil = getHttp(url, paramMap, headerMap);
        return httputil.execute().getString();
    }

    public static HttpUtil postHttp(String url, Multimap<String, String> paramMap, Map<String, File> fileMap,
                                    Map<String, String> headerMap) {
        HttpUtil httpUtil = newInstance();
        HttpPost request = new HttpPost(url);

        if (fileMap != null && fileMap.size() > 0) {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().setCharset(StandardCharsets.UTF_8).setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            for (Entry<String, File> param : fileMap.entrySet()) {
                multipartEntityBuilder.addBinaryBody(param.getKey(), param.getValue());
            }

            if (paramMap != null && paramMap.size() > 0) {
                for (Entry<String, String> param : paramMap.entries()) {
                    multipartEntityBuilder.addTextBody(param.getKey(), param.getValue(), ContentType.WILDCARD.withCharset(StandardCharsets.UTF_8));
                }
            }
            request.setEntity(multipartEntityBuilder.build());
        } else {
            EntityBuilder entityBuilder = EntityBuilder.create().setContentType(ContentType.APPLICATION_FORM_URLENCODED.withCharset(StandardCharsets.UTF_8)).setContentEncoding(StandardCharsets.UTF_8.name());
            if (paramMap != null && paramMap.size() > 0) {
                List<NameValuePair> nameValuePairs = convertParameters(paramMap);
                entityBuilder.setParameters(nameValuePairs);
            }
            request.setEntity(entityBuilder.build());
        }

        if (headerMap != null) {
            for (Entry<String, String> param : headerMap.entrySet()) {
                request.addHeader(param.getKey(), param.getValue());
            }
        }
        httpUtil.request = request;
        return httpUtil;
    }

    public static HttpUtil postJsonHttp(String url, String jsonParam, Map<String, String> headerMap) {
        HttpUtil httpUtil = newInstance();
        HttpPost request = new HttpPost(url);

        if (StringUtils.isNotBlank(jsonParam)) {
            HttpEntity entity = EntityBuilder.create().setText(jsonParam).setContentType(ContentType.APPLICATION_JSON.withCharset(StandardCharsets.UTF_8)).setContentEncoding(StandardCharsets.UTF_8.name()).build();
            request.setEntity(entity);
        }

        if (headerMap != null) {
            for (Entry<String, String> param : headerMap.entrySet()) {
                request.addHeader(param.getKey(), param.getValue());
            }
        }
        httpUtil.request = request;
        return httpUtil;
    }

    public static String post(String url) {
        HttpUtil httputil = postHttp(url, null, null, null);
        return httputil.execute().getString();
    }

    public static String postJson(String url, String jsonParam, Map<String, String> headerMap) {
        HttpUtil httputil = postJsonHttp(url, jsonParam, headerMap);
        return httputil.execute().getString();
    }

    public static String post(String url, Multimap<String, String> map) {
        HttpUtil httputil = postHttp(url, map, null, null);
        return httputil.execute().getString();
    }

    public static String post(String url, Multimap<String, String> paramMap, Map<String, String> headerMap) {
        HttpUtil httputil = postHttp(url, paramMap, null, headerMap);
        return httputil.execute().getString();
    }

    public static String postFile(String url, Map<String, File> fileMap) {
        HttpUtil httputil = postHttp(url, null, fileMap, null);
        return httputil.execute().getString();
    }

    public static String postFile(String url, File file) {
        Map<String, File> fileMap = new HashMap<>(1);
        fileMap.put(file.getName(), file);
        HttpUtil httputil = postHttp(url, null, fileMap, null);
        return httputil.execute().getString();
    }

    public static String postFile(String url, Multimap<String, String> paramMap, Map<String, File> fileMap) {
        HttpUtil httputil = postHttp(url, paramMap, fileMap, null);
        return httputil.execute().getString();
    }

    public static String postFile(String url, Multimap<String, String> paramMap, File file) {
        Map<String, File> fileMap = new HashMap<>(1);
        fileMap.put(file.getName(), file);
        HttpUtil httputil = postHttp(url, paramMap, fileMap, null);
        return httputil.execute().getString();
    }

    public static String postFile(String url, Multimap<String, String> paramMap, List<File> fileList) {
        Map<String, File> fileMap = new HashMap<>(fileList.size());
        for (int i = 0, length = fileList.size(); i < length; i++) {
            fileMap.put(fileList.get(i).getName(), fileList.get(i));
        }
        HttpUtil httputil = postHttp(url, paramMap, fileMap, null);
        return httputil.execute().getString();
    }

    public static HttpUtil putHttp(String url, Multimap<String, String> paramMap, Map<String, File> fileMap,
                                   Map<String, String> headerMap) {
        HttpUtil httpUtil = newInstance();
        HttpPut request = new HttpPut(url);

        if (fileMap != null && fileMap.size() > 0) {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().setCharset(StandardCharsets.UTF_8).setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            for (Entry<String, File> param : fileMap.entrySet()) {
                multipartEntityBuilder.addBinaryBody(param.getKey(), param.getValue());
            }

            if (paramMap != null && paramMap.size() > 0) {
                for (Entry<String, String> param : paramMap.entries()) {
                    multipartEntityBuilder.addTextBody(param.getKey(), param.getValue(), ContentType.WILDCARD.withCharset(StandardCharsets.UTF_8));
                }
            }
            request.setEntity(multipartEntityBuilder.build());
        } else {
            EntityBuilder entityBuilder = EntityBuilder.create().setContentType(ContentType.APPLICATION_FORM_URLENCODED.withCharset(StandardCharsets.UTF_8)).setParameters();
            if (paramMap != null && paramMap.size() > 0) {
                List<NameValuePair> nameValuePairs = convertParameters(paramMap);
                entityBuilder.setParameters(nameValuePairs);
            }
            request.setEntity(entityBuilder.build());
        }

        if (headerMap != null) {
            for (Entry<String, String> param : headerMap.entrySet()) {
                request.addHeader(param.getKey(), param.getValue());
            }
        }
        httpUtil.request = request;
        return httpUtil;
    }

    public static String put(String url) {
        HttpUtil httputil = putHttp(url, null, null, null);
        return httputil.execute().getString();
    }

    public static String put(String url, Multimap<String, String> map) {
        HttpUtil httputil = putHttp(url, map, null, null);
        return httputil.execute().getString();
    }

    public static String put(String url, Multimap<String, String> paramMap, Map<String, String> headerMap) {
        HttpUtil httputil = putHttp(url, paramMap, null, headerMap);
        return httputil.execute().getString();
    }

    public static String putFile(String url, Map<String, File> fileMap) {
        HttpUtil httputil = postHttp(url, null, fileMap, null);
        return httputil.execute().getString();
    }

    public static HttpUtil deleteHttp(String url, Map<String, String> headerMap) {
        HttpUtil httpUtil = newInstance();
        HttpDelete request = new HttpDelete(url);
        if (headerMap != null) {
            for (Entry<String, String> param : headerMap.entrySet()) {
                request.addHeader(param.getKey(), param.getValue());
            }
        }
        httpUtil.request = request;
        return httpUtil;
    }

    public static String delete(String url) {
        HttpUtil httputil = deleteHttp(url, null);
        return httputil.execute().getString();
    }

    public static String delete(String url, Map<String, String> headerMap) {
        HttpUtil httputil = deleteHttp(url, headerMap);
        return httputil.execute().getString();
    }

    public static InputStream openStream(String url) throws Exception {
        return new URL(url).openStream();
    }

    public static long getContentLength(String url) throws Exception {
        return new URL(url).openConnection().getContentLengthLong();
    }

    public static boolean reachable(String url) {
        try {
            getContentLength(url);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public static boolean notReachable(String url){
        return !reachable(url);
    }

    public void setSocketTimeout(int socketTimeout) {
        this.configBuilder.setSocketTimeout(socketTimeout);
    }

    public void setConnectTimeout(int connectTimeout) {
        this.configBuilder.setConnectTimeout(connectTimeout);
    }

    public ResponseWrap execute() {
        this.request.setConfig(this.configBuilder.build());

        CloseableHttpClient httpClient = clientBuilder.build();

        HttpClientContext context = HttpClientContext.create();
        if (!this.cookieStore.getCookies().isEmpty()) {
            context.setCookieStore(cookieStore);
        }

        try {
            CloseableHttpResponse response = httpClient.execute(request, context);
            return new ResponseWrap(response, context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public class ResponseWrap {

        private CloseableHttpResponse response;
        private HttpClientContext context;
        private HttpEntity entity;

        public ResponseWrap(CloseableHttpResponse response, HttpClientContext context) {
            this.response = response;
            this.context = context;
            this.entity = response.getEntity();
        }

        public void close() {
            HttpClientUtils.closeQuietly(response);
        }

        public String getString() {
            return getString(StandardCharsets.UTF_8);
        }

        public String getString(Charset defaultCharset) {
            try {
                return entity == null ? "" : EntityUtils.toString(entity, defaultCharset);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                HttpClientUtils.closeQuietly(response);
            }
        }

        public byte[] getByteArray() {
            try {
                return entity == null ? null : EntityUtils.toByteArray(entity);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                HttpClientUtils.closeQuietly(response);
            }
        }

        public Header[] getAllHeaders() {
            return response.getAllHeaders();
        }

        public Header[] getHeaders(String name) {
            return response.getHeaders(name);
        }

        public StatusLine getStatusLine() {
            return response.getStatusLine();
        }

        public int getStatusCode() {
            return response.getStatusLine().getStatusCode();
        }

        public boolean containsHeader(String name) {
            return response.containsHeader(name);
        }

        public List<Cookie> getCookies() {
            return context.getCookieStore().getCookies();
        }

        public Cookie getCookie(String name) {
            List<Cookie> cookies = context.getCookieStore().getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (name.equals(cookie.getName())) {
                        return cookie;
                    }
                }
            }
            return null;
        }

        public InputStream getInputStream() {
            try {
                return entity == null ? null : entity.getContent();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public void addCookie(String name, String value) {
        cookieStore.addCookie(new BasicClientCookie2(name, value));
    }

    public void addCookie(Map<String, String> cookieMap) {
        for (Entry<String, String> entry : cookieMap.entrySet()) {
            cookieStore.addCookie(new BasicClientCookie2(entry.getKey(), entry.getValue()));
        }
    }

    /**
     * 下载文件
     * 文件名为 yyyyMMddHHmmss + 4位随机数 + 扩展名
     *
     * @param url       url
     * @param path      保存路径
     */
    public static void download(String url, String path) throws Exception {
        String name = MessageFormat.format("{0}_{1}.{2}",
                DateUtil.getCurrentDateFormat(DateUtil.FORMAT_DATE_TIME_UNSIGNED),
                RandomUtil.createRandomStr(4),
                FileUtil.extName(url));
        download(url, path, name);
    }

    /**
     * 下载文件
     *
     * @param url       url
     * @param path      保存路径
     * @param imageName 图片的名字
     *                      为空时使用URL上的文件名
     */
    public static void download(String url, String path, @Nullable String imageName) throws Exception {
        URL downloadUrl = new URL(url);
        InputStream inputStream = downloadUrl.openStream();
        File file = new File(path);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
        }
        if (imageName == null || imageName.length() == 0){
            imageName = url.substring(url.lastIndexOf("/") + 1);
        }
        FileOutputStream out = new FileOutputStream(file + "\\" + imageName);
        IOUtils.copy(inputStream, out);
        inputStream.close();
        out.close();
    }

    static class IdleConnectionMonitorThread extends Thread {

        private final HttpClientConnectionManager connMgr;
        private volatile boolean shutdown;

        public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
            super();
            this.connMgr = connMgr;
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(5000);
                        // Close expired connections
                        connMgr.closeExpiredConnections();
                        // Optionally, close connections
                        // that have been idle longer than 30 sec
                        connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
                    }
                }
            } catch (InterruptedException ex) {
                // terminate
            }
        }

        public void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        String url = "http://qiniu.acyou.cn/0ecdd8490b7ee4302861b4708a50ac8d.jpg";
        try {
            download(url, "D:\\images\\");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
