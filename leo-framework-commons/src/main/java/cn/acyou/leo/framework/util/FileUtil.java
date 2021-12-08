package cn.acyou.leo.framework.util;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author youfang
 * @version [1.0.0, 2020/7/29]
 **/
public class FileUtil {

    private static final Map<String, String> FILE_TYPE_MAP;

    /**
     * 未知类型
     */
    public static final String UNKNOWN = "UNKNOWN";

    static {
        FILE_TYPE_MAP = new ConcurrentSkipListMap<>((s1, s2) -> {
            int len1 = s1.length();
            int len2 = s2.length();
            if (len1 == len2) {
                return s1.compareTo(s2);
            } else {
                return len2 - len1;
            }
        });

        FILE_TYPE_MAP.put("ffd8ff", "jpg"); // JPEG (jpg)
        FILE_TYPE_MAP.put("89504e47", "png"); // PNG (png)
        FILE_TYPE_MAP.put("4749463837", "gif"); // GIF (gif)
        FILE_TYPE_MAP.put("4749463839", "gif"); // GIF (gif)
        FILE_TYPE_MAP.put("49492a00227105008037", "tif"); // TIFF (tif)
        FILE_TYPE_MAP.put("424d228c010000000000", "bmp"); // 16色位图(bmp)
        FILE_TYPE_MAP.put("424d8240090000000000", "bmp"); // 24色位图(bmp)
        FILE_TYPE_MAP.put("424d8e1b030000000000", "bmp"); // 256色位图(bmp)
        FILE_TYPE_MAP.put("41433130313500000000", "dwg"); // CAD (dwg)
        FILE_TYPE_MAP.put("7b5c727466315c616e73", "rtf"); // Rich Text Format (rtf)
        FILE_TYPE_MAP.put("38425053000100000000", "psd"); // Photoshop (psd)
        FILE_TYPE_MAP.put("46726f6d3a203d3f6762", "eml"); // Email [Outlook Express 6] (eml)
        FILE_TYPE_MAP.put("5374616E64617264204A", "mdb"); // MS Access (mdb)
        FILE_TYPE_MAP.put("252150532D41646F6265", "ps");
        FILE_TYPE_MAP.put("255044462d312e", "pdf"); // Adobe Acrobat (pdf)
        FILE_TYPE_MAP.put("2e524d46000000120001", "rmvb"); // rmvb/rm相同
        FILE_TYPE_MAP.put("464c5601050000000900", "flv"); // flv与f4v相同
        FILE_TYPE_MAP.put("0000001C66747970", "mp4");
        FILE_TYPE_MAP.put("00000020667479706", "mp4");
        FILE_TYPE_MAP.put("00000018667479706D70", "mp4");
        FILE_TYPE_MAP.put("49443303000000002176", "mp3");
        FILE_TYPE_MAP.put("000001ba210001000180", "mpg"); //
        FILE_TYPE_MAP.put("3026b2758e66cf11a6d9", "wmv"); // wmv与asf相同
        FILE_TYPE_MAP.put("52494646e27807005741", "wav"); // Wave (wav)
        FILE_TYPE_MAP.put("52494646d07d60074156", "avi");
        FILE_TYPE_MAP.put("4d546864000000060001", "mid"); // MIDI (mid)
        FILE_TYPE_MAP.put("526172211a0700cf9073", "rar"); // WinRAR
        FILE_TYPE_MAP.put("235468697320636f6e66", "ini");
        FILE_TYPE_MAP.put("504B03040a0000000000", "jar");
        FILE_TYPE_MAP.put("504B0304140008000800", "jar");
        // MS Excel 注意：word、msi 和 excel的文件头一样
        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10", "xls");
        FILE_TYPE_MAP.put("504B0304", "zip");
        FILE_TYPE_MAP.put("4d5a9000030000000400", "exe"); // 可执行文件
        FILE_TYPE_MAP.put("3c25402070616765206c", "jsp"); // jsp文件
        FILE_TYPE_MAP.put("4d616e69666573742d56", "mf"); // MF文件
        FILE_TYPE_MAP.put("7061636b616765207765", "java"); // java文件
        FILE_TYPE_MAP.put("406563686f206f66660d", "bat"); // bat文件
        FILE_TYPE_MAP.put("1f8b0800000000000000", "gz"); // gz文件
        FILE_TYPE_MAP.put("cafebabe0000002e0041", "class"); // class文件
        FILE_TYPE_MAP.put("49545346030000006000", "chm"); // chm文件
        FILE_TYPE_MAP.put("04000000010000001300", "mxp"); // mxp文件
        FILE_TYPE_MAP.put("6431303a637265617465", "torrent");
        FILE_TYPE_MAP.put("6D6F6F76", "mov"); // Quicktime (mov)
        FILE_TYPE_MAP.put("FF575043", "wpd"); // WordPerfect (wpd)
        FILE_TYPE_MAP.put("CFAD12FEC5FD746F", "dbx"); // Outlook Express (dbx)
        FILE_TYPE_MAP.put("2142444E", "pst"); // Outlook (pst)
        FILE_TYPE_MAP.put("AC9EBD8F", "qdf"); // Quicken (qdf)
        FILE_TYPE_MAP.put("E3828596", "pwl"); // Windows Password (pwl)
        FILE_TYPE_MAP.put("2E7261FD", "ram"); // Real Audio (ram)
    }


    /**
     * 根据文件流的头部信息获得文件类型
     *
     * @param fileStreamHexHead 文件流头部16进制字符串
     * @return 文件类型，未找到为{@code null}
     */
    private static String getType(String fileStreamHexHead) {
        for (Map.Entry<String, String> fileTypeEntry : FILE_TYPE_MAP.entrySet()) {
            if (fileStreamHexHead.toLowerCase().startsWith(fileTypeEntry.getKey().toLowerCase())) {
                return fileTypeEntry.getValue();
            }
        }
        return null;
    }

    /**
     * 根据文件流的头部信息获得文件类型
     *
     * <pre>
     *     1、无法识别类型默认按照扩展名识别
     *     2、xls、doc、msi头信息无法区分，按照扩展名区分
     *     3、zip可能为docx、xlsx、pptx、jar、war头信息无法区分，按照扩展名区分
     * </pre>
     *
     * @param file 文件 {@link File}
     * @return 类型，文件的扩展名，未找到为{@code null}
     */
    public static String getTypeByStream(File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            return getTypeByStream(in);
        }catch (Exception e){
            //ignore
        }finally {
            IOUtil.close(in);
        }
        return UNKNOWN;
    }


    /**
     * 通过路径获得文件类型
     *
     * @param path 路径，绝对路径或相对ClassPath的路径
     * @return 类型
     */
    public static String getTypeByStream(String path) {
        return getTypeByStream(new File(path));
    }

    /**
     * 根据文件流的头部信息获得文件类型
     *
     * @param in {@link InputStream}
     * @return 类型，文件的扩展名，未找到为{@code null}
     */
    public static String getTypeByStream(InputStream in) {
        try {
            return getType(IOUtil.readHex28Upper(in));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return UNKNOWN;
    }


    /**
     * 常用静态图片文件类型
     */
    private static final List<String> imageTypes = Lists.newArrayList("png", "jpg", "jpeg");

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
        int index = fileName.lastIndexOf(StringUtils.DOT);
        if (index == -1) {
            return null;
        } else {
            return fileName.substring(index + 1);
        }
    }

    /**
     * 判断是否是图片格式
     *
     * @param extName 文件扩展名
     * @return boolean
     */
    public static boolean isImage(String extName){
        if (StringUtils.isBlank(extName)) {
            return false;
        }
        return imageTypes.contains(extName.toLowerCase());
    }

}
