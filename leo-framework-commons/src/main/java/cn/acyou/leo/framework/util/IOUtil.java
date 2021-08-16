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
}
