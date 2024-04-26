package cn.acyou.leo.tool.test;


import cn.acyou.leo.framework.oss.OSSUtil;
import cn.acyou.leo.framework.prop.OSSProperty;
import cn.acyou.leo.framework.test.TestUtils;
import cn.acyou.leo.framework.util.FileUtil;
import cn.acyou.leo.framework.util.UrlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author youfang
 * @version [1.0.0, 2024/4/26 9:14]
 **/
@Slf4j
public class MainTest201 {
    private static final AtomicLong idGenerator = new AtomicLong(1);
    private static final JSONArray jsonArray = new JSONArray();

    public static void main(String[] args) {
        TestUtils.loadExtendProperties();
        //File path = new File("D:\\TranslateVideos");
        //File path = new File("E:\\");
        List<String> pathList= Lists.newArrayList("E:\\");
        for (String pathStr : pathList) {
            File path = new File(pathStr);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", idGenerator.getAndAdd(1));
            jsonObject.put("parentId", 0);
            jsonObject.put("name", path.getAbsolutePath());
            jsonObject.put("isFolder", true);
            jsonObject.put("open", true);
            jsonArray.add(jsonObject);
            copyIndexName(path, jsonObject);
        }


        System.out.println("+++++++++++++  解析文件成功  +++++++++++++");
        String content = "window.ztreeConfig = '" + UrlUtil.encode( JSON.toJSONString(jsonArray)) + "'";
        final byte[] bytes = content.getBytes();
        OSSProperty ossProperty = new OSSProperty();
        ossProperty.setEndpoint("https://oss-cn-beijing.aliyuncs.com");
        ossProperty.setAccessKeyId(System.getProperty("accessKeyID"));
        ossProperty.setAccessKeySecret(System.getProperty("accessKeySecret"));
        OSSUtil ossUtil = new OSSUtil(ossProperty);
        ossUtil.uploadByte("dev-acyou-cn", System.getProperty("ztreeConfigPath"), bytes);
        System.out.println("+++++++++++++  上传oss成功  +++++++++++++");
    }

    private static void copyIndexName(File dir, JSONObject current){
        FileUtil.listFilesV2(dir, f ->{
            if (!f.isHidden()) {
                if (f.isDirectory() && !f.getName().contains("ignore")) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", idGenerator.getAndAdd(1));
                    jsonObject.put("parentId", current.getInteger("id"));
                    jsonObject.put("name", f.getAbsolutePath());
                    jsonObject.put("isFolder", true);
                    jsonArray.add(jsonObject);
                    copyIndexName(f, jsonObject);
                }
                if (f.isFile()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", idGenerator.getAndAdd(1));
                    jsonObject.put("parentId", current.getInteger("id"));
                    jsonObject.put("name", f.getAbsolutePath());
                    jsonObject.put("isFolder", false);
                    jsonArray.add(jsonObject);

                }
            }
        });
    }

}
