package cn.acyou.leo.product.service.impl;

import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.exception.ServiceException;
import cn.acyou.leo.framework.util.BeanCopyUtil;
import cn.acyou.leo.framework.util.RandomUtil;
import cn.acyou.leo.product.client.ProductClient;
import cn.acyou.leo.product.dto.vo.ProductVo;
import cn.acyou.leo.product.entity.Product;
import cn.acyou.leo.product.mapper.ProductMapper;
import cn.acyou.leo.product.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * @author youfang
 * @version [1.0.0, 2020-4-17 下午 08:56]
 **/
@Slf4j
@Service
@DubboService(interfaceClass = ProductClient.class)
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService, ProductClient {
    @Override
    public ProductVo getProductById(Long productId) {
        Product product = getById(productId);
        return BeanCopyUtil.copy(product, ProductVo.class);
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void stockOut(Long productId, Integer number) {
        Product product = getById(productId);
        if (product.getStockNumber() < number) {
            throw new ServiceException("库存不足无法出库！");
        }
        product.setStockNumber(product.getStockNumber() - number);
        updateById(product);
    }

    @Override
    public void insertProduct(String productName) {
        Product product = new Product();
        product.setProductName(productName);
        product.setStockNumber(RandomUtil.randomRangeNumber(100, 10000));
        product.setStatus(Constant.NORMAL);
        baseMapper.insert(product);
        log.info("插入成功！");
        if (product.getProductName().contains("-")){
            throw new ServiceException("物品名称不能包含 “-” ！");
        }
    }
}
