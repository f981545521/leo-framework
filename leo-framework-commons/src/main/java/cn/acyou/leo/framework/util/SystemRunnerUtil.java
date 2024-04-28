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
public class SystemRunnerUtil {
    public static void main(String[] args) {
        execCommand("ipconfig", System.out::println);
        //String batFilePath = "D:\\workspace\\work\\auto-commit.bat";
        //execCommand(batFilePath, System.out::println);
        //exec(batFilePath, System.out::println);
    }

    public static void execCommand(String command, Consumer<String> produce) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                produce.accept(line);
            }
            int exitCode = process.waitFor();
            log.info("脚本 [{}] 执行结束：{}", command, exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exec(String runFilePath, Consumer<String> produce) {
        try {
            ProcessBuilder pb = new ProcessBuilder(runFilePath);
            Process process = pb.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                produce.accept(line);
            }
            int exitCode = process.waitFor();
            log.info("脚本 [{}] 执行结束：{}", runFilePath, exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
