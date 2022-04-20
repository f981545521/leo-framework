package cn.acyou.leo.media.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author youfang
 * @version [1.0.0, 2022/4/19 17:54]
 **/
public class ImageUtil {

    /**
     * 压缩图像
     *
     * @param url        url
     * @param targetPath 目标路径
     * @throws Exception 异常
     */
    public static void compressImage(URL url, File targetPath) throws Exception {
        targetPath.getParentFile().mkdirs();
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(5000);//超时时间
        InputStream is = conn.getInputStream();
        FileOutputStream os = new FileOutputStream(targetPath);
        compressImage(is, os);
    }

    /**
     * 压缩图像
     *
     * @param sourceFile 源文件
     * @param targetPath 目标路径
     * @throws Exception 异常
     */
    public static void compressImage(File sourceFile, File targetPath) throws Exception {
        targetPath.getParentFile().mkdirs();
        InputStream is = new FileInputStream(sourceFile);
        FileOutputStream os = new FileOutputStream(targetPath);
        compressImage(is, os);
    }

    /**
     * 压缩图像
     *
     * <pre>
     * BufferedImage read = ImageIO.read(new URL("http://qiniu.acyou.cn/images/13.jpg"));
     * BufferedImage bi = new BufferedImage(read.getWidth(), read.getHeight(), BufferedImage.TYPE_INT_RGB);
     * Graphics g = bi.getGraphics();
     * g.drawImage(read, 0, 0, read.getWidth(), read.getHeight(), Color.LIGHT_GRAY, null);
     * g.dispose();
     * FileOutputStream os = new FileOutputStream("D:\\temp\\image\\123456.jpg");
     * ImageIO.write(bi, "jpg", os);
     * os.close();
     * </pre>
     *
     * @param is 输入流
     * @param os 输出流
     * @throws Exception 异常
     */
    public static void compressImage(InputStream is, OutputStream os) throws Exception {
        Image img = ImageIO.read(is);
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        g.drawImage(img, 0, 0, width, height, Color.LIGHT_GRAY, null);
        g.dispose();
        ImageIO.write(bi, "jpg", os);
        is.close();
        os.close();
    }

}
