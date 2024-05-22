package cn.acyou.leo.framework.util.component;

import cn.acyou.leo.framework.prop.OpenApiProperty;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author youfang
 * @version [1.0.0, 2024/1/12]
 **/
@Slf4j
public class OpenApiUtil {

    private OpenApiProperty openApiProperty;

    public OpenApiUtil() {

    }

    public OpenApiUtil(OpenApiProperty openApiProperty) {
        this.openApiProperty = openApiProperty;
    }

    public void setOpenApiProperty(OpenApiProperty openApiProperty) {
        this.openApiProperty = openApiProperty;
    }

    /**
     * 查找城市
     * @param location 城市编码
     * @return 天气信息
     */
    public JSONObject lookup(String location){
        String url = String.format("https://geoapi.qweather.com/v2/city/lookup?key=%s&location=%s", openApiProperty.getQweatherKey(), location);
        final String s = HttpUtil.get(url);
        return JSON.parseObject(s);
    }

    /**
     * 实时天气
     * @param location 城市编码
     * @return 天气信息
     */
    public JSONObject weatherNow(String location){
        String url = String.format("https://devapi.qweather.com/v7/weather/now?key=%s&location=%s", openApiProperty.getQweatherKey(), location);
        final String s = HttpUtil.get(url);
        return JSON.parseObject(s);
    }

    /**
     * 三天天气
     * @param location 城市编码
     * @return 天气信息
     */
    public JSONObject weather3d(String location){
        String url = String.format("https://devapi.qweather.com/v7/weather/3d?key=%s&location=%s", openApiProperty.getQweatherKey(), location);
        final String s = HttpUtil.get(url);
        return JSON.parseObject(s);
    }

    /**
     * 七天天气
     * @param location 城市编码
     * @return 天气信息
     */
    public JSONObject weather7d(String location){
        String url = String.format("https://devapi.qweather.com/v7/weather/7d?key=%s&location=%s", openApiProperty.getQweatherKey(), location);
        final String s = HttpUtil.get(url);
        return JSON.parseObject(s);
    }

    /**
     * 查询手机归属地
     *
     * {"手机号码":"18779657999","归属地":"江西省吉安市","邮编":"343000","电话区号":"0796","行政区划代码":"360800","运营商":"中国移动"}
     *
     * @param phone 手机号
     * @return {@link JSONObject}
     */
    public JSONObject getPhoneArea(String phone){
        String s = HttpUtil.get("https://www.haoshudi.com/"+phone+".htm");
        Document document = Jsoup.parse(s);
        Elements tables = document.select("table tr");
        JSONObject res = new JSONObject();
        for (Element table : tables) {
            String k = table.select("td").get(0).text();
            String v = table.select("td").get(1).text().split(" ")[0];
            res.put(k, v);
        }
        return res;
    }


}
