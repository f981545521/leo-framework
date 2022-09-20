package cn.acyou.leo.framework.obs;

import lombok.Data;

import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2022/9/16 16:50]
 **/
@Data
public class ObsSignatureTemporaryVo {
    /**
     * 鉴权信息的URL
     */
    private String signedUrl;

    /**
     * 鉴权信息头
     */
    private Map<String, String> actualSignedRequestHeaders;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 端点
     */
    private String endPoint;

    /**
     * 桶
     */
    private String bucketName;


}
