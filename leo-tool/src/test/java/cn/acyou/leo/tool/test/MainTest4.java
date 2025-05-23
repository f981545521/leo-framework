package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.util.*;
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
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/30 9:40]
 **/
public class MainTest4 {
    @Test
    public void test123423() throws Exception {
        //File file = new File("C:\\Users\\1\\Videos\\resources\\target\\111.txt");
        //PrintWriter printWriter = new PrintWriter(file);
        //printWriter.write("你好");
        //printWriter.flush();
        //FileUtil.writeString("你好", file, StandardCharsets.UTF_8);
        System.out.println(FileUtil.getNamePrefix("C:\\Users\\1\\Videos\\resources\\target\\111.txt"));
        System.out.println(FileUtil.getFullPath("C:\\Users\\1\\Videos\\resources\\target\\111.txt"));
        System.out.println(FileUtil.getFullPathNoEndSeparator("C:\\Users\\1\\Videos\\resources\\target\\111.txt"));
        System.out.println(FileUtil.getBaseName("C:\\Users\\1\\Videos\\resources\\target\\111.txt"));
        System.out.println(FileUtil.getName("C:\\Users\\1\\Videos\\resources\\target\\111.txt"));
        System.out.println(FileUtil.extName("C:\\Users\\1\\Videos\\resources\\target\\111.txt"));
        System.out.println(FileUtil.getPrefix("C:\\Users\\1\\Videos\\resources\\target\\111.txt"));
    }


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
    public void test435() {
        System.out.println(String.format("part%sto%s", 1000, 2000));
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


    @Test
    public void test12354() {
        String startDateStr = "10:42";
        Date startDate = DateUtil.parseTime(new Date(), startDateStr + ":00");
        Date noonBreakStart = DateUtil.parseTime(new Date(), "12:00:00");
        Date noonBreakEnd = DateUtil.parseTime(new Date(), "13:30:00");
        Date eveningBreakStart = DateUtil.parseTime(new Date(), "18:30:00");
        Date eveningBreakEnd = DateUtil.parseTime(new Date(), "17:30:00");
        Date lastDate = null;
        for (int i = 1; i < 10; i++) {
            Date endDate = DateUtil.addHour(startDate, i);
            System.out.print(i + "小时|转换工时：" + Calculator.val(i).divide(8) + " -> ");
            if (endDate.before(noonBreakStart)) {
                lastDate = endDate;
                System.out.print(DateUtil.getDateTimeFormat(endDate));
            } else if (endDate.after(noonBreakStart) && endDate.before(noonBreakEnd)) {
                BigDecimal diff = DateUtil.getDiff(noonBreakStart, endDate, DateUtil.Unit.MINUTE);
                lastDate = DateUtil.addMinutes(noonBreakEnd, diff.intValue());
                System.out.print(DateUtil.getDateTimeFormat(lastDate));
            } else if (endDate.after(noonBreakEnd) && endDate.before(eveningBreakStart)) {
                lastDate = DateUtil.addHour(lastDate, 1);
                System.out.print(DateUtil.getDateTimeFormat(lastDate));
            }
            System.out.println();
        }
    }


    @Test
    public void test123524() {
        String startDateStr = "10:42";
        Date startDate = DateUtil.parseTime(new Date(), startDateStr + ":00");
        Date noonBreakStart = DateUtil.parseTime(new Date(), "12:00:00");
        Date noonBreakEnd = DateUtil.parseTime(new Date(), "13:30:00");
        Date eveningBreakStart = DateUtil.parseTime(new Date(), "18:30:00");
        Date eveningBreakEnd = DateUtil.parseTime(new Date(), "17:30:00");
        for (int i = 1; i < 10; i++) {
            Date endDate = DateUtil.addHour(startDate, i);
            System.out.print(i + "小时|转换工时：" + Calculator.val(i).divide(8) + " -> ");
            if (endDate.before(noonBreakStart)) {
                System.out.print(DateUtil.getDateTimeFormat(endDate));
            } else if (endDate.after(noonBreakStart) && endDate.before(noonBreakEnd)) {
                BigDecimal diff = DateUtil.getDiff(noonBreakStart, endDate, DateUtil.Unit.MINUTE);
                Date lastDate = DateUtil.addMinutes(noonBreakEnd, diff.intValue());
                System.out.print(DateUtil.getDateTimeFormat(lastDate));
            } else if (endDate.after(noonBreakEnd) && endDate.before(eveningBreakStart)) {
                System.out.print(DateUtil.getDateTimeFormat(DateUtil.addMinutes(endDate, 90)));
            } else if (endDate.after(eveningBreakStart) && endDate.before(eveningBreakEnd)) {
                System.out.print(DateUtil.getDateTimeFormat(DateUtil.addMinutes(endDate, 90)));
            }
            System.out.println();
        }
    }
}
