##  ShardingSphere
Apache ShardingSphere 是一款分布式的数据库生态系统， 可以将任意数据库转换为分布式数据库，并通过数据分片、弹性伸缩、加密等能力对原有数据库进行增强。

Apache ShardingSphere 设计哲学为 Database Plus，旨在构建异构数据库上层的标准和生态。 它关注如何充分合理地利用数据库的计算和存储能力，而并非实现一个全新的数据库。 它站在数据库的上层视角，关注它们之间的协作多于数据库自身。


### ShardingSphere-JDBC
使用限制：

https://shardingsphere.apache.org/document/current/cn/features/sharding/limitation/

## 集成
### 第一步：Maven引入starter
```xml
        <dependency>
            <groupId>org.apache.shardingsphere</groupId>
            <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
            <version>4.1.1</version>
        </dependency>
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