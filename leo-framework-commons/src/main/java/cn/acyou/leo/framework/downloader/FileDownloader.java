package cn.acyou.leo.framework.downloader;

import cn.acyou.leo.framework.downloader.ext.FileResponseExtractor;
import cn.acyou.leo.framework.downloader.support.DownloadProgressPrinter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.File;
import java.io.IOException;

public class FileDownloader extends AbstractDownloader {

    public FileDownloader(DownloadProgressPrinter downloadProgressPrinter) {
        super(downloadProgressPrinter);
    }

    public FileDownloader() {
        super(DownloadProgressPrinter.defaultDownloadProgressPrinter());
    }

    @Override
    protected void doDownload(String fileURL, String dir, String fileName, HttpHeaders headers) throws IOException {
        String filePath = dir + File.separator + fileName;
        FileResponseExtractor extractor = new FileResponseExtractor(filePath + ".download", downloadProgressPrinter); //创建临时下载文件
        File tmpFile = restTemplate.execute(fileURL, HttpMethod.GET, null, extractor);
        tmpFile.renameTo(new File(filePath)); //修改临时下载文件名称
    }

}
