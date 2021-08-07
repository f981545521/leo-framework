package cn.acyou.leo.framework.util;

import java.io.File;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/29]
 **/
public class FileUtil {
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
            return StringUtil.EMPTY;
        } else {
            return fileName.substring(index + 1);
        }
    }
}
