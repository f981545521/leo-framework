package cn.acyou.leo.order.web.controller;

import cn.acyou.leo.framework.commons.PageQuery;
import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.order.entity.Order;
import cn.acyou.leo.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fangyou
 * @version [1.0.0, 2021/7/29]
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Api(value = "订单", tags = "订单接口")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping(value = "page")
    @ApiOperation("测试分页")
    public Result<PageData<Order>> page(Integer pageNum, Integer pageSize) {
        PageData<Order> convertType = PageQuery.startPage(pageNum, pageSize).selectMapper(orderService.list());
        return Result.success(convertType);
    }

    @PostMapping(value = "insert")
    @ApiOperation("插入订单")
    public Result<Void> insert(Long productId) {
        orderService.insertOrder(productId);
        return Result.success();
    }

}
