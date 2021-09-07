package cn.acyou.leo.framework.util;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * HTTP Client
 *
 * @author youfang
 */
public class HttpUtil2 {

    public static final RestTemplate restTemplate;

    private HttpUtil2(){

    }

    static {
        restTemplate = new RestTemplate();
    }

    /**
     * get请求
     *
     * @param url url
     * @return 返回类型
     */
    public static String get(String url) {
        return get(url, null, null);
    }

    /**
     * get请求
     *
     * @param url       url
     * @param paramsMap 参数
     * @return 返回类型
     */
    public static String getParameter(String url, Map<String, Object> paramsMap) {
        return get(url, paramsMap, null);
    }

    /**
     * get请求
     *
     * @param url        url
     * @param headersMap Header
     * @return 返回类型
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
     * @return 返回类型
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
     * @param resType    返回类型
     * @return 返回类型
     */
    public static <T> T get(String url, Map<String, Object> paramsMap, Map<String, String> headersMap, Class<T> resType) {
        return getMethod(url, paramsMap, headersMap, resType, null);
    }

    /**
     * get请求
     *
     * @param url          url
     * @param paramsMap    参数
     * @param headersMap   Header
     * @param responseType 返回类型（泛型）
     * @return 返回类型
     */
    public static <T> T get(String url, Map<String, Object> paramsMap, Map<String, String> headersMap, ParameterizedTypeReference<T> responseType) {
        return getMethod(url, paramsMap, headersMap, null, responseType);
    }


    /**
     * get请求
     *
     * @param url          url
     * @param paramsMap    参数
     * @param headersMap   Header
     * @param responseType 返回类型（泛型）
     * @return 返回类型
     */
    private static <T> T getMethod(String url, Map<String, Object> paramsMap, Map<String, String> headersMap, Class<T> resType, ParameterizedTypeReference<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        if (headersMap != null && !headersMap.isEmpty()) {
            headersMap.forEach(headers::add);
        }
        if (paramsMap != null && !paramsMap.isEmpty()) {
            url = UrlUtil.append(url, paramsMap);
        }
        HttpEntity<?> entity = new HttpEntity<>(headers);
        if (resType != null) {
            ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.GET, entity, resType);
            return result.getBody();
        }
        if (responseType != null) {
            ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
            return result.getBody();
        }
        return null;
    }


    /**
     * post请求
     *
     * @param url        url
     * @param paramsMap  参数
     * @param headersMap Header
     * @param resType    返回类型
     * @return 返回类型
     */
    public static <T> T post(String url, Map<String, Object> paramsMap, Map<String, String> headersMap, Class<T> resType) {
        return postMethod(url, paramsMap, headersMap, resType, null);
    }

    /**
     * post请求
     *
     * @param url          url
     * @param paramsMap    参数
     * @param headersMap   Header
     * @param responseType 返回类型（泛型）
     * @return 返回类型
     */
    public static <T> T post(String url, Map<String, Object> paramsMap, Map<String, String> headersMap, ParameterizedTypeReference<T> responseType) {
        return postMethod(url, paramsMap, headersMap, null, responseType);
    }


    /**
     * post 表单提交。 application/x-www-form-urlencoded
     *
     * @param url        url
     * @param paramsMap  参数映射
     * @param headersMap 头图
     * @param resType    res类型
     * @return 返回类型
     */
    private static <T> T postMethod(String url, Map<String, Object> paramsMap, Map<String, String> headersMap, Class<T> resType, ParameterizedTypeReference<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        if (headersMap != null && !headersMap.isEmpty()) {
            headersMap.forEach(headers::add);
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (paramsMap != null && !paramsMap.isEmpty()) {
            for (Map.Entry<String, Object> param : paramsMap.entrySet()) {
                map.add(param.getKey(), param.getValue().toString());
            }
        }
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        if (resType != null) {
            ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.POST, entity, resType);
            return result.getBody();
        }
        if (responseType != null) {
            ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
            return result.getBody();
        }
        return null;
    }


    /**
     * postJson 请求
     *
     * @param url          url
     * @param jsonParamStr json字符串参数
     * @param headersMap   Header
     * @param resType      返回类型
     * @return 返回类型
     */
    public static <T> T postJson(String url, String jsonParamStr, Map<String, String> headersMap, Class<T> resType) {
        return postJsonMethod(url, jsonParamStr, headersMap, resType, null);
    }

    /**
     * postJson 请求
     *
     * @param url          url
     * @param jsonParamStr json字符串参数
     * @param headersMap   Header
     * @param responseType 返回类型（泛型）
     * @return 返回类型
     */
    public static <T> T postJson(String url, String jsonParamStr, Map<String, String> headersMap, ParameterizedTypeReference<T> responseType) {
        return postJsonMethod(url, jsonParamStr, headersMap, null, responseType);
    }


    /**
     * post json提交。 application/json
     *
     * @param url          url
     * @param jsonParamStr json字符串参数
     * @param headersMap   header
     * @param resType      返回类型
     * @return 返回类型
     */
    private static <T> T postJsonMethod(String url, String jsonParamStr, Map<String, String> headersMap, Class<T> resType, ParameterizedTypeReference<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (headersMap != null && !headersMap.isEmpty()) {
            headersMap.forEach(headers::add);
        }
        HttpEntity<String> entity = new HttpEntity<>(jsonParamStr, headers);
        if (resType != null) {
            ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.POST, entity, resType);
            return result.getBody();
        }
        if (responseType != null) {
            ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
            return result.getBody();
        }
        return null;
    }


}
