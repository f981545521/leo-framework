package cn.acyou.leo.framework.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2023/2/27 15:40]
 **/
@Slf4j
public class JSONUtil {

    /**
     * 覆盖 合并两个JSON属性
     *
     * @return {@link JSONObject}
     */
    public static JSONObject merge(String sourceStr, String targetStr) {
        return WorkUtil.swallowException(() -> {
            JSONObject source = JSON.parseObject(sourceStr);
            JSONObject target = JSON.parseObject(targetStr);
            return merge(source, target, true);
        });
    }

    /**
     * 覆盖 合并两个JSON属性
     *
     * @param source 源
     * @param target 目标
     * @return {@link JSONObject}
     */
    public static JSONObject merge(JSONObject source, JSONObject target) {
        return merge(source, target, true);
    }

    /**
     * 合并两个JSON属性
     *
     * @param source 源
     * @param target 目标
     * @param cover 覆盖
     * @return {@link JSONObject}
     */
    public static JSONObject merge(JSONObject source, JSONObject target, boolean cover) {
        if (cover) {
            source.putAll(target);
        } else {
            Map<String, Object> innerMap_source = source.getInnerMap();
            Map<String, Object> innerMap_target = target.getInnerMap();
            innerMap_target.forEach((x, v) -> {
                if (!innerMap_source.containsKey(x)) {
                    source.put(x, v);
                }
            });
        }
        return source;
    }

    /**
     * 从JSON字符串 根据键获取值
     *
     * @param str str
     * @param key 键
     * @return {@link String} （字符串）
     */
    public static String getByKey(String str, String key) {
        if (StringUtils.isNotBlank(str)) {
            try {
                JSONObject jsonObject = JSON.parseObject(str);
                return jsonObject.getString(key);
            } catch (Exception e) {
                log.error("解析JSON出错", e);
            }
        }
        return null;
    }

    /**
     * 从JSON字符串 根据键获取值
     * <p></p>
     * <strong>注意：必须是确认的类型</strong>
     *
     * @param str str
     * @param key 键
     * @return {@link T} （类型）
     */
    @SuppressWarnings("unchecked")
    public static <T> T getObjectByKey(String str, String key) {
        if (StringUtils.isNotBlank(str)) {
            try {
                JSONObject jsonObject = JSON.parseObject(str);
                return (T) jsonObject.get(key);
            } catch (Exception e) {
                log.info("获取属性出错", e);
            }
        }
        return null;
    }

    /**
     * 为JSON字符串设置键值
     *
     * @param str str
     * @param key 键
     * @param val 值
     * @return {@link String}
     */
    public static String setByKey(String str, String key, Object val) {
        JSONObject obj = new JSONObject();
        if (StringUtils.isNotBlank(str)) {
            try {
                obj = JSON.parseObject(str);
            } catch (Exception e) {
                log.error("解析JSON出错", e);
            }
        }
        obj.put(key, val);
        return obj.toJSONString();
    }


    /**
     * 获取JSONP JSON
     *
     * @param jsonpStr jsonpStr
     * @return JSON
     */
    public static String getJsonPJsonStr(String jsonpStr) {
        int startIndex = jsonpStr.indexOf("(");
        int endIndex = jsonpStr.lastIndexOf(")");
        return jsonpStr.substring(startIndex + 1, endIndex);
    }

    /**
     * 获取JSONP (JSONObject)
     *
     * @param jsonpStr jsonpStr
     * @return JSON
     */
    public static JSONObject getJsonPJsonObject(String jsonpStr) {
        String jsonPJsonStr = getJsonPJsonStr(jsonpStr);
        return JSON.parseObject(jsonPJsonStr);
    }

    /**
     * 获取JSONP (JSONArray)
     *
     * @param jsonpStr jsonpStr
     * @return JSON
     */
    public static JSONArray getJsonPJsonArray(String jsonpStr) {
        String jsonPJsonStr = getJsonPJsonStr(jsonpStr);
        return JSON.parseArray(jsonPJsonStr);
    }
}
