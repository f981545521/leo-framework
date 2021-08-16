package cn.acyou.leo.framework.downloader;

import cn.acyou.leo.framework.downloader.ext.ByteArrayResponseExtractor;
import cn.acyou.leo.framework.downloader.support.DownloadProgressPrinter;
import cn.acyou.leo.framework.downloader.utils.RestTemplateBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-16 17:30]
 */
@Slf4j
public class DownloadUtil {
    private static final RestTemplate restTemplate;
    private static final DownloadProgressPrinter downloadProgressPrinter;

    static {
        restTemplate = RestTemplateBuilder.builder().build();
        downloadProgressPrinter = DownloadProgressPrinter.defaultDownloadProgressPrinter();
    }

    private DownloadUtil(){

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
    public static void download(String fileURL, String dir, String fileName) throws Exception {
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
        File file = new File(fileFullPath);
        boolean mkdirs = file.getParentFile().mkdirs();
        if (mkdirs) {
            log.info("auto create directory ：{}", file.getParentFile().getAbsolutePath());
        }
        byte[] body = restTemplate.execute(fileURL, HttpMethod.GET, null,
                new ByteArrayResponseExtractor(downloadProgressPrinter));
        Files.write(Paths.get(fileFullPath), Objects.requireNonNull(body));
    }
    /**
     * 下载
     *
     * @param fileURL      文件的url
     * @param fileFullPath 文件的完整路径
     * @throws Exception 异常
     */
    public static void download2(String fileURL, String fileFullPath, Map<String, String> headerMaps) throws Exception {
        File file = new File(fileFullPath);
        boolean mkdirs = file.getParentFile().mkdirs();
        if (mkdirs) {
            log.info("auto create directory ：{}", file.getParentFile().getAbsolutePath());
        }
        HttpHeaders headers = new HttpHeaders();
        headerMaps.forEach(headers::add);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ByteArrayResponseExtractor byteArrayResponseExtractor = new ByteArrayResponseExtractor(downloadProgressPrinter);
        RequestCallback requestCallback = restTemplate.httpEntityCallback(requestEntity, ByteArrayResponseExtractor.class);
        byte[] body = restTemplate.execute(fileURL, HttpMethod.GET, requestCallback, byteArrayResponseExtractor);
        Files.write(Paths.get(fileFullPath), Objects.requireNonNull(body));
    }
}
