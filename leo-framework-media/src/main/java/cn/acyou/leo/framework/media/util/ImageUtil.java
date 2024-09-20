package cn.acyou.leo.framework.media.util;

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
        ImageIO.write(bi, "jpg", os);//默认使用JPG格式 因为PNG格式比较大
        is.close();
        os.close();
    }

    /**
     * 得到图片信息
     *
     * @param source 源
     * @return {@link ImageInfo}
     * @throws Exception 异常
     */
    public static ImageInfo getImageInfo(String source) throws Exception {
        ImageInfo imageInfo = new ImageInfo();
        // 图片对象
        BufferedImage image;
        if (source.startsWith("http")) {
            URL url = new URL(source);
            URLConnection c = url.openConnection();
            imageInfo.setSize(c.getContentLength());
            image = ImageIO.read(c.getInputStream());
        } else {
            File file = new File(source);
            imageInfo.setSize(file.length());
            image = ImageIO.read(new FileInputStream(file));
        }
        imageInfo.setHeight(image.getHeight());
        imageInfo.setWidth(image.getWidth());
        String type = "";
        if (source.lastIndexOf(".") > 0) {
            type = source.substring(source.lastIndexOf(".") + 1);
        }
        imageInfo.setType(type);
        return imageInfo;
    }

    /**
     * 缩放图片大小
     *
     * @param source       源文件
     * @param target       目标文件
     * @param targetWidth  目标宽
     * @param targetHeight 目标高
     * @throws Exception 异常
     */
    public static void resize(File source, File target, int targetWidth, int targetHeight) throws Exception {
        BufferedImage originalImage = ImageIO.read(source);
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        BufferedImage targetImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
        Graphics2D graphics2D = targetImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        ImageIO.write(targetImage, "png", target);
    }

    public static void main(String[] args) throws Exception {
        File source = new File("C:\\Users\\1\\Pictures\\XM1.webp");
        File target = new File("C:\\Users\\1\\Pictures\\XM1_target2.png");
        resize(source, target, 240, 240);
    }
}
