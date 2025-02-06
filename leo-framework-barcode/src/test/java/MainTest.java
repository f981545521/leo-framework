import cn.acyou.leo.framework.barcode.BarCodeUtil;
import com.google.common.io.Resources;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author youfang
 * @version [1.0.0, 2025/1/21 17:21]
 **/
public class MainTest {
    public static void main(String[] args) throws Exception {
        //BarCodeUtil.createCode128Code(Files.newOutputStream(Paths.get("D:\\export\\112.png")), "11111");
        //BarCodeUtil.createPdf417Code(Files.newOutputStream(Paths.get("D:\\export\\1.png")), "11111");
        //BarCodeUtil.createQrCode(Files.newOutputStream(Paths.get("D:\\export\\2.png")), "11111", 500);

        //// 获取系统支持的字体名称列表
        //String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        //// 打印字体名称
        //for (String fontName : fontNames) {
        //    System.out.println(fontName);
        //    GoogleBarCodeUtils.font = new Font(fontName, Font.PLAIN, 12);
        //    BufferedImage barCode = GoogleBarCodeUtils.getBarCode("1234567890");
        //    BufferedImage xxx = GoogleBarCodeUtils.insertWords(barCode, "1234567890");
        //    ImageIO.write(xxx, "png", Files.newOutputStream(Paths.get("D:\\export\\test_"+fontName+".png").toFile().toPath()));
        //}

        //注册字体
        //GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //Font font = Font.createFont(Font.TRUETYPE_FONT, new File("D:\\developer\\font\\Hanzi-Pinyin-Font.top.ttf"));
        //System.out.println("注册字体：" + font.getFontName());
        //ge.registerFont(font);
        //font = Font.createFont(Font.TRUETYPE_FONT, new File("D:\\developer\\font\\阿朱泡泡体.ttf"));
        //System.out.println("注册字体：" + font.getFontName());
        //ge.registerFont(font);
        //GoogleBarCodeUtils.font = new Font("AZPPT_1_1436212_19", Font.PLAIN, 12);
        //BufferedImage barCode = GoogleBarCodeUtils.getBarCode("1234567890");
        //BufferedImage xxx = GoogleBarCodeUtils.insertWords(barCode, "1234567890");
        //ImageIO.write(xxx, "png", Files.newOutputStream(Paths.get("D:\\export\\test_aaaa.png").toFile().toPath()));

        //直接使用 本地
        //Font font = Font.createFont(Font.TRUETYPE_FONT, new File("D:\\developer\\font\\阿朱泡泡体.ttf"));
        //直接使用 resources
        Font font = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("font/阿朱泡泡体.ttf").openStream());
        GoogleBarCodeUtils.font = font.deriveFont(Font.PLAIN, 12);
        BufferedImage barCode = GoogleBarCodeUtils.getBarCode("1234567890");
        BufferedImage xxx = GoogleBarCodeUtils.insertWords(barCode, "1234567890");
        ImageIO.write(xxx, "png", Files.newOutputStream(Paths.get("D:\\export\\test_aaaaaa.png").toFile().toPath()));

    }
}
