import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BarcodeWithText {

    public static void main(String[] args) {
        String barcodeText = "1234567890"; // 条形码内容
        int width = 600;
        int height = 200;
        String filePath = "D:\\export\\barcode.png"; // 保存路径

        try {
            // 生成条形码图像
            BufferedImage barcodeImage = generateBarcode(barcodeText, width, height);
            // 添加文字到条形码图像
            BufferedImage imageWithText = addTextToBarcode(barcodeImage, barcodeText);
            // 保存图像
            saveImage(imageWithText, filePath);
            System.out.println("条形码已生成: " + filePath);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成条形码图像
     *
     * @param text   条形码内容
     * @param width  图像宽度
     * @param height 图像高度
     * @return 条形码图像
     * @throws WriterException 写入异常
     */
    public static BufferedImage generateBarcode(String text, int width, int height) throws WriterException {
        // 创建 Code128 条形码写入器
        Code128Writer writer = new Code128Writer();
        // 设置编码提示
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 生成 BitMatrix
        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.CODE_128, width, height, hints);
        // 将 BitMatrix 转换为 BufferedImage
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
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

    /**
     * 保存图像到指定路径
     *
     * @param image    要保存的图像
     * @param filePath 保存路径
     * @throws IOException 写入异常
     */
    public static void saveImage(BufferedImage image, String filePath) throws IOException {
        // 创建 File 对象
        File outputFile = new File(filePath);
        // 将图像写入文件
        ImageIO.write(image, "png", outputFile);
    }
}