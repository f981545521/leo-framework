package cn.acyou.leo.framework.util.component;

import cn.acyou.leo.framework.prop.TencentMapProperty;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * [腾讯地图Rest服务](https://lbs.qq.com/service/webService/webServiceGuide/webServiceOverview)
 *
 * @author youfang
 * @version [1.0.0, 2024/1/12]
 **/
@Slf4j
public class TencentMapUtil {
    public static final Integer SUCCESS = 0;

    private final String key;

    public TencentMapUtil(TencentMapProperty tencentMapProperty) {
        this.key = tencentMapProperty.getKey();
    }

    /**
     * 通过终端设备IP地址获取其当前所在地理位置，常用于显示当地城市天气预报、初始化用户城市等非精确定位场景。
     * 接口文档地址：
     * https://lbs.qq.com/service/webService/webServiceGuide/webServiceIp
     * 示例
     *      {
     *         "status": 0,
     *             "message": "Success",
     *             "request_id": "0026efdf2d3e48789b90921fb63ab5f0",
     *             "result": {
     *         "ip": "112.2.230.194",
     *                 "location": {
     *             "lat": 31.99226,
     *                     "lng": 118.7787
     *         },
     *         "ad_info": {
     *             "nation": "中国",
     *                     "province": "江苏省",
     *                     "city": "南京市",
     *                     "district": "雨花台区",
     *                     "adcode": 320114,
     *                     "nation_code": 156
     *         }
     *     }
     *     }
     * @param ip ip地址
     * @return 地址信息
     */
    public JSONObject getIpLocation(String ip){
        final String url = String.format("https://apis.map.qq.com/ws/location/v1/ip?key=%s&ip=%s", key, ip);
        final String s = HttpUtil.get(url);
        final JSONObject res = JSON.parseObject(s);
        if (SUCCESS.equals(res.getInteger("status"))) {
            return res.getJSONObject("result");
        }else {
            return res;
        }
    }

}
