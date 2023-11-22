package cn.acyou.leo.framework.media.encoder;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author youfang
 * @version [1.0.0, 2023/11/2 17:23]
 **/
@Slf4j
class Util {

    /**
     * 根据像素 计算屏幕比例（分辨率）
     * <pre>
     * System.out.println(proportion(1920, 1080));//16:9
     * System.out.println(proportion(3000, 2000));//3:2
     * System.out.println(commonDivisor(3840, 0));//3840
     * System.out.println(commonDivisor(0, 2160));//2160
     * System.out.println(proportion(3840, 0));//1:0
     * System.out.println(proportion(0, 2160));//0:1
     * System.out.println(proportion(3840, -2160));//16:-9
     * System.out.println(proportion(-3840, 2160));//-16:9
     * </pre>
     *
     * @param x x
     * @param y y
     * @return {@link String}
     */
    public static String proportion(int x, int y) {
        if (x == 0 || y == 0) {
            return "9:16";
        }
        int i = commonDivisor(x, y);
        return x / i + ":" + y / i;
    }

    /**
     * 求最大公约数
     *
     * @param M 值1
     * @param N 值2
     * @return 最大公约数
     */
    private static int commonDivisor(int M, int N) {
        M = Math.abs(M);
        N = Math.abs(N);
        if (N == 0) {
            return M;
        }
        return commonDivisor(N, M % N);
    }

    public static long getContentLength(String i) {
        try {
            if (StringUtils.isNotBlank(i)) {
                if (i.toLowerCase().startsWith("http")) {
                    return getContentLength(new URL(i));
                } else {
                    return getContentLength(new File(i));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return -1;
    }


    /**
     * 获取指定URL对应资源的内容长度，对于Http，其长度使用Content-Length头决定。
     *
     * @param url URL
     * @return 内容长度，未知返回-1
     */
    public static long getContentLength(URL url) {
        if (null == url) {
            return -1;
        }
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            return conn.getContentLengthLong();
        } catch (IOException e) {
            return -1;
        } finally {
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
        }
    }

    /**
     * 获取指定file对应资源的内容长度
     *
     * @param file 文件
     * @return 内容长度，未知返回-1
     */
    public static long getContentLength(File file) {
        if (null == file) {
            return -1;
        }
        try {
            return file.length();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 字节转换 为可读值
     * <pre>
     *     convertFileSize(200000L);    = 195 KB
     * </pre>
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.2f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.2f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }
}
