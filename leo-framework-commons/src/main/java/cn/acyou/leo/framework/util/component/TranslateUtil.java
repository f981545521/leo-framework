package cn.acyou.leo.framework.util.component;

import cn.acyou.leo.framework.prop.TranslateProperty;
import cn.acyou.leo.framework.util.Md5Util;
import cn.acyou.leo.framework.util.SHAUtil;
import cn.acyou.leo.framework.util.WorkUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


/**
 * 翻译
 *
 * @author youfang
 */
@Slf4j
public class TranslateUtil {

    private final TranslateProperty translateProperty;

    // 有道翻译开放接口
    private static final String TRANS_API_HOST = "https://openapi.youdao.com/api";
    // 百度翻译开放接口
    private static final String BAIDU_TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    // 百度语种识别开放接口
    private static final String BAIDU_LANGUAGE_API_HOST = "https://fanyi-api.baidu.com/api/trans/vip/language";

    public TranslateUtil(TranslateProperty translateProperty) {
        this.translateProperty = translateProperty;
    }


    /**
     * 【百度翻译】https://api.fanyi.baidu.com/product/113
     *
     * @param q    待翻译文本
     * @return 翻译文本
     */
    public String baiduLanguage(String q) {
        Map<String, Object> params = new HashMap<>();
        params.put("q", q);
        params.put("appid", translateProperty.getBaiduAppId());
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);
        String src = translateProperty.getBaiduAppId() + q + salt + translateProperty.getBaiduSecurityKey();
        params.put("sign", Md5Util.md5(src));
        String res = HttpUtil.get(BAIDU_LANGUAGE_API_HOST, params);
        log.info("百度翻译(语种识别) 结果 q:{} res:{}", q, res);
        JSONObject jsonObject = JSON.parseObject(res);
        //速度限制：{"error_code":"54003","error_msg":"Invalid Access Limit"}
        String error_code = jsonObject.getString("error_code");
        if ("54003".equals(error_code)) {
            WorkUtil.trySleep(1000);
            return baiduLanguage(q);
        }
        return jsonObject.getJSONObject("data").getString("src");
    }

    /**
     * 【百度翻译】https://api.fanyi.baidu.com/product/113
     *
     * @param q    待翻译文本
     * @param from 源语言 (可设置为auto)
     * @param to   目标语言
     * @return 翻译文本
     */
    public String baiduTranslate(String q, String from, String to) {
        Map<String, Object> params = new HashMap<>();
        params.put("q", q);
        params.put("from", from);
        params.put("to", to);
        params.put("appid", translateProperty.getBaiduAppId());
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);
        String src = translateProperty.getBaiduAppId() + q + salt + translateProperty.getBaiduSecurityKey();
        params.put("sign", Md5Util.md5(src));
        String res = HttpUtil.get(BAIDU_TRANS_API_HOST, params);
        log.info("百度翻译 结果 q:{} res:{}", q, res);
        JSONObject jsonObject = JSON.parseObject(res);
        //速度限制：{"error_code":"54003","error_msg":"Invalid Access Limit"}
        String error_code = jsonObject.getString("error_code");
        if ("54003".equals(error_code)) {
            WorkUtil.trySleep(1000);
            return baiduTranslate(q, from, to);
        }
        return jsonObject.getJSONArray("trans_result").getJSONObject(0).getString("dst");
    }


    /**
     * 【有道翻译】https://ai.youdao.com/DOCSIRMA/html/trans/api/wbfy/index.html
     *
     * @param q    待翻译文本
     * @param from 源语言 (可设置为auto)
     * @param to   目标语言
     * @return 翻译文本
     */
    public String youdaoTranslate(String q, String from, String to) {
        Map<String, Object> params = new HashMap<>();
        params.put("q", q);
        params.put("from", from);
        params.put("to", to);
        //计算方式 signType=v3 : sign = sha256(appKey + input(q) + salt + curtime + appSecret)
        //其中，input的计算方式为：input=q前10个字符 + q长度 + q后10个字符（当q长度大于20）或 input=q字符串（当q长度小于等于20）；
        String salt = String.valueOf(System.currentTimeMillis());
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        String sb = translateProperty.getYoudaoAppKey() +
                getInput(params.get("q").toString()) +
                salt +
                curtime +
                translateProperty.getYoudaoAppSecret();
        String sha256 = SHAUtil.getSHA256(sb);
        params.put("salt", salt);
        params.put("appKey", translateProperty.getYoudaoAppKey());
        params.put("curtime", curtime);
        params.put("signType", "v3");
        params.put("sign", sha256);
        String s = HttpUtil.get(TRANS_API_HOST, params);
        log.info("有道翻译 结果 q:{} res:{}", q, s);
        JSONObject jsonObject = JSON.parseObject(s);
        //速度限制
        String errorCode = jsonObject.getString("errorCode");
        if (errorCode != null && errorCode.endsWith("411")) {
            WorkUtil.trySleep(1000);
            return youdaoTranslate(q, from, to);
        }
        return jsonObject.getJSONArray("translation").getString(0);
    }


    private static String getInput(String input) {
        if (input == null) {
            return null;
        }
        String result;
        int len = input.length();
        if (len <= 20) {
            result = input;
        } else {
            String startStr = input.substring(0, 10);
            String endStr = input.substring(len - 10, len);
            result = startStr + len + endStr;
        }
        return result;
    }

    public String baidu_langdetect(String query) {
        String s = HttpUtil.get("https://fanyi.baidu.com/langdetect?query=" + query);
        return JSON.parseObject(s).getString("lan");
    }

    public JSONArray baidu_sug(String kw) {
        String s = HttpUtil.get("https://fanyi.baidu.com/sug?kw=" + kw);
        return JSON.parseObject(s).getJSONArray("data");
    }

    public String baidu_translate(String query, String from, String to) {
        String res = cn.hutool.http.HttpUtil.createPost("https://fanyi.baidu.com/ait/text/translate")
                .body("{\n" +
                        "  \"query\": \""+query+"\",\n" +
                        "  \"from\": \""+from+"\",\n" +
                        "  \"to\": \""+to+"\",\n" +
                        "  \"reference\": \"\",\n" +
                        "  \"corpusIds\": [],\n" +
                        "  \"qcSettings\": [],\n" +
                        "  \"needPhonetic\": false,\n" +
                        "  \"domain\": \"common\",\n" +
                        "  \"milliTimestamp\": "+System.currentTimeMillis()+"\n" +
                        "}")
                .execute().body();
        String[] split = res.split("\n");
        for (String s : split) {
            if (s.contains("翻译中")) {
                JSONObject jsonObject = JSON.parseObject(s.substring(6));
                return jsonObject.getJSONObject("data").getJSONArray("list").getJSONObject(0).getString("dst");
            }
        }
        return "";
    }
}
