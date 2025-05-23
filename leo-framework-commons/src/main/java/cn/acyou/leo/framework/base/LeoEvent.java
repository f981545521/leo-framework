package cn.acyou.leo.framework.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

/**
 * @author youfang
 * @version [1.0.0, 2025/5/23 17:55]
 **/
@Data
@AllArgsConstructor
public class LeoEvent<T> implements ResolvableTypeProvider {
    /**
     * 事件内容
     */
    private T data;


    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forClass(data.getClass()));
    }

}
