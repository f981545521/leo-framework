package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.util.Calculator;
import cn.acyou.leo.framework.util.FileUtil;
import cn.acyou.leo.framework.util.PinYinHelper;
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


}
