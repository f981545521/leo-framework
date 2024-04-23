package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.prop.GaodeMapProperty;
import cn.acyou.leo.framework.prop.OpenApiProperty;
import cn.acyou.leo.framework.prop.TranslateProperty;
import cn.acyou.leo.framework.util.FileUtil;
import cn.acyou.leo.framework.util.LoggerUtil;
import cn.acyou.leo.framework.util.PinYinHelper;
import cn.acyou.leo.framework.util.component.GaodeMapUtil;
import cn.acyou.leo.framework.util.component.OpenApiUtil;
import cn.acyou.leo.framework.util.component.TranslateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2023/8/8 10:20]
 **/
public class MainTest5 {

    @Test
    public void test123() {
        JSONArray jsonArray = new JSONArray();
        File fileDir = new File("C:\\Users\\1\\Downloads\\2023_08_08_故事");
        String[] list = fileDir.list();
        for (String s : list) {
            JSONObject object = new JSONObject();
            object.put("name", s);
            object.put("key", PinYinHelper.converterToSpell(s));
            //object.put("key", "live");
            jsonArray.add(object);
        }
        System.out.println(jsonArray.toJSONString());
    }

    @Test
    public void test12() throws Exception {
        JSONArray jsonArray = new JSONArray();
        File fileDir = new File("C:\\Users\\1\\Downloads\\2023_08_08_故事\\闲聊");
        String name = fileDir.getName();
        List<String> contentList = new ArrayList<>();
        System.out.println(name + "     " + PinYinHelper.converterToSpell(name));
        File file = fileDir.listFiles()[1];
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.getName().contains("txt")) {
                //System.out.println(file1);
                List<String> strings = FileUtil.readLines(file1, StandardCharsets.UTF_8);
                for (int i = 0; i < strings.size(); i = i + 2) {
                    String txt = strings.get(i);
                    //System.out.println(txt);
                    String content = txt.split("\t")[1];
                    if (content.length() > 15 && contentList.size() < 200 && !content.contains("【")) {
                        //System.out.println(content);
                        contentList.add(content);
                        JSONObject object = new JSONObject();
                        object.put("index", contentList.size());
                        object.put("text", content);
                        jsonArray.add(object);
                    }
                }

            }
        }
        System.out.println(contentList.size());
        System.out.println(jsonArray.toJSONString());
    }

    @Test
    public void test234(){
        LoggerUtil.setLevel("cn.acyou.leo.framework.util.component.TranslateUtil", "OFF");

        TranslateProperty translateProperty = new TranslateProperty();
        translateProperty.setYoudaoAppKey("501062f86ee03660");
        translateProperty.setYoudaoAppSecret("aKk1aZgheXsoQlhux129J7LHIG5PUuGN");
        translateProperty.setBaiduAppId("20190808000324958");
        translateProperty.setBaiduSecurityKey("2i7Z8KQQitJVt1mzn1fI");
        TranslateUtil translateUtil = new TranslateUtil(translateProperty);
        //有道翻译
        System.out.println(translateUtil.youdaoTranslate("你在哪里", "zh", "en"));
        System.out.println(translateUtil.youdaoTranslate("吃饭了吗", "zh", "en"));
        System.out.println(translateUtil.youdaoTranslate("明天有什么计划吗？", "zh", "en"));
        //百度翻译
        System.out.println(translateUtil.baiduTranslate("你在哪里", "zh", "en"));
        System.out.println(translateUtil.baiduTranslate("吃饭了吗", "zh", "en"));
        System.out.println(translateUtil.baiduTranslate("明天有什么计划吗？", "zh", "en"));
        //百度获取语种
        System.out.println(translateUtil.baiduLanguage("你在哪里"));
        System.out.println(translateUtil.baiduLanguage("where are you"));

        String text = "推荐\n" +
                "抖音爆款\n" +
                "超级主播\n" +
                "情绪主播\n" +
                "配音热门\n" +
                "影视解说\n" +
                "客服助手\n" +
                "情感语录";
        String[] split = text.split("\n");
        for (String s : split) {
            String string_en = translateUtil.youdaoTranslate(s, "zh", "en");
            String string_jp = translateUtil.youdaoTranslate(s, "zh", "ja");
            String sql = "UPDATE `tts-market`.tts_label_order " +
                    "\tSET ext='{\"label_name_zh\":\""+s+"\",\"label_name_en\":\""+string_en+"\",\"label_name_jp\":\""+string_jp+"\"}' " +
                    "\tWHERE label_type = 'df_category' and label_name='"+s+"';";
            System.out.println(sql);
        }
    }


    @Test
    public void test23445(){
        GaodeMapProperty gaodeMapProperty = new GaodeMapProperty();
        gaodeMapProperty.setKey("220da8558e8e5***");
        GaodeMapUtil gaodeMapUtil = new GaodeMapUtil(gaodeMapProperty);
        JSONObject ipLocation = gaodeMapUtil.getIpLocation(null);
        //{"province":"江苏省","city":"南京市","adcode":"320100","infocode":"10000","rectangle":"118.4253323,31.80452471;119.050169,32.39401346","status":"1","info":"OK"}
        System.out.println(ipLocation);
        String province = ipLocation.getString("province");
        String city = ipLocation.getString("city");
        String adcode = ipLocation.getString("adcode");
        JSONObject weatherInfoLive = gaodeMapUtil.weatherInfoLive(adcode);
        JSONObject weatherInfoForecasts = gaodeMapUtil.weatherInfoForecasts(adcode);
        if ("1".equals(weatherInfoLive.getString("status"))) {
            JSONObject lives = weatherInfoLive.getJSONArray("lives").getJSONObject(0);
            //天气现象（汉字描述）
            String weather = lives.getString("weather");
            //实时气温，单位：摄氏度
            String temperature = lives.getString("temperature");
            //风向描述
            String winddirection = lives.getString("winddirection");
            //风力级别，单位：级
            String windpower = lives.getString("windpower");
            //空气湿度
            String humidity = lives.getString("humidity");
            //数据发布的时间
            String reporttime = lives.getString("reporttime");
            System.out.println("ok");
        }
        System.out.println("ok");
    }

    @Test
    public void  testrs234(){
        OpenApiProperty openApiProperty = new OpenApiProperty();
        openApiProperty.setQweatherKey("caf54ef232");
        OpenApiUtil openApiUtil = new OpenApiUtil(openApiProperty);
        JSONObject jsonObject = openApiUtil.lookup("南京");
        String location_id = jsonObject.getJSONArray("location").getJSONObject(0).getString("id");
        JSONObject jsonObject1 = openApiUtil.weatherNow(location_id);
        JSONObject jsonObject2 = openApiUtil.weather3d(location_id);
        JSONObject jsonObject3 = openApiUtil.weather7d(location_id);
        System.out.println(jsonObject);
        System.out.println(jsonObject1);
        System.out.println(jsonObject2);
        System.out.println(jsonObject3);
        System.out.println("ok");

    }


}
