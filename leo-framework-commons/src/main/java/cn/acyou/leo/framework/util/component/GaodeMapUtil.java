package cn.acyou.leo.framework.util.component;

import cn.acyou.leo.framework.prop.GaodeMapProperty;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * [高德地图Rest服务](https://lbs.amap.com/api/webservice/guide/api/ipconfig)
 *
 * @author youfang
 * @version [1.0.0, 2024/1/12]
 **/
@Slf4j
public class GaodeMapUtil {
    public static final Integer SUCCESS = 0;

    private final String key;

    public GaodeMapUtil(GaodeMapProperty gaodeMapProperty) {
        this.key = gaodeMapProperty.getKey();
    }

    /**
     * 通过终端设备IP地址获取其当前所在地理位置
     *
     * {
     *   "status": "1",
     *   "info": "OK",
     *   "infocode": "10000",
     *   "province": "江苏省",
     *   "city": "南京市",
     *   "adcode": "320100",
     *   "rectangle": "118.4253323,31.80452471;119.050169,32.39401346"
     * }
     * @param ip ip地址
     * @return 地址信息
     */
    public JSONObject getIpLocation(String ip){
        String url = String.format("https://restapi.amap.com/v3/ip?key=%s", key);
        if (ip != null) {
            url = url + "&ip=" + ip;
        }
        final String s = HttpUtil.get(url);
        return JSON.parseObject(s);
    }

    /**
     * 实时天气
     * {"status":"1","count":"1","info":"OK","infocode":"10000","lives":[{"province":"江苏","city":"南京市","adcode":"320100","weather":"阴","temperature":"24","winddirection":"西","windpower":"≤3","humidity":"53","reporttime":"2024-04-23 15:01:47","temperature_float":"24.0","humidity_float":"53.0"}]}
     *
     * @param adcode 城市编码
     * @return 天气信息
     */
    public JSONObject weatherInfoLive(String adcode){
        String url = String.format("https://restapi.amap.com/v3/weather/weatherInfo?key=%s&city=%s", key, adcode);
        final String s = HttpUtil.get(url);
        return JSON.parseObject(s);
    }

    /**
     * 预测天气
     * @param adcode 城市编码
     * @return 天气信息
     */
    public JSONObject weatherInfoForecasts(String adcode){
        String url = String.format("https://restapi.amap.com/v3/weather/weatherInfo?key=%s&city=%s&extensions=all", key, adcode);
        final String s = HttpUtil.get(url);
        return JSON.parseObject(s);
    }


}
