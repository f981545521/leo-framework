package cn.acyou.leo.framework.util.component;

import cn.acyou.leo.framework.prop.OpenApiProperty;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author youfang
 * @version [1.0.0, 2024/1/12]
 **/
@Slf4j
public class OpenApiUtil {

    private OpenApiProperty openApiProperty;

    public OpenApiUtil(OpenApiProperty openApiProperty) {
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


}
