package cn.acyou.leo.order.service;

import cn.acyou.leo.order.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author youfang
 * @version [1.0.0, 2020-4-17 下午 08:55]
 **/
public interface OrderService extends IService<Order> {

    /**
     * 创建订单
     *
     * @param productId 产品id
     */
    void insertOrder(Long productId);
}
