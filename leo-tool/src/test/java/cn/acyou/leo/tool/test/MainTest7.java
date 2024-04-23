package cn.acyou.leo.tool.test;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.alimt20181012.AsyncClient;
import com.aliyun.sdk.service.alimt20181012.models.GetDetectLanguageRequest;
import com.aliyun.sdk.service.alimt20181012.models.GetDetectLanguageResponse;
import com.aliyun.sdk.service.alimt20181012.models.TranslateGeneralRequest;
import com.aliyun.sdk.service.alimt20181012.models.TranslateGeneralResponse;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;

import java.util.concurrent.CompletableFuture;


/**
 * https://help.aliyun.com/zh/machine-translation/developer-reference/api-alimt-2018-10-12-translategeneral?spm=a2c4g.11186623.0.0.25931a88EbnviI
 *
 * @author youfang
 * @version [1.0.0, 2024/4/23 14:10]
 **/
public class MainTest7 {
    public static void main(String[] args) throws Exception {

        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId("LTAI5tLxNLqUWKEL4zotuXKe")
                .accessKeySecret("*")
                .build());

        AsyncClient client = AsyncClient.builder()
                .region("cn-hangzhou")
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("mt.cn-hangzhou.aliyuncs.com")
                )
                .build();

        //识别语种
        GetDetectLanguageRequest getDetectLanguageRequest = GetDetectLanguageRequest.builder()
                .sourceText("今天晚上吃什么")
                .build();
        CompletableFuture<GetDetectLanguageResponse> response = client.getDetectLanguage(getDetectLanguageRequest);
        GetDetectLanguageResponse resp = response.get();
        System.out.println("识别语种结果：" + new Gson().toJson(resp));

        //文本翻译
        TranslateGeneralRequest translateGeneralRequest = TranslateGeneralRequest.builder()
                .formatType("text")
                .sourceLanguage("zh")
                .targetLanguage("en")
                .sourceText("今天晚上吃什么？")
                .scene("general")
                .build();

        CompletableFuture<TranslateGeneralResponse> response2 = client.translateGeneral(translateGeneralRequest);
        TranslateGeneralResponse resp2 = response2.get();
        System.out.println("文本翻译结果：" + new Gson().toJson(resp2));


        client.close();
    }
}
