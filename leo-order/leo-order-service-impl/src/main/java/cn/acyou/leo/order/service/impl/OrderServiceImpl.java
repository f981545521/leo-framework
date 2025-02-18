package cn.acyou.leo.order.service.impl;

import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.util.RandomUtil;
import cn.acyou.leo.order.entity.Order;
import cn.acyou.leo.order.mapper.OrderMapper;
import cn.acyou.leo.order.service.OrderService;
import cn.acyou.leo.product.client.ProductClient;
import cn.acyou.leo.product.dto.vo.ProductVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * @author youfang
 * @version [1.0.0, 2020-4-17 下午 08:56]
 **/
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @DubboReference(check = false)
    private ProductClient productClient;

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void insertOrder(Long productId) {
        int i = RandomUtil.randomRangeNumber(1, 20);
        ProductVo productVo = productClient.getProductById(productId);
        log.info("准备下单商品：{} 数量： {}", productVo.getProductName(), i);
        productClient.stockOut(productId, i);


        Order order = new Order();
        order.setProductId(productId);
        order.setStatus(Constant.NORMAL);
        order.setOrderName("订单：" + RandomUtil.randomUserName());
        log.info("准备创建订单...");
        if (productId == 1) {
            throw new ServiceException("1号商品还不支持下单！");
        }
        save(order);
        log.info("下单成功！");
    }
}
