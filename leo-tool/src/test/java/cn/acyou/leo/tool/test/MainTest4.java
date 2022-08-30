package cn.acyou.leo.tool.test;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

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
}
