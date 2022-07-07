package cn.acyou.leo.framework.util.function;

/**
 * 任务(无返回值)
 *
 * @author youfang
 * @version [1.0.0, 2022/7/7 17:54]
 **/
public interface Task {
    void run() throws RuntimeException;
}
