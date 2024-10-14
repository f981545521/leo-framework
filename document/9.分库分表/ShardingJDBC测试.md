
## 集成测试
### 第一步：初始化数据
```sql
-- 数据库1
CREATE DATABASE IF NOT EXISTS `scorpio` DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_bin;
use scorpio;
CREATE TABLE IF NOT EXISTS t_order_0 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id BIGINT NOT NULL, address_id BIGINT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
CREATE TABLE IF NOT EXISTS t_order_1 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id BIGINT NOT NULL, address_id BIGINT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
-- 数据库2
CREATE DATABASE IF NOT EXISTS `scorpio2` DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_bin;
use scorpio2;
CREATE TABLE IF NOT EXISTS t_order_0 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id BIGINT NOT NULL, address_id BIGINT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
CREATE TABLE IF NOT EXISTS t_order_1 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id BIGINT NOT NULL, address_id BIGINT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));

```

### 第二步：yml配置规则

```yml
spring:
  shardingsphere:
    datasource:
      names: ds0,ds1
      ds0:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3306/scorpio?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
        username: root
        password: root123
      ds1:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3306/scorpio2?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
        username: root
        password: root123
    sharding:
      default-data-source-name: ds0
      default-database-strategy:
        standard:
          shardingColumn: user_id
          preciseAlgorithmClassName: cn.acyou.leo.tool.sharding.algorithm.PreciseModuloShardingDatabaseAlgorithm
          rangeAlgorithmClassName: cn.acyou.leo.tool.sharding.algorithm.RangeModuloShardingDatabaseAlgorithm
        #inline: #不支持范围查询
        #  sharding-column: user_id
        #  algorithm-expression: ds$->{user_id % 2}
      binding-tables: t_order # 绑定表 • 绑定表适用于具有相同结构和访问模式的表
      broadcast-tables: sys_param_config # 广播表 • 在分库分表以后，比如数据库1有配置表，数据库2有个配置表，数据库3也有个配置表，需要我们把配置同时写入三个数据库的配置表中， 在这样的情况下这里的配置表就是一种广播表，要求就是数据同步和一致。
      tables:
        t_order:
          actual-data-nodes: ds$->{0..1}.t_order_$->{0..1} # 分表： t_order_0 跟 t_order_1
          table-strategy:
            standard:
              shardingColumn: order_id
              preciseAlgorithmClassName: cn.acyou.leo.tool.sharding.algorithm.PreciseModuloShardingTableAlgorithm
              rangeAlgorithmClassName: cn.acyou.leo.tool.sharding.algorithm.RangeModuloShardingTableAlgorithm
            #inline:
            #  sharding-column: order_id # 按照order_id%2分别入t_order_0/t_order_1表
            #  algorithm-expression: t_order_$->{order_id % 2}
          # 主键生成策略（如果是自动生成的，在插入数据的sql中就不要传id，null也不行，直接插入字段中就不要有主键的字段）
          key-generator:
            column: order_id # order_id自动生成
            type: SNOWFLAKE
            props:
              worker:
                id: 1
```

### 第三步：测试用例
```java
package cn.acyou.leo.tool.test;

import cn.acyou.leo.framework.commons.PageQuery;
import cn.acyou.leo.framework.model.PageData;
import cn.acyou.leo.tool.entity.ParamConfig;
import cn.acyou.leo.tool.entity.TOrder;
import cn.acyou.leo.tool.service.ParamConfigService;
import cn.acyou.leo.tool.service.TOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2022/5/10 14:14]
 **/
public class Test3 extends ApplicationBaseTests {
    @Autowired
    private TOrderService orderService;
    @Autowired
    private ParamConfigService paramConfigService;

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

```