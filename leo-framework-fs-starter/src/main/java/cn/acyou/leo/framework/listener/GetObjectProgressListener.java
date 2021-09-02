package cn.acyou.leo.framework.listener;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;

/**
 * @author fangyou
 * @version [1.0.0, 2021-09-01 15:24]
 */
public class GetObjectProgressListener implements ProgressListener {
    private long bytesRead = 0;
    private long totalBytes = -1;
    private boolean succeed = false;
    private final ProgressCallback callback;

    public GetObjectProgressListener(ProgressCallback process) {
        this.callback = process;
    }
    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        switch (eventType) {
            case TRANSFER_STARTED_EVENT:
                //开始下载事件
                break;
            case RESPONSE_CONTENT_LENGTH_EVENT:
                //获取内容总字节数
                this.totalBytes = bytes;
                break;
            case RESPONSE_BYTE_TRANSFER_EVENT:
                //传输过程中
                this.bytesRead += bytes;
                if (this.totalBytes != -1) {
                    int percent = (int) (this.bytesRead * 100.0 / this.totalBytes);
                    callback.progress(percent);
                }
                //else: 没有明确的总字节数
                break;
            case TRANSFER_COMPLETED_EVENT:
                //传输完成
                this.succeed = true;
                callback.success();
                break;
            case TRANSFER_FAILED_EVENT:
                //传输失败
                break;
            default:
                break;
        }
    }

    public boolean isSucceed() {
        return succeed;
    }
}