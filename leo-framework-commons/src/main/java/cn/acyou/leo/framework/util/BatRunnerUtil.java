package cn.acyou.leo.framework.util;

/**
 * @author youfang
 * @version [1.0.0, 2024/4/28 15:42]
 **/
public class BatRunnerUtil {
    public static void main(String[] args) {
        try {
            // 替换为你的批处理文件路径
            String batFilePath = "D:\\workspace\\work\\auto-commit.bat";
            ProcessBuilder pb = new ProcessBuilder(batFilePath);
            Process process = pb.start();
            process.waitFor(); // 等待批处理脚本执行完成
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
