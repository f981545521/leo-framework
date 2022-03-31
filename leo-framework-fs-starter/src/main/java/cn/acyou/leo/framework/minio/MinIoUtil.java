package cn.acyou.leo.framework.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;

/**
 * @author youfang
 * @version [1.0.0, 2022/3/31 16:56]
 **/
@Slf4j
public class MinIoUtil {

    private MinioClient minioClient;

    public MinIoUtil(MinioClient minioClient) {
        log.info("MinIoUtil 初始化完成。");
        this.minioClient = minioClient;
    }

    public void makeBucket(String name) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
