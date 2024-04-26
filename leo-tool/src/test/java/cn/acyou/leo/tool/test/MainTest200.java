package cn.acyou.leo.tool.test;


import cn.acyou.leo.framework.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author youfang
 * @version [1.0.0, 2024/4/26 9:14]
 **/
@Slf4j
public class MainTest200 {

    public static void main(String[] args) {
        File path = new File("D:\\TranslateVideos");
        File indexPath = new File("D:\\TranslateVideos_index");
        copyIndexName(path, indexPath);
    }

    private static void copyIndexName(File dir, File targetDir){
        FileUtil.listFiles(dir, f ->{
            File newPathFile = new File(f.getAbsolutePath().replace(dir.getAbsolutePath(), targetDir.getAbsolutePath()));
            if (!f.isHidden()) {
                if (f.isDirectory()) {
                    FileUtil.mkdir(newPathFile);
                    copyIndexName(f, newPathFile);
                }
                if (f.isFile()) {
                    FileUtil.touch(newPathFile + ".txt");
                }
            }
        });
    }

}
