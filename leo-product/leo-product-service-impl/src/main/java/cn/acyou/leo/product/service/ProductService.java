package cn.acyou.leo.product.service;

import cn.acyou.leo.product.dto.vo.ProductVo;
import cn.acyou.leo.product.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author youfang
 * @version [1.0.0, 2020-4-17 下午 08:55]
 **/
public interface ProductService extends IService<Product> {

    /**
     * 插入产品
     * @param productName 产品名称
     */
    void insertProduct(String productName);


    /**
     * 通过id获取产品
     *
     * @param productId 产品id
     * @return {@link ProductVo}
     */
    ProductVo getProductById(Long productId);


    /**
     * 出库
     *
     * @param productId 产品id
     * @param number    数量
     */
    void stockOut(Long productId, Integer number);

}
