import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * @author youfang
 * @version [1.0.0, 2021/12/27 17:11]
 **/
public class MainTest {

    static String regionId = "cn-beijing";
    static String ak = "**";
    static String as = "**";
    static String roleArn = "acs:ram::1121059432221011:role/ramosstest";
    static String roleSessionName = "test";

    private static final String POLICY = "{\n" +
            "    \"Version\": \"1\",\n" +
            "    \"Statement\": [\n" +
            "        {\n" +
            "            \"Effect\": \"Allow\",\n" +
            "            \"Action\": [\n" +
            "                \"oss:Get*\",\n" +
            "                \"oss:Put*\",\n" +
            "                \"oss:AbortMultipartUpload\",\n" +
            "                \"oss:ListObjects\",\n" +
            "                \"oss:ListParts\"\n" +
            "            ],\n" +
            "            \"Resource\": [\n" +
            "                \"acs:oss:*:*:%s\",\n" +
            "                \"acs:oss:*:*:%s/%s*\"\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    public static void main(String[] args) throws Exception {
        AssumeRoleResponse.Credentials credentials = getSTSToken();
        IClientProfile profile = DefaultProfile.getProfile(regionId, credentials.getAccessKeyId(), credentials.getAccessKeySecret(), credentials.getSecurityToken());
        System.out.println(profile);
    }

    public static AssumeRoleResponse.Credentials getSTSToken() throws Exception {
        String stsRegionId = regionId;
        //使用B账号的子账号AK信息进行调用
        String safAccessId = ak;
        String safAccessSecret = as;
        IClientProfile profile = DefaultProfile.getProfile(stsRegionId, safAccessId, safAccessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        AssumeRoleRequest request = new AssumeRoleRequest();
        // 指定请求方法
        request.setMethod(com.aliyuncs.http.MethodType.POST);
        request.setAcceptFormat(FormatType.JSON);
        //获取方式详细见步骤1.3
        request.setRoleArn(roleArn);
        //A账号的主账号ID
        request.setRoleSessionName(roleSessionName);
        request.setPolicy(POLICY);
        AssumeRoleResponse httpResponse = client.getAcsResponse(request);
        return httpResponse.getCredentials();
    }
}
