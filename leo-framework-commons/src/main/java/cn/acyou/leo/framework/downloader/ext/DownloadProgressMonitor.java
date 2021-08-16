package cn.acyou.leo.framework.downloader.ext;

public interface DownloadProgressMonitor {

    /**
     * 计算下载进度
     *
     * @param contentLength 文件总大小
     */
    void calculateDownloadProgress(long contentLength);

    /**
     * 返回已下载的字节数
     *
     * @return 已经下载的文件大小
     */
    long getAlreadyDownloadLength();

}
