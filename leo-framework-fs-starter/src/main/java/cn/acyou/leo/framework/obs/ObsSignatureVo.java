package cn.acyou.leo.framework.obs;

import lombok.Data;

/**
 * @author youfang
 * @version [1.0.0, 2022/9/16 16:50]
 **/
@Data
public class ObsSignatureVo {

    /**
     * AK
     */
    private String accessKeyId;

    /**
     * 对象ACL权限
     */
    private String xObsAcl;

    /**
     * 对象MIME类型
     */
    private String contextType;

    /**
     * policy的base64编码值
     */
    protected String policy;

    /**
     * 签名串信息
     */
    protected String signature;
}
