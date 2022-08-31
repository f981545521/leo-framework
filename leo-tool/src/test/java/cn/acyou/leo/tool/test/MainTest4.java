package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.util.HttpClientUtil;
import cn.acyou.leo.framework.util.HttpUtil2;
import com.google.common.io.Resources;
import info.monitorenter.cpdetector.io.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/30 9:40]
 **/
public class MainTest4 {
    public static void main(String[] args) throws Exception {
        //String s = HttpUtil.get("https://www.fastmock.site/mock/5039c4361c39a7e3252c5b55971f1bd3/api/demo/list?page=1&pageSize=20&_=1661823663208");
        //System.out.println(s);
        //HttpUtil.shutdown();

        CloseableHttpClient client = HttpClients.createDefault();
        HttpUriRequest request = new HttpGet("https://www.fastmock.site/mock/5039c4361c39a7e3252c5b55971f1bd3/api/demo/list?page=1&pageSize=20&_=1661823663208");
        String execute = client.execute(request, new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return EntityUtils.toString(response.getEntity());
            }
        });
        System.out.println(execute);
    }

    @Test
    public void test23() {
        String s = HttpClientUtil.doGet("https://www.fastmock.site/mock/5039c4361c39a7e3252c5b55971f1bd3/api/demo/list?page=1&pageSize=20&_=1661823663208");
        System.out.println(s);
    }

    @Test
    public void test233() {
        String s = HttpUtil2.get("https://www.fastmock.site/mock/5039c4361c39a7e3252c5b55971f1bd3/api/demo/list?page=1&pageSize=20&_=1661823663208");
        System.out.println(s);
    }

    @Test
    public void test2233() throws Exception {
        URL resource = Resources.getResource("测试文本.txt");
        File file = new File(URLDecoder.decode(resource.getPath()));
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        detector.add(new ParsingDetector(false));
        detector.add(JChardetFacade.getInstance());
        detector.add(ASCIIDetector.getInstance());
        detector.add(UnicodeDetector.getInstance());
        Charset charset = detector.detectCodepage(file.toURI().toURL());
        System.out.println(charset.name());
        String s = StreamUtils.copyToString(new FileInputStream(file), charset);
        String s1 = StreamUtils.copyToString(new FileInputStream(file), StandardCharsets.UTF_8);
        System.out.println(s);
        System.out.println(s1);
    }
}
