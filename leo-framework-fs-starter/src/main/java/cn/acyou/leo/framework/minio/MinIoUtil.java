package cn.acyou.leo.framework.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;

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

    public void putObject(String bucket, String objectKey, File file) throws Exception {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(objectKey)
                .stream(new FileInputStream(file), file.length(), -1)
                .build());
    }

}
