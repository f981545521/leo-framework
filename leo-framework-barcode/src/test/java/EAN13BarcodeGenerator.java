import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;

public class EAN13BarcodeGenerator {

    public static void main(String[] args) throws Exception {
        String message = "6921734976505";
        OutputStream outputStream = Files.newOutputStream(new File("D:\\export\\ean13_22.jpg").toPath());
        EAN13Bean barcode = new EAN13Bean();
        barcode.setBarHeight(20); // 设置条形码高度
        barcode.setHeight(20);
        barcode.setModuleWidth(0.33); // 设置窄条宽度
        barcode.setVerticalQuietZone(0.2);
        try {
            BitmapCanvasProvider canvas = new BitmapCanvasProvider
                    (outputStream, "image/png", 350,
                            BufferedImage.TYPE_BYTE_BINARY, true, 0);
            barcode.generateBarcode(canvas, message);
            canvas.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}