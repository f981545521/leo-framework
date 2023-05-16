package cn.acyou.leo.framework.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author youfang
 * @version [1.0.0, 2023/2/27 15:40]
 **/
@Slf4j
public class JSONUtil {

    public static String getByKey(String str, String key) {
        if (StringUtils.isNotBlank(str)) {
            try {
                JSONObject jsonObject = JSON.parseObject(str);
                return jsonObject.getString(key);
            } catch (Exception e) {
                log.error("获取属性出错了", e);
            }
        }
        return null;
    }

    public static String setByKey(String str, String key, String val) {
        JSONObject obj = new JSONObject();
        if (StringUtils.isNotBlank(str)) {
            try {
                obj = JSON.parseObject(str);
            } catch (Exception e) {
                log.error("解析字符串属性出错了", e);
            }
        }
        obj.put(key, val);
        return obj.toJSONString();
    }
}
