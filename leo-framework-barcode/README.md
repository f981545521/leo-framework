## 工程简介
二维码/条形码 生成工具
## 使用说明
### 二维码
#### 案例一
```
    public static void main(String[] args) {
        // 生成 二维码
        QrCode.form("好好啊学习，天天向上！")
                .size(512) // 默认 512，可以不设置
                .backGroundColor(Color.WHITE) // 默认白色，可以不设置
                .foreGroundColor(Color.BLACK) // 默认黑色，可以不设置
                .encode(Charsets.UTF_8) // 默认 UTF_8，可以不设置
                .imageFormat("png") // 默认 png，可以不设置
                .deleteMargin(true) // 删除白边，默认为 true，可以不设置
                .logo("D:\\tmp6\\logo.jpg") // 设置二维码 logo
                .toFile("D:\\tmp6\\xxx1.png"); // 写出，同类方法有 toImage、toStream、toBytes

        // 二维码读取
        String text = QrCode.read("D:\\tmp6\\xxx1.png");
        System.out.println(text);
    }
```
#### 案例二
```
        //创建二维码
        BarCodeUtil.createLogoQrCode(new FileOutputStream(new File(
                "D:\\tmp6\\qrcode1.jpg")),
                "D:\\tmp6\\logo.jpg",
                "4191699341779402753asdsadsadafss",
                300);
        //读取二维码
        String s = BarCodeUtil.readQrCode(new FileInputStream(new File("D:\\tmp6\\qrcode1.jpg")));
        System.out.println(s);
    }
```
### PDF417
```
    public static void main(String[] args) throws Exception {
        BarCodeUtil.createPdf417Code(new FileOutputStream(new File("D:\\tmp6\\pdf417.jpg")), "4191699341779402753");
    }
```
### 其他条形码
```

    public static void main(String[] args) throws Exception {
        Code128Writer code128Writer = new Code128Writer();
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, "20");
        BitMatrix bitMatrix = code128Writer.encode("12331244345", BarcodeFormat.CODE_128, 400, 150, hints);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        pressText("12331244345", image);
        ImageIO.write(image, DEFAULT_IMAGE_FORMAT, new FileOutputStream(new File("D:\\temp\\code128.jpg")));
    }
```