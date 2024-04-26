package cn.acyou.leo.tool.test;


import cn.acyou.leo.framework.util.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author youfang
 * @version [1.0.0, 2024/4/26 9:14]
 **/
@Slf4j
public class MainTest200 {
    private static final AtomicLong idGenerator = new AtomicLong(1);
    private static final JSONArray jsonArray = new JSONArray();

    public static void main(String[] args) {
        File path = new File("D:\\TranslateVideos");
        File indexPath = new File("D:\\TranslateVideos_index");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", idGenerator.getAndAdd(1));
        jsonObject.put("parentId", 0);
        jsonObject.put("name", path.getAbsolutePath());
        jsonObject.put("isFolder", true);
        jsonObject.put("open", true);
        jsonArray.add(jsonObject);

        copyIndexName(path, indexPath, jsonObject);

        System.out.println("+++++++++++++  read file end  +++++++++++++");
        String filePrefix = "window.ztreeConfig = ";
        String content = filePrefix + JSON.toJSONString(jsonArray);
        final byte[] bytes = content.getBytes();
        File file = new File("D:\\TranslateVideos_index1\\config.js");
        FileUtil.writeBytes(bytes, file);
        System.out.println("+++++++++++++  write file end  +++++++++++++");
    }

    private static void copyIndexName(File dir, File targetDir, JSONObject current){
        FileUtil.deleteRecursively(targetDir);
        FileUtil.listFilesV2(dir, f ->{
            File newPathFile = new File(f.getAbsolutePath().replace(dir.getAbsolutePath(), targetDir.getAbsolutePath()));
            if (!f.isHidden()) {
                if (f.isDirectory()) {
                    FileUtil.mkdir(newPathFile);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", idGenerator.getAndAdd(1));
                    jsonObject.put("parentId", current.getInteger("id"));
                    jsonObject.put("name", newPathFile.getAbsolutePath());
                    jsonObject.put("isFolder", true);
                    jsonArray.add(jsonObject);

                    copyIndexName(f, newPathFile, jsonObject);
                }
                if (f.isFile()) {
                    FileUtil.touch(newPathFile + ".txt");

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", idGenerator.getAndAdd(1));
                    jsonObject.put("parentId", current.getInteger("id"));
                    jsonObject.put("name", newPathFile.getAbsolutePath());
                    jsonObject.put("isFolder", false);
                    jsonArray.add(jsonObject);

                }
            }
        });
    }

}
