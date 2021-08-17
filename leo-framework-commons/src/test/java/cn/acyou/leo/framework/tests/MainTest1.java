package cn.acyou.leo.framework.tests;

import cn.acyou.leo.framework.downloader.DownloadUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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

        //============ 方式一
        //Downloader downloader = new SimpleDownloader();
        //downloader.download(fileURL, "D:\\tmp2\\");

        //============ 方式二
        //FileDownloader fileDownloader = new FileDownloader();
        //fileDownloader.download(fileURL, "D:\\tmp2\\");

        //============ 方式三：大文件下载
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

        //============ 方式四：自定义header
        //Map<String, String> headerMap = new HashMap<>();
        //headerMap.put("Content-Type", "Aweme 6.5.0 rv:65014 (iPhone; iOS 12.3.1; en_CN) Cronet");
        //long l = System.currentTimeMillis();
        ////DownloadUtil.download(fileURL, "D:\\tmp2\\1234_3.mp4");
        //DownloadUtil.download(fileURL, "D:\\tmp2\\1234_5.mp4", headerMap);
        //System.out.println("结束：" + (System.currentTimeMillis() - l));//结束：1521

        //============ 方式五：JDK自带的URLConnection
        //URL url = new URL(fileURL);
        //long l = System.currentTimeMillis();
        //URLConnection urlConnection = url.openConnection();
        //InputStream inputStream = urlConnection.getInputStream();
        //FileOutputStream fos = new FileOutputStream("D:\\tmp2\\ABC_6.mp4");
        //IOUtils.copyLarge(inputStream, fos);
        //System.out.println("结束：" + (System.currentTimeMillis() - l));//结束：826
        //fos.flush();
        //fos.close();
        //inputStream.close();

        //============ 方式六：HTTPClient
        long l = System.currentTimeMillis();
        downloadFile(fileURL, "D:\\tmp2\\1234_777.mp4");
        System.out.println("结束：" + (System.currentTimeMillis() - l));//结束：2019
    }


    /**
     * 下载文件
     *
     * @param url      网络文件路径
     * @param filepath 保存路径
     */
    public static void downloadFile(String url, String filepath) {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = client.execute(httpget);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            File file = new File(filepath);
            boolean mkdirs = file.getParentFile().mkdirs();
            FileOutputStream fileout = new FileOutputStream(file);
            IOUtils.copyLarge(is, fileout);
            fileout.flush();
            is.close();
            fileout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
