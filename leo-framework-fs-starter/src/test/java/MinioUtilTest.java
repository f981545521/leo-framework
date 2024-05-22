import cn.acyou.leo.framework.minio.MinIoUtil;
import io.minio.BucketExistsArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;

import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/26 13:41]
 **/
public class MinioUtilTest {
    public static void main(String[] args) throws Exception {
        String endpoint = "http://localhost:9000";
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials("minioadmin", "minioadmin")
                .build();
        MinIoUtil minIoUtil = new MinIoUtil(minioClient);
        //查询Bucket列表
        List<Bucket> buckets = minIoUtil.listBuckets();
        System.out.println(buckets);
        //bucket是否存在
        boolean dev = minioClient.bucketExists(BucketExistsArgs.builder().bucket("dev").build());
        System.out.println(dev);
        if (!dev) {
            minIoUtil.makeBucket("dev");
        }
        //File file = new File("C:\\Users\\1\\Pictures\\6.jpg");
        //minIoUtil.putObject("dev", "/images1/6.jpg", file);
        String path = "/images/6.jpg";
        minIoUtil.putObjectHttp("dev", path, "http://qiniu.acyou.cn/images/6.jpg");
        System.out.println("访问地址:" + endpoint + "/dev" + path);
        Iterable<Result<Item>> results = minioClient.listObjects( ListObjectsArgs.builder()
                .bucket("dev")
                .maxKeys(100)
                .includeVersions(true)
                .build());
        System.out.println(results);
    }
}
