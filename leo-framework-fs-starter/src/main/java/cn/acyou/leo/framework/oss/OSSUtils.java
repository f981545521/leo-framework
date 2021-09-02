package cn.acyou.leo.framework.oss;

import cn.acyou.leo.framework.listener.GetObjectProgressListener;
import cn.acyou.leo.framework.listener.ProgressCallback;
import cn.acyou.leo.framework.listener.PutObjectProgressListener;
import cn.acyou.leo.framework.prop.OSSProperty;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-31 13:40]
 */
@Component
@EnableConfigurationProperties(value = OSSProperty.class)
public class OSSUtils {
    private static final Logger log = LoggerFactory.getLogger(OSSUtils.class);
    private static OSSProperty ossProperty;
    private static OSS ossClient;

    @Autowired
    public void setWxConfigProperties(OSSProperty ossProperty) {
        if (!StringUtils.hasText(ossProperty.getEndpoint()) ||
                !StringUtils.hasText(ossProperty.getAccess_key_id()) ||
                !StringUtils.hasText(ossProperty.getAccess_key_secret())) {
            log.warn("OSS 未配置，将影响OSS使用！");
            return;
        }
        OSSUtils.ossProperty = ossProperty;
        ossClient = initOssClient();
    }

    /**
     * 创建OSSClient实例。
     *
     * @return OSSClient实例
     */
    private static OSS initOssClient() {
        log.info("OSS Client 初始化...");
        // 创建ClientConfiguration实例，您可以按照实际情况修改默认参数。
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        // 设置是否支持CNAME。CNAME用于将自定义域名绑定到目标Bucket。
        conf.setSupportCname(true);
        return new OSSClientBuilder()
                .build(ossProperty.getEndpoint(), ossProperty.getAccess_key_id(), ossProperty.getAccess_key_secret(), conf);
    }

    /**
     * 上传网络流
     *
     * @param bucket   桶
     * @param fileName 文件名称
     * @param url      内容
     */
    public static void uploadURL(String bucket, String fileName, String url) {
        InputStream inputStream;
        try {
            inputStream = new URL(url).openStream();
        } catch (IOException e) {
            e.printStackTrace();
            throw new OSSException("网络连接失败！");
        }
        uploadInputStream(bucket, fileName, inputStream);
    }

    /**
     * 上传网络流
     *
     * @param bucket        桶
     * @param fileName      文件名称
     * @param localFilePath 本地文件
     */
    public static void uploadLocalFile(String bucket, String fileName, String localFilePath) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(localFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new OSSException("文件读取失败！");
        }
        uploadInputStream(bucket, fileName, inputStream);
    }

    /**
     * 上传网络流
     *
     * @param bucket   桶
     * @param fileName 文件名称
     * @param file     File对象上传
     */
    public static void uploadLocalFile(String bucket, String fileName, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, file);
        ossClient.putObject(putObjectRequest);
    }

    /**
     * 上传（带进度）
     *
     * @param bucket   桶
     * @param fileName 文件名称
     * @param file     文件上传
     * @param process  进度回调
     */
    public static void uploadWithProcess(String bucket, String fileName, File file, ProgressCallback process) {
        // 上传文件的同时指定了进度条参数。
        ossClient.putObject(new PutObjectRequest(bucket, fileName, file).
                withProgressListener(new PutObjectProgressListener(process)));
    }

    /**
     * 上传网络流
     *
     * @param bucket      桶
     * @param fileName    文件名称
     * @param inputStream 输入流
     */
    public static void uploadInputStream(String bucket, String fileName, InputStream inputStream) {
        ossClient.putObject(bucket, fileName, inputStream);
    }

    /**
     * 上传字符串
     *
     * @param bucket   桶
     * @param fileName 文件名称
     * @param content  内容
     */
    public static void uploadString(String bucket, String fileName, String content) {
        final byte[] bytes = content.getBytes();
        uploadByte(bucket, fileName, bytes);
    }


    /**
     * 上传byte数组
     *
     * @param bucket   桶
     * @param fileName 文件名称
     * @param bytes    内容
     */
    public static void uploadByte(String bucket, String fileName, byte[] bytes) {
        ossClient.putObject(bucket, fileName, new ByteArrayInputStream(bytes));
    }

    /**
     * 追加上传
     * <p>
     * 追加上传是指通过AppendObject方法在已上传的追加类型文件（Appendable Object）末尾直接追加内容。
     *
     * @param bucket      桶
     * @param fileName    文件名称
     * @param inputStream 内容
     * @return 追加上传结果
     */
    public static AppendObjectResult uploadAppend(String bucket, String fileName, InputStream inputStream, long position, ObjectMetadata meta) {
        AppendObjectRequest appendObjectRequest = new AppendObjectRequest(bucket, fileName, inputStream, meta);
        appendObjectRequest.setPosition(position);
        return ossClient.appendObject(appendObjectRequest);
    }

    /**
     * 下载
     *
     * @param bucket   桶
     * @param fileName 文件名称
     * @return {@link OSSObject}
     */
    public static OSSObject download(String bucket, String fileName) {
        return ossClient.getObject(bucket, fileName);
    }

    /**
     * 下载
     *
     * @param bucket   桶
     * @param fileName 文件名称
     * @param file     文件
     */
    public static void downloadLocalFile(String bucket, String fileName, File file) {
        ossClient.getObject(new GetObjectRequest(bucket, fileName), file);
    }

    /**
     * 下载（带进度）
     *
     * @param bucket   桶
     * @param fileName 文件名称
     * @param file     文件
     * @param callback 回调
     */
    public static void downloadWithProcess(String bucket, String fileName, File file, ProgressCallback callback) {
        ossClient.getObject(new GetObjectRequest(bucket, fileName).
                        withProgressListener(new GetObjectProgressListener(callback)),
                file);
    }


    /**
     * 判断文件是否存在
     *
     * @param bucketName bucketName
     * @param objectName objectName
     * @return true 存在/false 不存在
     */
    public static boolean existFile(String bucketName, String objectName) {
        // 判断文件是否存在。
        return ossClient.doesObjectExist(bucketName, objectName);
    }


    /**
     * 列举存储空间。
     */
    public static List<String> listBuckets() {
        List<Bucket> buckets = ossClient.listBuckets();
        List<String> buckNameList = new ArrayList<>();
        for (Bucket input : buckets) {
            String name = input.getName();
            buckNameList.add(name);
        }
        return buckNameList;
    }


    /**
     * 关闭
     */
    public static void shutdown() {
        ossClient.shutdown();
    }


}
