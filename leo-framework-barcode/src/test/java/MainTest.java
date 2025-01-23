import cn.acyou.leo.framework.barcode.BarCodeUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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

        BufferedImage barCode = GoogleBarCodeUtils.getBarCode("1234567890");
        BufferedImage xxx = GoogleBarCodeUtils.insertWords(barCode, "1234567890");
        ImageIO.write(xxx, "png", Files.newOutputStream(Paths.get("D:\\export\\test3.png").toFile().toPath()));
    }
}
