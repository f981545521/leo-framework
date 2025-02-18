package cn.acyou.leo.order.web.tests.es;

import cn.acyou.leo.framework.commons.PageQuery;
import cn.acyou.leo.framework.commons.SnowFlake;
import cn.acyou.leo.framework.constant.Constant;
import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.framework.util.RandomUtil;
import cn.acyou.leo.order.web.base.ApplicationBaseTests;
import cn.acyou.leo.product.entity.Product;
import cn.acyou.leo.product.es.ProductEsRepository;
import cn.acyou.leo.product.es.eo.ProductEo;
import cn.acyou.leo.product.mapper.ProductMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author youfang
 * @version [1.0.0, 2021-8-7]
 **/
public class ElasticSearchTests extends ApplicationBaseTests {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private ProductEsRepository productEsRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private SnowFlake snowFlake;

    @Test
    public void deleteIndex() {
        //elasticsearchRestTemplate.deleteIndex("product");
        elasticsearchRestTemplate.indexOps(Product.class).delete();
    }

    @Test
    public void save() {
        ProductEo product = new ProductEo();
        product.setId(snowFlake.nextId());
        product.setStatus(Constant.NORMAL);
        product.setStockNumber(RandomUtil.randomRangeNumber(100, 100000));
        product.setProductName("电风扇");
        productEsRepository.save(product);
        System.out.println("OK");
    }
    @Test
    public void save2() {
        ProductEo product = new ProductEo();
        product.setId(174L);
        product.setStatus(Constant.NORMAL);
        product.setStockNumber(RandomUtil.randomRangeNumber(100, 100000));
        product.setProductName("CEA抗体试剂(免疫组织化学)");
        productEsRepository.save(product);
        System.out.println("OK");
    }

    @Test
    @Commit
    public void saveAll() {
        List<Product> productList = productMapper.selectList(null);
        List<ProductEo> collect = productList.stream()
                .map(x -> new ProductEo(x.getProductId(), x.getProductName(), x.getStatus(), x.getStockNumber()))
                .collect(Collectors.toList());
        productEsRepository.saveAll(collect);
        System.out.println("批量保存成功！OK");
    }

    @Test
    public void findById(){
        Optional<ProductEo> productEo = productEsRepository.findById(174L);
        Iterable<ProductEo> productEos = productEsRepository.findAllById(Lists.newArrayList(120L, 121L));
        System.out.println("OK");
        for (ProductEo eo : productEos) {
            System.out.println("查询多个：" + eo);
        }
    }

    @Test
    public void testPageQuery1(){
        Page<ProductEo> users = productEsRepository.findAll(PageRequest.of(1, 20));
        PageData<ProductEo> pageData = PageQuery.convert(users);
        System.out.println(pageData);
    }
    @Test
    public void testPageQuery2(){
        Sort sort = Sort.by("stockNumber").descending();
        PageRequest pageRequest = PageRequest.of(1, 20, sort);
        Page<ProductEo> pages = productEsRepository.findByProductName("组织", pageRequest);
        PageData<ProductEo> pageData = PageQuery.convert(pages);
        System.out.println(pageData);
    }
    @Test
    public void testPageQuery3(){
        Sort sort = Sort.by("stockNumber").descending();
        //注意：从0开始！！
        PageRequest pageRequest = PageRequest.of(0, 20, sort);
        Page<ProductEo> pages = productEsRepository.findAll(pageRequest);
        PageData<ProductEo> pageData = PageQuery.convert(pages);
        System.out.println(pageData);
    }

    @Test
    public void testPageQuery4(){
        Query query = Query.findAll();
        elasticsearchRestTemplate.multiGet(query, ProductEo.class);
    }

    @Test
    public void testDelete(){
        productEsRepository.deleteAll();
    }


}
