import cn.acyou.leo.framework.minio.MinIoUtil;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.messages.Bucket;

import java.io.File;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/26 13:41]
 **/
public class MinioUtilTest {
    public static void main(String[] args) throws Exception {
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://192.168.4.65:9000")
                .credentials("minioadmin", "minioadmin")
                .build();
        MinIoUtil minIoUtil = new MinIoUtil(minioClient);
        //查询Bucket列表
        List<Bucket> buckets = minioClient.listBuckets();
        System.out.println(buckets);
        //bucket是否存在
        boolean dev2 = minioClient.bucketExists(BucketExistsArgs.builder().bucket("dev2").build());
        System.out.println(dev2);
        //
        File file = new File("C:\\Users\\1\\Pictures\\appStart.png");
        minIoUtil.putObject("dev2", "/images/appStart.png", file);
    }
}
