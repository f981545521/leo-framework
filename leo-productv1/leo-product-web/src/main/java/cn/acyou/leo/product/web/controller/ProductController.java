package cn.acyou.leo.product.web.controller;

import cn.acyou.leo.framework.commons.PageQuery;
import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.framework.model.Result;
import cn.acyou.leo.product.entity.Product;
import cn.acyou.leo.product.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author fangyou
 * @version [1.0.0, 2021-08-04 18:03]
 */
@Slf4j
@RestController
@RequestMapping("/product")
@Api(value = "商品", tags = "商品接口")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping(value = "page")
    @ApiOperation("测试分页")
    public Result<PageData<Product>> page(Integer pageNum, Integer pageSize) {
        PageData<Product> convertType = PageQuery.startPage(pageNum, pageSize).selectMapper(productService.list());
        return Result.success(convertType);
    }

    @PostMapping(value = "insert")
    @ApiOperation("插入商品")
    public Result<Void> insert(@RequestBody String productName) {
        productService.insertProduct(productName);
        return Result.success();
    }

}
