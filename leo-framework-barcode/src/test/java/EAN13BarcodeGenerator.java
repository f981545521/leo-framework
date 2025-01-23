import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;

/**
 * 生成EAN13码
 * 并且设置白色边框
 */
public class EAN13BarcodeGenerator {

    public static void main(String[] args) throws Exception {
        String message = "6921734976505";
        OutputStream outputStream = Files.newOutputStream(new File("D:\\export\\ean13_26.jpg").toPath());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        EAN13Bean barcode = new EAN13Bean();
        barcode.setBarHeight(20); // 设置条形码高度
        barcode.setHeight(20);
        barcode.setModuleWidth(0.3); // 设置窄条宽度
        barcode.setVerticalQuietZone(0.2);
        try {
            BitmapCanvasProvider canvas = new BitmapCanvasProvider
                    (os, "image/png", 350,
                            BufferedImage.TYPE_BYTE_BINARY, true, 0);
            barcode.generateBarcode(canvas, message);
            canvas.finish();

            BufferedImage read = ImageIO.read(new ByteArrayInputStream(os.toByteArray()));
            BufferedImage imageWithText = addTextToBarcode(read, "");
            ImageIO.write(imageWithText, "png", outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 添加文字到条形码图像
     *
     * @param barcodeImage 条形码图像
     * @param text         要添加的文字
     * @return 带有文字的条形码图像
     */
    public static BufferedImage addTextToBarcode(BufferedImage barcodeImage, String text) {
        // 计算新图像的高度，增加文字显示区域
        int newHeight = barcodeImage.getHeight() + 40;
        // 创建新的 BufferedImage 对象
        BufferedImage imageWithText = new BufferedImage(barcodeImage.getWidth(), newHeight, BufferedImage.TYPE_INT_RGB);
        // 获取 Graphics2D 对象
        Graphics2D g2d = imageWithText.createGraphics();
        // 设置背景颜色为白色
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, imageWithText.getWidth(), imageWithText.getHeight());
        // 绘制条形码图像
        g2d.drawImage(barcodeImage, 0, 20, null);
        // 设置文字颜色为黑色
        g2d.setColor(Color.BLACK);
        // 设置字体
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        // 计算文字居中的 x 坐标
        int textX = (imageWithText.getWidth() - g2d.getFontMetrics().stringWidth(text)) / 2;
        // 计算文字的 y 坐标
        int textY = barcodeImage.getHeight() + 35;
        // 绘制文字
        g2d.drawString(text, textX, textY);
        // 释放 Graphics2D 对象
        g2d.dispose();
        return imageWithText;
    }

}