package cn.acyou.leo.framework.util;

import java.io.*;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-16 14:56]
 */
public class IOUtil {
    /**
     * 表示文件 结尾（或流）。
     */
    public static final int EOF = -1;
    /**
     * 默认BUFFER缓冲大小
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    /**
     * 从大文件复制字节 (超过 2GB)
     *
     * @param input  输入流
     * @param output 输出
     * @return 复制的字节数
     * @throws NullPointerException  input or output is null
     * @throws IOException          if an I/O error occurs
     */
    public static long copyLarge(final InputStream input, final OutputStream output) throws IOException {
        return copy(input, output, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 复制字节 从<code>InputStream</code> 到 <code>OutputStream</code>
     * 使用给定大小的内部缓冲区。
     *
     * @param input  输入流
     * @param output 输出
     * @return 复制的字节数
     * @throws NullPointerException  input or output is null
     * @throws IOException          if an I/O error occurs
     */
    public static long copy(final InputStream input, final OutputStream output, final int bufferSize)
            throws IOException {
        return copyLarge(input, output, new byte[bufferSize]);
    }

    /**
     * 大文件复制字节 从<code>InputStream</code> 到 <code>OutputStream</code>.
     *
     * @param input  输入流
     * @param output 输出
     * @param buffer 用于复制的缓冲区
     * @return 复制的字节数
     * @throws NullPointerException  input or output is null
     * @throws IOException          if an I/O error occurs
     */
    public static long copyLarge(final InputStream input, final OutputStream output, final byte[] buffer)
            throws IOException {
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }


    /**
     * 从流中读取前28个byte并转换为16进制，字母部分使用大写
     *
     * @param in {@link InputStream}
     * @return 16进制字符串
     * @throws IOException IO异常
     */
    public static String readHex28Upper(InputStream in) throws IOException {
        return readHex(in, 28, false);
    }


    /**
     * 读取16进制字符串
     *
     * @param in          {@link InputStream}
     * @param length      长度
     * @param toLowerCase true 传换成小写格式 ， false 传换成大写格式
     * @return 16进制字符串
     * @throws IOException IO异常
     */
    public static String readHex(InputStream in, int length, boolean toLowerCase) throws IOException {
        return HexUtil.encodeHexStr(readBytes(in, length), toLowerCase);
    }


    /**
     * 读取指定长度的byte数组，不关闭流
     *
     * @param in     {@link InputStream}，为null返回null
     * @param length 长度，小于等于0返回空byte数组
     * @return bytes
     */
    public static byte[] readBytes(InputStream in, int length) throws IOException {
        if (null == in) {
            return null;
        }
        if (length <= 0) {
            return new byte[0];
        }
        byte[] b = new byte[length];
        int readLength = in.read(b);
        if (readLength > 0 && readLength < length) {
            byte[] b2 = new byte[readLength];
            System.arraycopy(b, 0, b2, 0, readLength);
            return b2;
        } else {
            return b;
        }
    }


    /**
     * 关闭<br>
     * 关闭失败不会抛出异常
     *
     * @param closeable 被关闭的对象
     */
    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                // 静默关闭
            }
        }
    }
}
