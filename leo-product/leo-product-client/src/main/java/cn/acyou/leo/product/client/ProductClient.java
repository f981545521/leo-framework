package cn.acyou.leo.product.client;

import cn.acyou.leo.product.dto.vo.ProductVo;


/**
 * @author fangyou
 * @version [1.0.0, 2021-08-04 18:16]
 */
public interface ProductClient {

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
