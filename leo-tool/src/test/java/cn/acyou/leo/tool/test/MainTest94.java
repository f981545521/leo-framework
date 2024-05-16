package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.util.JSONUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * @author youfang
 * @version [1.0.0, 2024/5/16 10:18]
 **/
public class MainTest94 {
    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 123);
        jsonObject.put("age", 23);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("id", 200);
        jsonObject1.put("name", "小飞");

        System.out.println(JSONUtil.merge(jsonObject, jsonObject1, false));
    }
}
