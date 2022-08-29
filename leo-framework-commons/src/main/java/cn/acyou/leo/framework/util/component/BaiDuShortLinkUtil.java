package cn.acyou.leo.framework.util.component;

import cn.acyou.leo.framework.prop.BaiDuShortLinkProperty;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


/**
 * 百度短链转换
 *
 * @author youfang
 */
@Slf4j
public class BaiDuShortLinkUtil {

    private final String token;

    private static final String BAI_DU_URL = "https://dwz.cn/api/v3/short-urls";

    public BaiDuShortLinkUtil(BaiDuShortLinkProperty baiDuShortLinkProperty) {
        this.token = baiDuShortLinkProperty.getToken();
    }

    public String getShortLink(String url) {
        if (StringUtils.isBlank(url)) {
            return url;
        }

        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("LongUrl", url);
        jsonObject.put("TermOfValidity", "long-term");
        jsonArray.add(jsonObject);
        /*
         * {
         *     "Code": -99,
         *     "ShortUrls": [
         *         {
         *             "ShortUrl": "https://dwz.cn/de3rp2Fl",
         *             "LongUrl": "https://www.baidu.com"
         *         },
         *         {
         *             "Code": -11,
         *             "LongUrl": "https://notexsit.dwz.cn",
         *             "ErrMsg": "long URL with unsupported host"
         *         },
         *     ],
         *     "ErrMsg": "partial fail"
         * }
         */
        try {
            HttpResponse httpResponse = HttpUtil.createPost(BAI_DU_URL)
                    .header("Dwz-Token", token)
                    .header("Content-Language", "zh")
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(jsonArray.toJSONString())
                    .execute();
            JSONObject response = JSON.parseObject(httpResponse.body(), JSONObject.class);
            log.info("获取百度短链 返回参数 {}", response);
            if (response != null && response.getInteger("Code") == 0) {
                JSONArray shortUrls = response.getJSONArray("ShortUrls");
                if (CollectionUtil.isNotEmpty(shortUrls)) {
                    url = shortUrls.getJSONObject(0).getString("ShortUrl");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return url;
        }
        return url;
    }
}
