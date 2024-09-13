package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.commons.PageQuery;
import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.framework.util.SpringHelper;
import cn.acyou.leo.tool.entity.ParamConfig;
import cn.acyou.leo.tool.entity.TOrder;
import cn.acyou.leo.tool.service.ParamConfigService;
import cn.acyou.leo.tool.service.TOrderService;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2022/5/10 14:14]
 **/
public class ShardingTestCase extends ApplicationBaseTests {
    @Autowired
    private TOrderService orderService;
    @Autowired
    private ParamConfigService paramConfigService;

    @Test
    public void initTables() throws Exception{
        Map<String, DataSource> dataSourceMap = SpringHelper.getBean(ShardingDataSource.class).getDataSourceMap();
        for (DataSource ds : dataSourceMap.values()) {
            String sql1 = "CREATE TABLE IF NOT EXISTS t_order_0 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id BIGINT NOT NULL, address_id BIGINT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));";
            String sql2 = "CREATE TABLE IF NOT EXISTS t_order_1 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id BIGINT NOT NULL, address_id BIGINT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));";
            try (Connection connection = ds.getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql1);
                statement.executeUpdate(sql2);
            }
        }
        System.out.println("ok");
    }

    @Test
    public void testInsert() {
        List<TOrder> orderList = new ArrayList<>();
        for (int i = 100; i < 2000; i++) {
            TOrder order = new TOrder();
            order.setUserId((long) i);
            order.setAddressId(0L);
            order.setStatus("ok");
            orderList.add(order);
            if (i == 100) {
                for (int j = 0; j < 100; j++) {
                    order = new TOrder();
                    order.setUserId((long) i);
                    order.setAddressId(0L);
                    order.setStatus("ok");
                    orderList.add(order);
                }
            }
        }
        orderService.saveBatch(orderList);
        System.out.println("ok");
    }

    @Test
    public void testSelect() {
        TOrder byId1 = orderService.getById(1834501596359180329L);
        TOrder byId2 = orderService.getById(1834501594165559299L);
        System.out.println(byId1);
        System.out.println(byId2);
    }

    @Test
    public void testSelect2() {
        PageData<TOrder> tOrderPageData = PageQuery.startPage(1, 10)
                .selectMapper(
                        orderService.lambdaQuery()
                                .ge(TOrder::getUserId, 1000)
                                .orderByDesc(TOrder::getUserId)
                                .list()
                );
        System.out.println(tOrderPageData);
    }
    @Test
    public void testSelect3() {
        List<TOrder> list = orderService.lambdaQuery()
                .ge(TOrder::getUserId, 1000L)
                .orderByDesc(TOrder::getUserId)
                .list();
        System.out.println(list);
    }

    @Test
    public void testinsert2() {
        ParamConfig paramConfig = new ParamConfig();
        paramConfig.setNamespace("icon");
        paramConfig.setCode("抖音");
        paramConfig.setValue("https://vshow.guiji.ai/nfs/tici/icon/douyin.png");
        paramConfig.setDescription("转换链接图标");
        paramConfigService.save(paramConfig);
    }

}
