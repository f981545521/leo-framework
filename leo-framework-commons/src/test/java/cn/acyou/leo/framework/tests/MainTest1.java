package cn.acyou.leo.framework.tests;

import cn.acyou.leo.framework.downloader.DownloadUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-16 16:14]
 */
public class MainTest1 {
    public static void main(String[] args) throws Exception {
        String fileURL = "https://robot.guiji.ai/nfs/vte/video/2dr/624654168652787848/624654168652787848.mp4";
        String bigFileURL = "https://download.jetbrains.8686c.com/idea/ideaIU-2020.3.dmg";

        //====== 方式一
        //Downloader downloader = new SimpleDownloader();
        //downloader.download(fileURL, "D:\\tmp2\\");

        //====== 方式二
        //FileDownloader fileDownloader = new FileDownloader();
        //fileDownloader.download(fileURL, "D:\\tmp2\\");

        //====== 方式三：大文件下载
        //MultiThreadDownloadProgressPrinter downloadProgressPrinter = new MultiThreadDownloadProgressPrinter(5);
        //CompletableFuture.runAsync(() -> {
        //    while (true) {
        //        long alreadyDownloadLength = downloadProgressPrinter.getAlreadyDownloadLength();
        //        long contentLength = downloadProgressPrinter.getContentLength();
        //        System.out.println(contentLength + "  =>  " + alreadyDownloadLength + "|" + MathUtil.calculationPercent(alreadyDownloadLength, contentLength) + "%");
        //        if (alreadyDownloadLength != 0 && alreadyDownloadLength > contentLength) {
        //            break;
        //        }
        //        try {
        //            Thread.sleep(1000L);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
        //    }
        //});
        //long l = System.currentTimeMillis();
        //MultiThreadFileDownloader fileDownloader = new MultiThreadFileDownloader(5, downloadProgressPrinter);
        //fileDownloader.download(bigFileURL, "D:\\tmp2\\");
        //System.out.println("结束：" + (System.currentTimeMillis() - l));//结束：1526
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "Aweme 6.5.0 rv:65014 (iPhone; iOS 12.3.1; en_CN) Cronet");
        long l = System.currentTimeMillis();
        //DownloadUtil.download(fileURL, "D:\\tmp2\\1234_3.mp4");
        DownloadUtil.download(fileURL, "D:\\tmp2\\1234_5.mp4", headerMap);
        System.out.println("结束：" + (System.currentTimeMillis() - l));//结束：1526

    }
}
