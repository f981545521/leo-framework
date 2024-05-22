package cn.acyou.leo.framework.minio;

import io.minio.*;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2022/3/31 16:56]
 **/
@Slf4j
public class MinIoUtil {

    private MinioClient minioClient;

    public MinIoUtil(MinioClient minioClient) {
        this.minioClient = minioClient;
        log.info("MinIoUtil 初始化完成。");
    }

    /**
     * 创建桶
     *
     * @param name 桶名称
     */
    public void makeBucket(String name) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 存储对象
     *
     * @param bucket    桶名称           示例值："picture-files"
     * @param objectKey 对象的关键       示例值："aaa/bbb/ccc.jpg"
     * @param file      文件
     * @throws Exception 异常
     */
    public void putObject(String bucket, String objectKey, File file) throws Exception {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(objectKey)
                .stream(new FileInputStream(file), file.length(), -1)
                .build());
    }

    /**
     * 存储对象
     *
     * @param bucket    桶名称           示例值："picture-files"
     * @param objectKey 对象的关键       示例值："aaa/bbb/ccc.jpg"
     * @param is        流
     * @throws Exception 异常
     */
    public void putObject(String bucket, String objectKey, InputStream is) throws Exception {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(objectKey)
                .stream(is, is.available(), -1)
                .build());

    }

    /**
     * 桶列表
     *
     * @return {@link List}<{@link Bucket}>
     * @throws Exception 异常
     */
    public List<Bucket> listBuckets() throws Exception {
        return minioClient.listBuckets();
    }

    /**
     * 删除对象
     *
     * @param bucket    桶名称           示例值："picture-files"
     * @param objectKey 对象的关键       示例值："aaa/bbb/ccc.jpg"
     * @throws Exception 异常
     */
    public void removeObject(String bucket, String objectKey) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectKey).build());
    }

    /**
     * 获取原始客户端
     *
     * @return {@link MinioClient}
     */
    public MinioClient getMinioClient() {
        return minioClient;
    }

    public void putObjectHttp(String bucket, String path, String url) throws Exception {
        URLConnection connection = new URL(url).openConnection();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(path)
                .stream(connection.getInputStream(), connection.getContentLengthLong(), -1)
                .build());
    }
}
