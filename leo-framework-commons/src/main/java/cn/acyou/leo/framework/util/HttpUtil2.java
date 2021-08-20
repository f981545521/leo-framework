package cn.acyou.leo.framework.util;

import cn.acyou.leo.framework.model.Result;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;

/**
 * HTTP Client
 *
 * @author youfang
 */
public class HttpUtil2 {

    public static final RestTemplate restTemplate;

    static {
        restTemplate = new RestTemplateBuilder()
                .messageConverters(getJsonMessageConvert())
                .setConnectTimeout(Duration.ofMinutes(5))
                .build();
    }

    /**
     * get请求
     *
     * @param url url
     * @return 返回
     */
    public static String get(String url) {
        return get(url, null, null);
    }

    /**
     * get请求
     *
     * @param url       url
     * @param paramsMap 参数
     * @return 返回
     */
    public static String getParameter(String url, Map<String, Object> paramsMap) {
        return get(url, paramsMap, null);
    }

    /**
     * get请求
     *
     * @param url        url
     * @param headersMap Header
     * @return 返回
     */
    public static String getHeader(String url, Map<String, String> headersMap) {
        return get(url, null, headersMap);
    }

    /**
     * get请求
     *
     * @param url        url
     * @param paramsMap  参数
     * @param headersMap Header
     * @return 返回
     */
    public static String get(String url, Map<String, Object> paramsMap, Map<String, String> headersMap) {
        return get(url, paramsMap, headersMap, String.class);
    }

    /**
     * get请求
     *
     * @param url        url
     * @param paramsMap  参数
     * @param headersMap Header
     * @param resType 返回类型
     * @return 返回
     */
    public static <T> T get(String url, Map<String, Object> paramsMap, Map<String, String> headersMap, Class<T> resType) {
        HttpHeaders headers = new HttpHeaders();
        if (headersMap != null && !headersMap.isEmpty()) {
            headersMap.forEach(headers::add);
        }
        if (paramsMap != null && !paramsMap.isEmpty()) {
            url = UrlUtil.append(url, paramsMap);
        }
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.GET, entity, resType);
        return result.getBody();
    }
    /**
     * get请求
     *
     * @param url        url
     * @param paramsMap  参数
     * @param headersMap Header
     * @param responseType 返回类型（泛型）
     * @return 返回
     */
    public static <T> T get(String url, Map<String, Object> paramsMap, Map<String, String> headersMap, ParameterizedTypeReference<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        if (headersMap != null && !headersMap.isEmpty()) {
            headersMap.forEach(headers::add);
        }
        if (paramsMap != null && !paramsMap.isEmpty()) {
            url = UrlUtil.append(url, paramsMap);
        }
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
        return result.getBody();
    }

    public static <T> T post(String url, Map<String, Object> paramsMap, Map<String, String> headersMap, Class<T> resType){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        if (headersMap != null && !headersMap.isEmpty()) {
            headersMap.forEach(headers::add);
        }

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (paramsMap != null && !paramsMap.isEmpty()) {
            for (Map.Entry<String, Object> stringObjectEntry : paramsMap.entrySet()) {
                map.add(stringObjectEntry.getKey(), stringObjectEntry.getValue().toString());
            }
        }
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.POST, entity, resType);
        return result.getBody();
    }

    public static <T> T postJson(String url, String jsonParam, Map<String, String> headersMap, Class<T> resType){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonParam, headers);
        ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.POST, entity, resType);
        return result.getBody();
    }


    public static void main(String[] args) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("id", 123);
        paramsMap.put("name", "黄飞鸿");
        paramsMap.put("age", 12);
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Auth", "54321666666");
        Result s = post("http://localhost:8054/rest/test1", paramsMap, headersMap, Result.class);
        //Result<List<String>> s2 = get("http://localhost:8054/rest/test1", paramsMap, headersMap, new ParameterizedTypeReference<Result<List<String>>>() {});
        System.out.println(s);
    }

    private static HttpMessageConverter getJsonMessageConvert(){
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.ALL);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        return fastConverter;
    }
}
