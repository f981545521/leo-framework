package cn.acyou.leo.framework.obs;

import cn.acyou.leo.framework.exception.FsException;
import cn.acyou.leo.framework.listener.ProgressCallback;
import cn.acyou.leo.framework.prop.OBSProperty;
import com.obs.services.ObsClient;
import com.obs.services.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2022/8/26 10:34]
 **/
@Slf4j
public class OBSUtil {
    private final OBSProperty obsProperty;
    private final ObsClient obsClient;

    public OBSUtil(OBSProperty obsProperty) {
        this.obsProperty = obsProperty;
        obsClient = initObsClient();
        log.info("OBSUtil 初始化完成。");
    }

    private ObsClient initObsClient() {
        log.info("OBS Client 初始化...");
        return new ObsClient(obsProperty.getAccessKeyId(), obsProperty.getAccessKeySecret(), obsProperty.getEndpoint());
    }


    /**
     * 上传网络流
     *
     * @param bucket   桶
     * @param fileName 文件名称
     * @param url      内容
     */
    public void uploadURL(String bucket, String fileName, String url) {
        InputStream inputStream;
        try {
            inputStream = new URL(url).openStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FsException("网络连接失败！");
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
    public void uploadLocalFile(String bucket, String fileName, String localFilePath) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(localFilePath);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FsException("文件读取失败！");
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
    public void uploadLocalFile(String bucket, String fileName, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, file);
        obsClient.putObject(putObjectRequest);
    }

    /**
     * 上传（带进度）
     *
     * @param bucket   桶
     * @param fileName 文件名称
     * @param file     文件上传
     * @param process  进度回调
     */
    public void uploadWithProcess(String bucket, String fileName, File file, ProgressCallback process) {
        // 上传文件的同时指定了进度条参数。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, file);
        putObjectRequest.setProgressListener(new ProgressListener() {
            @Override
            public void progressChanged(ProgressStatus status) {
                // 获取上传平均速率
                System.out.println("AverageSpeed:" + status.getAverageSpeed());
                // 获取上传进度百分比
                System.out.println("TransferPercentage:" + status.getTransferPercentage());
            }
        });
        obsClient.putObject(putObjectRequest);
    }

    /**
     * 上传网络流
     *
     * @param bucket      桶
     * @param fileName    文件名称
     * @param inputStream 输入流
     */
    public void uploadInputStream(String bucket, String fileName, InputStream inputStream) {
        obsClient.putObject(bucket, fileName, inputStream);
    }

    /**
     * 上传字符串
     *
     * @param bucket   桶
     * @param fileName 文件名称
     * @param content  内容
     */
    public void uploadString(String bucket, String fileName, String content) {
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
    public void uploadByte(String bucket, String fileName, byte[] bytes) {
        obsClient.putObject(bucket, fileName, new ByteArrayInputStream(bytes));
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
    public AppendObjectResult uploadAppend(String bucket, String fileName, InputStream inputStream, long position, ObjectMetadata meta) {
        AppendObjectRequest appendObjectRequest = new AppendObjectRequest(bucket);
        //, fileName, inputStream, meta
        appendObjectRequest.setPosition(position);
        return obsClient.appendObject(appendObjectRequest);
    }

    public boolean uploadBigFile(String bucket, String fileName, File file) {
        UploadFileRequest request = new UploadFileRequest(bucket, fileName);
        // 设置待上传的本地文件，localfile为待上传的本地文件路径，需要指定到具体的文件名
        request.setUploadFile(file.getAbsolutePath());
        // 设置分段上传时的最大并发数
        request.setTaskNum(5);
        // 设置分段大小为10MB
        request.setPartSize(10 * 1024 * 1024);
        // 开启断点续传模式
        request.setEnableCheckpoint(true);
        try {
            // 进行断点续传上传
            CompleteMultipartUploadResult result = obsClient.uploadFile(request);
            return true;
        } catch (Exception e) {
            // 发生异常时可再次调用断点续传上传接口进行重新上传
            uploadBigFile(bucket, fileName, file);
        }
        return false;
    }

    /**
     * 下载
     *
     * @param bucket   桶
     * @param fileName 文件名称
     * @return {@link ObsObject}
     */
    public ObsObject download(String bucket, String fileName) {
        return obsClient.getObject(bucket, fileName);
    }

    /**
     * 下载
     *
     * @param bucket   桶
     * @param fileName 文件名称
     * @param file     文件
     */
    public boolean downloadLocalFile(String bucket, String fileName, File file) {
        try {
            ObsObject object = obsClient.getObject(new GetObjectRequest(bucket, fileName));
            FileCopyUtils.copy(object.getObjectContent(), new FileOutputStream(file));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 下载（带进度）
     *
     * @param bucket   桶
     * @param fileName 文件名称
     * @param file     文件
     * @param callback 回调
     */
    public boolean downloadWithProcess(String bucket, String fileName, File file, ProgressCallback callback) {
        GetObjectRequest request = new GetObjectRequest(bucket, fileName);
        request.setProgressListener(new ProgressListener() {

            @Override
            public void progressChanged(ProgressStatus status) {
                // 获取下载平均速率
                System.out.println("AverageSpeed:" + status.getAverageSpeed());
                // 获取下载进度百分比
                System.out.println("TransferPercentage:" + status.getTransferPercentage());
            }
        });
        // 每下载1MB数据反馈下载进度
        request.setProgressInterval(1024 * 1024L);
        try {
            ObsObject object = obsClient.getObject(request);
            FileCopyUtils.copy(object.getObjectContent(), new FileOutputStream(file));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }


    /**
     * 判断文件是否存在
     *
     * @param bucketName bucketName
     * @param objectName objectName
     * @return true 存在/false 不存在
     */
    public boolean existFile(String bucketName, String objectName) {
        // 判断文件是否存在。
        return obsClient.doesObjectExist(bucketName, objectName);
    }


    /**
     * 删除文件
     *
     * @param bucketName Bucket
     * @param objectName 对象
     */
    public void deleteFile(String bucketName, String objectName) {
        obsClient.deleteObject(bucketName, objectName);
    }

    /**
     * 删除 Bucket
     *
     * @param bucketName bucketName
     */
    public void deleteBucket(String bucketName) {
        obsClient.deleteBucket(bucketName);
    }

    /**
     * 创建 Bucket
     *
     * @param bucketName bucketName
     */
    public ObsBucket createBucket(String bucketName) {
        return obsClient.createBucket(bucketName);
    }

    /**
     * 列举存储空间。
     */
    public List<String> listBuckets() {
        ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
        List<ObsBucket> buckets = obsClient.listBuckets(listBucketsRequest);
        List<String> buckNameList = new ArrayList<>();
        for (ObsBucket input : buckets) {
            String name = input.getBucketName();
            buckNameList.add(name);
        }
        return buckNameList;
    }


    /**
     * 关闭
     */
    public void shutdown() {
        try {
            obsClient.close();
        } catch (IOException e) {
            //ignore
        }
    }

}
