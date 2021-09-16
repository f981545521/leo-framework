package cn.acyou.leo.framework.barcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2020-8-13 下午 11:06]
 **/
public class BarCodeUtil {
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    /**
     * 默认图片格式
     */
    private static final String DEFAULT_IMAGE_FORMAT = "PNG";

    // LOGO图片设置
    private static final int IMAGE_WIDTH = 80;
    private static final int IMAGE_HEIGHT = 80;
    private static final int IMAGE_HALF_WIDTH = IMAGE_WIDTH / 2;
    private static final int FRAME_WIDTH = 2;

    /**
     * 生成包含字符串信息的二维码图片
     *
     * @param outputStream 文件输出流路径
     * @param content      二维码携带的内容信息
     * @param qrCodeSize   二维码图片大小
     * @throws WriterException 异常
     * @throws IOException 异常
     */
    public static void createQrCode(OutputStream outputStream, String content, int qrCodeSize) throws WriterException, IOException {
        BufferedImage image = createImage(content, qrCodeSize);
        ImageIO.write(image, DEFAULT_IMAGE_FORMAT, outputStream);
    }
    /**
     * 生成包含字符串信息的二维码图片
     *
     * @param outputStream 文件输出流路径
     * @param content      二维码携带的内容信息
     * @param qrCodeSize   二维码图片大小
     * @throws WriterException 异常
     * @throws IOException 异常
     */
    public static void createLogoQrCode(OutputStream outputStream, String logoImagePath, String content, int qrCodeSize) throws WriterException, IOException {
        BufferedImage image = createLogoImage(content, qrCodeSize, logoImagePath);
        ImageIO.write(image, DEFAULT_IMAGE_FORMAT, outputStream);
    }

    /**
     * 创建带有Logo的二维码
     *
     * @param content      内容
     * @param qrCodeSize   二维码的大小
     * @param srcImagePath Logo图像路径
     * @return {@link BufferedImage}* @throws WriterException 异常
     * @throws IOException ioException
     */
    private static BufferedImage createLogoImage(String content, int qrCodeSize, String srcImagePath) throws WriterException,
            IOException {
        // 读取源图像
        BufferedImage scaleImage = scale(srcImagePath, IMAGE_WIDTH, IMAGE_HEIGHT, true);
        int[][] srcPixels = new int[IMAGE_WIDTH][IMAGE_HEIGHT];
        for (int i = 0; i < scaleImage.getWidth(); i++) {
            for (int j = 0; j < scaleImage.getHeight(); j++) {
                srcPixels[i][j] = scaleImage.getRGB(i, j);
            }
        }

        Map<EncodeHintType, Object> hint = new HashMap<>();
        hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 生成二维码
        MultiFormatWriter mutiWriter = new MultiFormatWriter();
        BitMatrix matrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE,
                qrCodeSize, qrCodeSize, hint);

        // 二维矩阵转为一维像素数组
        int halfW = matrix.getWidth() / 2;
        int halfH = matrix.getHeight() / 2;
        int[] pixels = new int[qrCodeSize * qrCodeSize];

        for (int y = 0; y < matrix.getHeight(); y++) {
            for (int x = 0; x < matrix.getWidth(); x++) {
                boolean adjust = (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
                        && x < halfW - IMAGE_HALF_WIDTH + FRAME_WIDTH
                        && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
                        + IMAGE_HALF_WIDTH + FRAME_WIDTH)
                        || (x > halfW + IMAGE_HALF_WIDTH - FRAME_WIDTH
                        && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
                        && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
                        + IMAGE_HALF_WIDTH + FRAME_WIDTH)
                        || (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
                        && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
                        && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
                        - IMAGE_HALF_WIDTH + FRAME_WIDTH)
                        || (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
                        && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
                        && y > halfH + IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
                        + IMAGE_HALF_WIDTH + FRAME_WIDTH);
                // 读取图片
                if (x > halfW - IMAGE_HALF_WIDTH
                        && x < halfW + IMAGE_HALF_WIDTH
                        && y > halfH - IMAGE_HALF_WIDTH
                        && y < halfH + IMAGE_HALF_WIDTH) {
                    pixels[y * qrCodeSize + x] = srcPixels[x - halfW
                            + IMAGE_HALF_WIDTH][y - halfH + IMAGE_HALF_WIDTH];
                }
                // 在图片四周形成边框
                else if (adjust) {
                    pixels[y * qrCodeSize + x] = 0xfffffff;
                } else {
                    // 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
                    pixels[y * qrCodeSize + x] = matrix.get(x, y) ? 0xff000000
                            : 0xfffffff;
                }
            }
        }
        BufferedImage image = new BufferedImage(qrCodeSize, qrCodeSize, BufferedImage.TYPE_INT_RGB);
        image.getRaster().setDataElements(0, 0, qrCodeSize, qrCodeSize, pixels);
        return image;
    }

    /**
     * 把传入的原始图像按高度和宽度进行缩放，生成符合要求的图标
     *
     * @param srcImageFile 源文件地址
     * @param height 目标高度
     * @param width 目标宽度
     * @param hasFiller 比例不对时是否需要补白：true为补白; false为不补白;
     * @throws IOException ex
     */
    private static BufferedImage scale(String srcImageFile, int height, int width, boolean hasFiller) throws IOException {
        // 缩放比例
        double ratio;
        File file = new File(srcImageFile);
        BufferedImage srcImage = ImageIO.read(file);
        Image destImage = srcImage.getScaledInstance(width, height,
                BufferedImage.SCALE_SMOOTH);
        // 计算比例
        if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {
            if (srcImage.getHeight() > srcImage.getWidth()) {
                ratio = (new Integer(height)).doubleValue()
                        / srcImage.getHeight();
            } else {
                ratio = (new Integer(width)).doubleValue()
                        / srcImage.getWidth();
            }
            AffineTransformOp op = new AffineTransformOp(
                    AffineTransform.getScaleInstance(ratio, ratio), null);
            destImage = op.filter(srcImage, null);
        }
        // 补白
        if (hasFiller) {
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D graphic = image.createGraphics();
            graphic.setColor(Color.white);
            graphic.fillRect(0, 0, width, height);
            if (width == destImage.getWidth(null)){
                graphic.drawImage(destImage, 0,
                        (height - destImage.getHeight(null)) / 2,
                        destImage.getWidth(null), destImage.getHeight(null),
                        Color.white, null);
            } else {
                graphic.drawImage(destImage,
                        (width - destImage.getWidth(null)) / 2, 0,
                        destImage.getWidth(null), destImage.getHeight(null),
                        Color.white, null);
            }
            graphic.dispose();
            destImage = image;
        }
        return (BufferedImage) destImage;
    }


    /**
     * 生成包含字符串信息的二维码图片
     *
     * @param outputStream 文件输出流路径
     * @param content      二维码携带信息
     * @throws WriterException 异常
     * @throws IOException 异常
     */
    public static boolean createPdf417Code(OutputStream outputStream, String content) throws WriterException, IOException {
        PDF417Writer pdf417Writer = new PDF417Writer();
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.MARGIN, "50");
        BitMatrix bitMatrix = pdf417Writer.encode(content, BarcodeFormat.PDF_417, 1000, 300, hints);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        pressText(content, image);
        return ImageIO.write(image, DEFAULT_IMAGE_FORMAT, outputStream);
    }

    /**
     * 读二维码携带的信息
     *
     * @param inputStream 输入流
     * @return {@link String}
     * @throws Exception 异常
     */
    public static String readQrCode(InputStream inputStream) throws Exception {
        BufferedImage image = ImageIO.read(inputStream);
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        Result result = reader.decode(bitmap);
        return result.getText();
    }

    /**
     * 创建图像
     *
     * @param content    内容
     * @param qrCodeSize 二维码的大小
     * @return {@link BufferedImage}* @throws WriterException 作家例外
     */
    private static BufferedImage createImage(String content, int qrCodeSize) throws WriterException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * 给二维码图片加上文字
     */
    public static void pressText(String pressText, BufferedImage image) {
        pressText = new String(pressText.getBytes(), StandardCharsets.UTF_8);
        Graphics g = image.createGraphics();
        g.setColor(Color.BLACK);
        Font font = new Font(Font.SERIF, Font.PLAIN, 50);
        FontMetrics metrics = g.getFontMetrics(font);
        // 文字在图片中的坐标 这里设置在中间
        int startX = (image.getWidth() - metrics.stringWidth(pressText)) / 2;
        //文字所在的Y轴开始坐标（20：需要动态调整设置的值）
        int startY = image.getHeight() - 10;
        g.setFont(font);
        g.drawString(pressText, startX, startY);
        g.dispose();
    }

}
