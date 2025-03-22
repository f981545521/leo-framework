package cn.acyou.leo.tool.dto;


import lombok.Data;

/**
 * @author youfang
 * @version [1.0.0, 2025/3/22]
 **/
@Data
public class TaskState {

    public final static State state = new State();

    @Data
    public static class State {
        private boolean running;

        private Object info;

    }
}
