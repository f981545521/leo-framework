package cn.acyou.leo.framework.listener;

/**
 * @author fangyou
 * @version [1.0.0, 2021-09-01 15:30]
 */
public interface ProgressCallback {
    /**
     * 进度回调 0~100
     *
     * @param percent 百分比
     */
    void progress(int percent);

    /**
     * 成功回调
     */
    void success();
}
