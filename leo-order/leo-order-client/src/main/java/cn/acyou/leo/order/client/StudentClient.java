package cn.acyou.leo.order.client;

import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.order.dto.so.StudentSo;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-04 8:48]
 */
public interface StudentClient {

    /**
     * 说“你好”
     *
     * @param name 的名字
     * @return {@link String}
     */
    String sayHello(String name);

    /**
     * 分页查询列表
     * @param studentSo
     * @return {@link PageData }<{@link ? }>
     */
    PageData<?> page(StudentSo studentSo);
}
