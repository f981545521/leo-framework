package cn.acyou.leo.product.es;

import cn.acyou.leo.product.es.eo.ProductEo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author youfang
 * @version [1.0.0, 2021-8-7]
 **/
@Repository
public interface ProductEsRepository extends ElasticsearchRepository<ProductEo, Long> {

    Page<ProductEo> findByProductName(String productName, Pageable pageable);

    @Query("{\"match\": {\"productName\": {\"query\": \"?0\"}}}")
    Page<ProductEo> selectProduct(String name, Pageable pageable);

}
