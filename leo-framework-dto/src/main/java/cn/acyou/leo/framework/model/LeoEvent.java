package cn.acyou.leo.framework.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author youfang
 * @version [1.0.0, 2025/5/23 17:55]
 **/
@Data
@AllArgsConstructor
public class LeoEvent<T> {
    private T data;
}
