package cn.acyou.leo.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpClientUtil
 * @author youfang
 */
@Slf4j
public class HttpClientUtil {

    public static String doGet(String url) {
        return doGet(url, null, null);
    }

    public static String doGetParams(String url, Map<String, String> paramMap) {
        return doGet(url, paramMap, null);
    }

    public static String doGetHeader(String url, Map<String, String> headerMap) {
        return doGet(url, null, headerMap);
    }

    public static String doGet(String url, Map<String, String> param, Map<String, String> headerMap) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            if (headerMap != null && headerMap.size() > 0) {
                for (Map.Entry<String, String> header : headerMap.entrySet()) {
                    httpGet.setHeader(header.getKey(), header.getValue());
                }
            }
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return resultString;
    }

    public static String doPost(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return resultString;
    }

    public static String doPost(String url) {
        return doPost(url, null);
    }

    public static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return resultString;
    }

    /**
     * post请求, 参数为xml字符串
     *
     * @param url       请求路径
     * @param xmlString xml字符串
     * @return result
     */
    public static String doPostXml(String url, String xmlString) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "text/xml;charset=utf-8");
            // 创建请求内容
            StringEntity entity = new StringEntity(xmlString, ContentType.TEXT_XML);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return resultString;
    }

    /**
     * 下载文件（需要移动端 User-Agent的）
     *
     * @param url      网络文件路径
     * @param filepath 保存路径
     */
    public static void downloadMobileFile(String url, String filepath) {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url);
            httpget.addHeader("sdk-version", "1");
            httpget.addHeader("User-Agent", "Aweme 6.5.0 rv:65014 (iPhone; iOS 12.3.1; en_CN) Cronet");
            httpget.addHeader("Content-Type", "application/x-www-form-urlencoded");
            HttpResponse response = client.execute(httpget);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            File file = new File(filepath);
            boolean mkdirs = file.getParentFile().mkdirs();
            FileOutputStream fileout = new FileOutputStream(file);
            IOUtil.copyLarge(is, fileout);
            fileout.flush();
            is.close();
            fileout.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 下载文件
     *
     * @param url      网络文件路径
     * @param filepath 保存路径
     */
    public static void downloadFile(String url, String filepath) {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = client.execute(httpget);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            File file = new File(filepath);
            boolean mkdirs = file.getParentFile().mkdirs();
            FileOutputStream fileout = new FileOutputStream(file);
            IOUtil.copyLarge(is, fileout);
            fileout.flush();
            is.close();
            fileout.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
