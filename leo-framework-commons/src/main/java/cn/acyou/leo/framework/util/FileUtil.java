package cn.acyou.leo.framework.util;

import com.google.common.collect.Lists;

import java.io.File;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/29]
 **/
public class FileUtil {
    private static List<String> imageTypes = Lists.newArrayList("png", "jpg", "jpeg");
    /**
     * 获取文件扩展名，扩展名不带“.”
     *
     * @param file 文件
     * @return 扩展名
     */
    public static String extName(File file) {
        if (null == file) {
            return null;
        }
        if (file.isDirectory()) {
            return null;
        }
        return extName(file.getName());
    }

    /**
     * 获取文件扩展名，扩展名不带“.”
     *
     * @param fileName 文件名称
     * @return {@link String}
     */
    public static String extName(String fileName) {
        if (fileName == null) {
            return null;
        }
        int index = fileName.lastIndexOf(StringUtil.DOT);
        if (index == -1) {
            return null;
        } else {
            return fileName.substring(index + 1);
        }
    }

    public static boolean isImage(String fileName){
        String extName = extName(fileName);
        if (extName == null) {
            return false;
        }
        return imageTypes.contains(extName.toLowerCase());
    }
}
