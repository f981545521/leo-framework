package cn.acyou.leo.tool.test;

import ch.qos.logback.classic.Level;
import cn.acyou.leo.framework.downloader.utils.RestTemplateBuilder;
import cn.acyou.leo.framework.util.IOUtil;
import cn.acyou.leo.framework.util.LoggerUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/**
 * @author youfang
 * @version [1.0.0, 2024/6/17 14:08]
 **/
@Slf4j
public class TestDownload {
    public static String url = "http://qiniu.acyou.cn/video/20240617142317%40%E6%95%99%E4%BD%A0%E6%80%8E%E4%B9%88%E6%B7%B7%E7%A4%BE%E4%BC%9A03%E4%B8%AD%E5%9B%BD%E6%9C%80%E7%9C%9F%E5%AE%9E%E7%9A%84%E7%A4%BE%E4%BA%A4%E5%85%B3%E7%B3%BB.mp4";

    @Test
    public void test1() throws Exception {
        URL downloadUrl = new URL(url);
        URLConnection connection = downloadUrl.openConnection();
        connection.setConnectTimeout(60000);
        connection.setReadTimeout(60000);
        File file = new File("D:\\temp1\\" + UUID.randomUUID() + ".mp4");
        if (!file.getParentFile().exists()) {
            boolean mkdirs = file.getParentFile().mkdirs();
        }
        long start = System.currentTimeMillis();
        log.info("准备开始");
        InputStream in = connection.getInputStream();
        FileOutputStream out = new FileOutputStream(file);
        IOUtils.copyLarge(in, out);
        log.info("下载结束:" + (System.currentTimeMillis() - start));
        out.flush();
        in.close();
        out.close();
    }

    @Test
    public void test3() throws Exception {
        HttpResponse response = HttpUtil.createGet(url, true)
                .timeout(1000 * 60 * 10)
                .executeAsync();
        File file = new File("D:\\temp1\\" + UUID.randomUUID() + ".mp4");
        log.info("准备开始");
        long start = System.currentTimeMillis();
        response.writeBody(file);
        log.info("下载结束:" + (System.currentTimeMillis() - start));
    }

    @Test
    public void test4() throws Exception {
        log.info("准备开始");
        long start = System.currentTimeMillis();
        File file = new File("D:\\temp1\\" + UUID.randomUUID() + ".mp4");
        RestTemplate restTemplate = RestTemplateBuilder.builder().build();
        FileOutputStream out = new FileOutputStream(file);
        ResponseExtractor<Boolean> responseExtractor = clientHttpResponse -> {
            StreamUtils.copy(clientHttpResponse.getBody(), out);
            return true;
        };
        Boolean execute = restTemplate.execute(new URI(url), HttpMethod.GET, null, responseExtractor);
        log.info("下载结束:" + (System.currentTimeMillis() - start));
    }

    @Test
    public void test5() throws Exception {
        log.info("准备开始");
        long start = System.currentTimeMillis();
        File file = new File("D:\\temp1\\" + UUID.randomUUID() + ".mp4");
        LoggerUtil.setLevel("org.apache.http.wire", Level.OFF);

        HttpClient client = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        org.apache.http.HttpResponse response = client.execute(httpget);
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        boolean mkdirs = file.getParentFile().mkdirs();
        FileOutputStream fileout = new FileOutputStream(file);
        IOUtil.copyLarge(is, fileout);
        fileout.flush();
        is.close();
        fileout.close();

        log.info("下载结束:" + (System.currentTimeMillis() - start));
    }
}
