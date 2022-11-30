package cn.acyou.leo.framework.downloader;

import cn.acyou.leo.framework.downloader.ext.ByteArrayResponseExtractor;
import cn.acyou.leo.framework.downloader.support.DownloadProgressPrinter;
import cn.acyou.leo.framework.downloader.utils.RestTemplateBuilder;
import cn.acyou.leo.framework.util.function.Task;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-16 17:30]
 */
@Slf4j
public class DownloadUtil {
    private static final RestTemplate restTemplate;
    private static final DownloadProgressPrinter downloadProgressPrinter;
    private static Proxy proxy;

    static {
        restTemplate = RestTemplateBuilder.builder().build();
        downloadProgressPrinter = DownloadProgressPrinter.defaultDownloadProgressPrinter();
    }

    private DownloadUtil() {

    }

    public static void setProxy(Proxy proxy) {
        DownloadUtil.proxy = proxy;
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setProxy(proxy);
        restTemplate.setRequestFactory(requestFactory);
    }

    /**
     * 下载到目录（使用URL上的文件名）
     *
     * @param fileURL 文件的url
     * @param dir     dir
     * @throws Exception 异常
     */
    public static void downloadDir(String fileURL, String dir) throws Exception {
        String urlResourcesName = fileURL.substring(fileURL.lastIndexOf("/") + 1);
        download(fileURL, dir + File.separator + urlResourcesName);
    }


    /**
     * 下载
     *
     * @param fileURL  文件的url
     * @param dir      文件目录
     * @param fileName 文件名称
     * @throws Exception 异常
     */
    public static void downloadFile(String fileURL, String dir, String fileName) throws Exception {
        download(fileURL, dir + File.separator + fileName);
    }

    /**
     * 下载
     *
     * @param fileURL      文件的url
     * @param fileFullPath 文件的完整路径
     * @throws Exception 异常
     */
    public static void download(String fileURL, String fileFullPath) throws Exception {
        download(fileURL, fileFullPath, null);
    }
    /**
     * 下载
     *
     * @param fileURL      文件的url
     * @param fileFullPath 文件的完整路径
     * @param headerMaps headers
     * @throws Exception 异常
     */
    public static void download(String fileURL, String fileFullPath, Map<String, String> headerMaps) throws Exception {
        File file = new File(fileFullPath);
        boolean mkdirs = file.getParentFile().mkdirs();
        if (mkdirs) {
            log.info("auto create directory ：{}", file.getParentFile().getAbsolutePath());
        }
        HttpHeaders headers = new HttpHeaders();
        if (headerMaps != null && headerMaps.size() > 0) {
            headerMaps.forEach(headers::add);
        }
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ByteArrayResponseExtractor byteArrayResponseExtractor = new ByteArrayResponseExtractor(downloadProgressPrinter);
        RequestCallback requestCallback = restTemplate.httpEntityCallback(requestEntity, ByteArrayResponseExtractor.class);
        byte[] body = restTemplate.execute(fileURL, HttpMethod.GET, requestCallback, byteArrayResponseExtractor);
        Files.write(Paths.get(fileFullPath), Objects.requireNonNull(body));
    }

    public static void downloadUseJdk(String url, String dir, String fileName) throws Exception {
        URL downloadUrl = new URL(url);
        URLConnection connection = null;
        if (proxy != null) {
            downloadUrl.openConnection(proxy);
        } else {
            downloadUrl.openConnection();
        }
        connection.setConnectTimeout(60000);
        connection.setReadTimeout(60000);
        InputStream in = connection.getInputStream();
        File file = new File(dir);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
        }
        if (fileName == null || fileName.length() == 0) {
            fileName = url.substring(url.lastIndexOf("/") + 1);
        }
        FileOutputStream out = new FileOutputStream(file + "\\" + fileName);
        IOUtils.copyLarge(in, out);
        out.flush();
        in.close();
        out.close();
    }

    public static void downloadMultiFile(List<String> sourceUrls, String dir, int threadNum, Task task) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        List<CompletableFuture<?>> futureList = new ArrayList<>();
        for (String sourceUrl : sourceUrls) {
            CompletableFuture<?> completableFuture = CompletableFuture
                    .runAsync(() -> {
                        try {
                            downloadUseJdk(sourceUrl, dir, null);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }, executorService);
            futureList.add(completableFuture);
        }
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
        completableFuture.whenComplete((o, e) -> {
            task.run();
        });
        completableFuture.join();
        executorService.shutdown();
    }

    //public static void main(String[] args) {
    //    List<String> sourceUrls = new ArrayList<>();
    //    sourceUrls.add("https://h0.monidai.com/20221006/zt9vwWN9/1000kb/hls/9UH8L2741000.ts");
    //    sourceUrls.add("https://h0.monidai.com/20221006/zt9vwWN9/1000kb/hls/9UH8L2741001.ts");
    //    sourceUrls.add("https://h0.monidai.com/20221006/zt9vwWN9/1000kb/hls/9UH8L2741002.ts");
    //    sourceUrls.add("https://h0.monidai.com/20221006/zt9vwWN9/1000kb/hls/9UH8L2741003.ts");
    //    sourceUrls.add("https://h0.monidai.com/20221006/zt9vwWN9/1000kb/hls/9UH8L2741004.ts");
    //    sourceUrls.add("https://h0.monidai.com/20221006/zt9vwWN9/1000kb/hls/9UH8L2741005.ts");
    //    sourceUrls.add("https://h0.monidai.com/20221006/zt9vwWN9/1000kb/hls/9UH8L2741006.ts");
    //    sourceUrls.add("https://h0.monidai.com/20221006/zt9vwWN9/1000kb/hls/9UH8L2741007.ts");
    //    sourceUrls.add("https://h0.monidai.com/20221006/zt9vwWN9/1000kb/hls/9UH8L2741008.ts");
    //    sourceUrls.add("https://h0.monidai.com/20221006/zt9vwWN9/1000kb/hls/9UH8L2741009.ts");
    //    sourceUrls.add("https://h0.monidai.com/20221006/zt9vwWN9/1000kb/hls/9UH8L2741010.ts");
    //    //HttpUtil.downloadFile("https://h0.monidai.com/20221006/zt9vwWN9/1000kb/hls/9UH8L2741000.ts", new File("D:\\temp\\m3u8\\TEA", "AAA.ts"));
    //    downloadMultiFile(sourceUrls, "D:\\temp\\m3u8\\TEA", 2, new Task() {
    //        @Override
    //        public void run() throws RuntimeException {
    //            System.out.println("下载完成啦");
    //        }
    //    });
    //    System.out.println("End");
    //}
}
