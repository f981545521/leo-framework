package cn.acyou.leo.tool.test;

import cn.acyou.leo.tool.entity.Order1;
import cn.acyou.leo.tool.mapper.Order1Mapper;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2022/5/10 11:04]
 **/
@SpringBootTest
public class ApplicationBaseTests {
    @Autowired
    private Order1Mapper order1Mapper;

    @Test
    public void test_save(){
        Order1 order1 = new Order1();
        order1.setAddressId(100L);
        order1.setStatus("success");
        order1.setUserId(2003);
        int save = order1Mapper.save(order1);
        System.out.println(save);
    }

    @Test
    public void test(){
        List<Order1> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Order1 order1 = new Order1();
            order1.setAddressId(100L);
            order1.setStatus("success");
            order1.setUserId(2003);
            list.add(order1);
        }
        int save = order1Mapper.saveBatch(list);
        System.out.println(save);
    }

    @Test
    public void test_removeById(){
        int save = order1Mapper.removeById(1L);
        System.out.println(save);
    }
    @Test
    public void test_removeByIds(){
        int save = order1Mapper.removeByIds(Lists.newArrayList(2,3,4));
        System.out.println(save);
    }
    @Test
    public void test_updateById(){
        Order1 order1 = new Order1();
        order1.setOrderId(5L);
        order1.setAddressId(5L);
        order1.setAddressId(100L);
        order1.setStatus("update");
        int save = order1Mapper.updateById(order1);
        System.out.println(save);
    }

    @Test
    public void test_get(){
        Order1 byId = order1Mapper.getById(6);
        System.out.println(byId);
    }

    @Test
    public void test_getBatch_updateBatch(){
        List<Order1> byId = order1Mapper.listByIds(Lists.newArrayList(7,8));
        System.out.println(byId);
        for (Order1 order1 : byId) {
            order1.setStatus("batch_update");
            order1.setAddressId(666L);
        }
        order1Mapper.updateBatchById(byId);
    }



}
