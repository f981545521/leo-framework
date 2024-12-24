package cn.acyou.leo.tool.test;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

/**
 * @author youfang
 * @version [1.0.0, 2024/12/20 9:16]
 **/
public class MainTest331 {
    public static void main(String[] args) {
        String jsonString = "[{\"name\":\"John\",\"age\":30}, {\"name\":\"Jane\",\"age\":25}]";
        String jsonObjString = "{\"name\":\"John\",\"age\":30}";
        int defaultParserFeature = JSON.DEFAULT_PARSER_FEATURE;
        System.out.println(defaultParserFeature);
        JSON.DEFAULT_PARSER_FEATURE = Feature.config(JSON.DEFAULT_PARSER_FEATURE, Feature.OrderedField, true);
        JSONArray objects = JSON.parseArray(jsonString);
        JSONObject jsonObject = JSON.parseObject(jsonObjString, Feature.OrderedField);
        System.out.println(objects);
        System.out.println(jsonObject);
    }
}
