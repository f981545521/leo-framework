package cn.acyou.leo.tool.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;

/**
 * https://help.aliyun.com/document_detail/90729.html
 *
 * @author youfang
 * @version [1.0.0, 2022/2/28 15:03]
 **/
@Slf4j
public class ASRUtil {
    // 地域ID，常量，固定值。
    public static final String REGIONID = "cn-shanghai";
    public static final String ENDPOINTNAME = "cn-shanghai";
    public static final String PRODUCT = "nls-filetrans";
    public static final String DOMAIN = "filetrans.cn-shanghai.aliyuncs.com";
    public static final String API_VERSION = "2018-08-17";
    public static final String POST_REQUEST_ACTION = "SubmitTask";
    public static final String GET_REQUEST_ACTION = "GetTaskResult";
    // 请求参数
    public static final String KEY_APP_KEY = "appkey";
    public static final String KEY_FILE_LINK = "file_link";
    public static final String KEY_VERSION = "version";
    public static final String KEY_ENABLE_WORDS = "enable_words";
    // 响应参数
    public static final String KEY_TASK = "Task";
    public static final String KEY_TASK_ID = "TaskId";
    public static final String KEY_STATUS_TEXT = "StatusText";
    public static final String KEY_RESULT = "Result";
    // 状态值
    public static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_RUNNING = "RUNNING";
    private static final String STATUS_QUEUEING = "QUEUEING";
    // 阿里云鉴权client
    IAcsClient client;

    public ASRUtil(String accessKeyId, String accessKeySecret) {
        // 设置endpoint
        DefaultProfile.addEndpoint(REGIONID, PRODUCT, ENDPOINTNAME);
        // 创建DefaultAcsClient实例并初始化
        DefaultProfile profile = DefaultProfile.getProfile(REGIONID, accessKeyId, accessKeySecret);
        this.client = new DefaultAcsClient(profile);
    }

    public String submitFileTransRequest(String appKey, String fileLink) {
        /*
         * 1. 创建CommonRequest，设置请求参数。
         */
        CommonRequest postRequest = new CommonRequest();
        // 设置域名
        postRequest.setSysDomain(DOMAIN);
        // 设置API的版本号，格式为YYYY-MM-DD。
        postRequest.setSysVersion(API_VERSION);
        // 设置action
        postRequest.setSysAction(POST_REQUEST_ACTION);
        // 设置产品名称
        postRequest.setSysProduct(PRODUCT);
        /*
         * 2. 设置录音文件识别请求参数，以JSON字符串的格式设置到请求Body中。
         */
        JSONObject taskObject = new JSONObject();
        // 设置appkey
        taskObject.put(KEY_APP_KEY, appKey);
        // 设置音频文件访问链接
        taskObject.put(KEY_FILE_LINK, fileLink);
        // 新接入请使用4.0版本，已接入（默认2.0）如需维持现状，请注释掉该参数设置。
        taskObject.put(KEY_VERSION, "4.0");
        // 设置是否输出词信息，默认为false，开启时需要设置version为4.0及以上。
        taskObject.put(KEY_ENABLE_WORDS, false);
        String task = taskObject.toJSONString();
        log.info(task);
        // 设置以上JSON字符串为Body参数。
        postRequest.putBodyParameter(KEY_TASK, task);
        // 设置为POST方式的请求。
        postRequest.setSysMethod(MethodType.POST);
        /*
         * 3. 提交录音文件识别请求，获取录音文件识别请求任务的ID，以供识别结果查询使用。
         */
        String taskId = null;
        try {
            CommonResponse postResponse = client.getCommonResponse(postRequest);
            log.info("提交录音文件识别请求的响应：" + postResponse.getData());
            if (postResponse.getHttpStatus() == 200) {
                JSONObject result = JSONObject.parseObject(postResponse.getData());
                String statusText = result.getString(KEY_STATUS_TEXT);
                if (STATUS_SUCCESS.equals(statusText)) {
                    taskId = result.getString(KEY_TASK_ID);
                }
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return taskId;
    }

    public String getFileTransResult(String taskId) {
        /*
         * 1. 创建CommonRequest，设置任务ID。
         */
        CommonRequest getRequest = new CommonRequest();
        // 设置域名
        getRequest.setSysDomain(DOMAIN);
        // 设置API版本
        getRequest.setSysVersion(API_VERSION);
        // 设置action
        getRequest.setSysAction(GET_REQUEST_ACTION);
        // 设置产品名称
        getRequest.setSysProduct(PRODUCT);
        // 设置任务ID为查询参数
        getRequest.putQueryParameter(KEY_TASK_ID, taskId);
        // 设置为GET方式的请求
        getRequest.setSysMethod(MethodType.GET);
        /*
         * 2. 提交录音文件识别结果查询请求
         * 以轮询的方式进行识别结果的查询，直到服务端返回的状态描述为“SUCCESS”或错误描述，则结束轮询。
         */
        String result = null;
        while (true) {
            try {
                CommonResponse getResponse = client.getCommonResponse(getRequest);
                log.info("识别查询结果：" + getResponse.getData());
                if (getResponse.getHttpStatus() != 200) {
                    break;
                }
                JSONObject rootObj = JSONObject.parseObject(getResponse.getData());
                String statusText = rootObj.getString(KEY_STATUS_TEXT);
                if (STATUS_RUNNING.equals(statusText) || STATUS_QUEUEING.equals(statusText)) {
                    // 继续轮询，注意设置轮询时间间隔。
                    Thread.sleep(10000);
                } else {
                    // 状态信息为成功，返回识别结果；状态信息为异常，返回空。
                    if (STATUS_SUCCESS.equals(statusText)) {
                        result = rootObj.getString(KEY_RESULT);
                        // 状态信息为成功，但没有识别结果，则可能是由于文件里全是静音、噪音等导致识别为空。
                        if (result == null) {
                            result = "";
                        }
                    }
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args) {
        final String appKey = "*";
        final String accessKeyId = "*";
        final String accessKeySecret = "*";
        String fileLink = "https://gw.alipayobjects.com/os/bmw-prod/0574ee2e-f494-45a5-820f-63aee583045a.wav";
        ASRUtil demo = new ASRUtil(accessKeyId, accessKeySecret);
        // 第一步：提交录音文件识别请求，获取任务ID用于后续的识别结果轮询。
        String taskId = demo.submitFileTransRequest(appKey, fileLink);
        if (taskId != null) {
            System.out.println("录音文件识别请求成功，task_id: " + taskId);
        } else {
            System.out.println("录音文件识别请求失败！");
            return;
        }
        // 第二步：根据任务ID轮询识别结果。
        String result = demo.getFileTransResult(taskId);
        if (result != null) {
            System.out.println("录音文件识别结果查询成功：" + result);
        } else {
            System.out.println("录音文件识别结果查询失败！");
        }
    }
}
