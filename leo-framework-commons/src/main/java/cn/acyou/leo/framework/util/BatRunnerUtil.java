package cn.acyou.leo.framework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.Consumer;

/**
 * @author youfang
 * @version [1.0.0, 2024/4/28 15:42]
 **/
@Slf4j
public class BatRunnerUtil {
    public static void main(String[] args) {
        String batFilePath = "D:\\workspace\\work\\auto-commit.bat";
        exec(batFilePath, s ->{
            System.out.println(s);
        });
    }

    public static void exec(String runFilePath, Consumer<String> produce) {
        try {
            ProcessBuilder pb = new ProcessBuilder(runFilePath);
            Process process = pb.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                produce.accept(line);
            }
            int exitCode = process.waitFor();// 等待批处理脚本执行完成
            log.info("脚本 [{}] 执行结束：{}", runFilePath, exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
