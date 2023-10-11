package cn.acyou.leo.framework.downloader;

import cn.acyou.leo.framework.downloader.ext.ByteArrayResponseExtractor;
import cn.acyou.leo.framework.downloader.support.DownloadProgressPrinter;
import cn.acyou.leo.framework.downloader.utils.RestTemplateBuilder;
import cn.acyou.leo.framework.util.UrlUtil;
import cn.acyou.leo.framework.util.WorkUtil;
import cn.acyou.leo.framework.util.function.Task;
import cn.hutool.core.math.Calculator;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
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
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

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

    /**
     * 为本类设置代理
     *
     * @param proxy 代理
     */
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

    /**
     * 下载使用hutool
     *
     * @param url      url
     * @param dir      dir
     * @param fileName 文件名称
     */
    public static void downloadUseHutool(String url, String dir, String fileName) {
        if (fileName == null || fileName.length() == 0) {
            fileName = UrlUtil.getName(url);
        }
        HttpResponse response = HttpUtil.createGet(url, true)
                .setProxy(proxy)
                .timeout(1000 * 60 * 10)
                .executeAsync();
        final File file = response.completeFileNameFromHeader(new File(dir, fileName));
        response.writeBody(file);
    }

    /**
     * 下载使用jdk
     *
     * @param url      url
     * @param dir      dir
     * @param fileName 文件名称
     * @throws Exception 异常
     */
    public static void downloadUseJdk(String url, String dir, String fileName) throws Exception {
        if (fileName == null || fileName.length() == 0) {
            fileName = UrlUtil.getName(url);
        }
        URL downloadUrl = new URL(url);
        URLConnection connection;
        if (proxy != null) {
            connection = downloadUrl.openConnection(proxy);
        } else {
            connection = downloadUrl.openConnection();
        }
        connection.setConnectTimeout(60000);
        connection.setReadTimeout(60000);
        InputStream in = connection.getInputStream();
        File file = new File(dir);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(file + "\\" + fileName);
        IOUtils.copyLarge(in, out);
        out.flush();
        in.close();
        out.close();
    }

    /**
     * 使用多线程下载多文件
     *
     * @param sourceUrls 源地址
     * @param dir        目标目录
     * @param threadNum  线程数
     * @param task       回调函数
     */
    public static void downloadMultiFile(List<String> sourceUrls, String dir, int threadNum, Task task) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        List<CompletableFuture<?>> futureList = new ArrayList<>();
        WorkUtil.watch(() -> {
            log.info("多线程下载任务执行开始 -> 任务数：{} 线程数：{}", sourceUrls.size(), threadNum);
            for (String sourceUrl : sourceUrls) {
                CompletableFuture<?> completableFuture = CompletableFuture
                        .runAsync(() -> {
                            try {
                                ThreadPoolExecutor executor = (ThreadPoolExecutor) executorService;
                                log.info("当前下载：{} 剩余任务：{}", sourceUrl, executor.getQueue().size());
                                WorkUtil.doRetryWork(3, ()->{
                                    downloadUseHutool(sourceUrl, dir, null);
                                });
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                            }
                        }, executorService);
                futureList.add(completableFuture);
            }
            CompletableFuture<Void> completableFuture = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
            completableFuture.whenComplete((o, e) -> task.run());
            completableFuture.join();
            executorService.shutdown();
            log.info("多线程下载任务执行结束");
        });

    }

    /**
     * 使用多线程下载多文件
     *
     * @param sourceUrls   源地址
     * @param dir          目标目录
     * @param executor     执行器
     * @param completeTask 回调函数
     */
    public static void downloadMultiFile(List<String> sourceUrls, String dir, Consumer<Integer> progressTask, Task completeTask, ThreadPoolExecutor executor) {
        List<CompletableFuture<?>> futureList = new ArrayList<>();
        WorkUtil.watch(() -> {
            log.info("多线程下载任务执行开始 -> 任务数：{} 线程数：{}", sourceUrls.size(), executor.getCorePoolSize());
            AtomicInteger downloadedCount = new AtomicInteger();
            for (String sourceUrl : sourceUrls) {
                CompletableFuture<?> completableFuture = CompletableFuture
                        .runAsync(() -> {
                            try {
                                int perm = (int) Calculator.conversion(downloadedCount.getAndIncrement() + " / " + sourceUrls.size() + "*100");
                                log.info("当前下载：{} -> {} 剩余任务：{}  进度：{}%", sourceUrl, dir, executor.getQueue().size(), perm);
                                WorkUtil.doRetryWork(3, () -> {
                                    downloadUseHutool(sourceUrl, dir, null);
                                });
                                if (progressTask != null) {
                                    progressTask.accept(perm);
                                }
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                            }
                        }, executor);
                futureList.add(completableFuture);
            }
            CompletableFuture<Void> completableFuture = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
            completableFuture.whenComplete((o, e) -> {
                if (progressTask != null) {
                    progressTask.accept(100);
                }
                completeTask.run();
            });
            completableFuture.join();
            log.info("多线程下载任务执行结束");
        });
    }

    //public static void main(String[] args) throws Exception {
    //    String url = "https://vipmp4i.vodfile.m1905.com/202212021048/b90507f1aa225fbdb02fcacb0118f1d1/video/2022/08/26/v202208267231O6PORJB98FZF/v202208267231O6PORJB98FZF.mp4";
    //    //downloadUseHutool(url, "D:\\temp\\", UrlUtil.getName(url));
    //    //downloadUseJdk(url, "D:\\temp\\", UrlUtil.getName(url));
    //    downloadFile(url, "D:\\temp\\", UrlUtil.getName(url));
    //}

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
