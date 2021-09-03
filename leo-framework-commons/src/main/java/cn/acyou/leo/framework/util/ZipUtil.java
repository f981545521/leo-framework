package cn.acyou.leo.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * @author youfang
 * @version [1.0.0, 2019/12/23]
 **/
public class ZipUtil {
    private final static Logger log = LoggerFactory.getLogger(ZipUtil.class);

    /**
     * 压缩文件
     * <pre>
     *         FileInputStream fi1 = new FileInputStream("D:\\temp6\\111.jpg");
     *         FileInputStream fi2 = new FileInputStream("D:\\temp6\\222.jpg");
     *         FileOutputStream fos = new FileOutputStream("D:\\temp6\\压缩文件.zip");
     *         String[] fileNames = new String[]{"好好学习.jpg", "天天向上.jpg"};
     *         zipBatch(fileNames, new InputStream[]{fi1, fi2}, fos);
     * </pre>
     *
     * @param fileNames  文件名
     * @param srcFiles   源文件流
     * @param destStream 目标文件流
     * @throws Exception 异常
     */
    public static void zipBatch(String[] fileNames, InputStream[] srcFiles, OutputStream destStream) throws Exception {
        log.info("压缩开始压缩...");
        ZipOutputStream zos = new ZipOutputStream(destStream, Charset.forName("GBK"));
        for (int k = 0; k < srcFiles.length; ++k) {
            ZipEntry ze = new ZipEntry(fileNames[k]);
            ze.setSize(srcFiles.length);
            ze.setTime((new Date()).getTime());
            log.info("正在压缩第" + k + "个输入流!");
            zos.putNextEntry(ze);
            BufferedInputStream is = new BufferedInputStream(srcFiles[k]);
            IOUtil.copyLarge(is, zos);
            is.close();
        }
        zos.close();
        log.info("压缩完毕！");
    }


}