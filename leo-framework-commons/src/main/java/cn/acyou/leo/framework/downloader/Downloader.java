package cn.acyou.leo.framework.downloader;

import java.io.IOException;

public interface Downloader {

    void download(String fileURL, String dir) throws IOException;

}
