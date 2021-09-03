# ♌leo-framework

[![Maven Central](https://img.shields.io/maven-central/v/cn.acyou/leo-framework.svg?color=2d545e)](https://search.maven.org/search?q=g:cn.acyou)
[![JDK support](https://img.shields.io/badge/JDK-1.8-green.svg)](https://gitee.com/f981545521/leo-framework)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

## 介绍
SpringCloud项目

## 软件架构
#### 核心组件：
1. Nacos作为注册中心、配置中心
2. Apache Dubbo作为服务调用（RPC）框架
3. Seata 解决分布式事务
4. 网关使用`Spring Cloud Gateway`，集成`Sentinel`熔断限流
5. 使用`RocketMQ`消息队列
6. 使用`Spring Cloud Sleuth` + `ZipKin` 的链路追踪
7. 使用`ElasticSearch`全文搜索

#### 微服务：
1. SpringBoot+SpringMvc+Mybatis
2. Redis
3. Mysql
4. [PageHelper分页插件](https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md) 一行代码搞定分页
```
    @PostMapping(value = "pageSo")
    @ApiOperation("测试分页")
    public Result<PageData<Student>> pageSo(@ParamValid @RequestBody StudentSo studentSo) {
        PageData<Student> convertType =  
                PageQuery.startPage(studentSo)
                .selectMapper(studentService.selectByProperties("name", studentSo.getName(), "age", studentSo.getAge()));
        return Result.success(convertType);
    }
```
5. [通用Mapper省去单表增删改查](https://gitee.com/free/Mapper/wikis/Home)
    - [通用Service](https://gitee.com/f981545521/leo-framework/blob/master/leo-framework-commons/src/main/java/cn/acyou/leo/framework/service/Service.java)
    - [通用Mapper](https://gitee.com/f981545521/leo-framework/blob/master/leo-framework-commons/src/main/java/cn/acyou/leo/framework/mapper/Mapper.java)
    - 支持乐观锁、逻辑删除....
6. Mybatis拦截器，继承`BaseEntity`，不用set createTime、createUser、updateUser、updateTime
7. 增强的参数校验
    - [@EnhanceValid](https://gitee.com/f981545521/leo-framework/blob/master/leo-framework-dto/src/main/java/cn/acyou/leo/framework/annotation/valid/EnhanceValid.java) 
    - [@BaseValid](https://gitee.com/f981545521/leo-framework/blob/master/leo-framework-dto/src/main/java/cn/acyou/leo/framework/annotation/valid/BaseValid.java)
``` java
@Data
@EqualsAndHashCode(callSuper = true)
public class StudentSo extends PageSo {

    @BaseValid(notEmpty = true, message = "姓名不能为空！")
    private String	name;

    @EnhanceValid({
            @BaseValid(notNull = true, message = "年龄不能为空！"),
            @BaseValid(min = 0, message = "年龄不能小于0岁！"),
            @BaseValid(max = 200, message = "年龄不能大于200岁！")
    })
    private Integer age;
}
```
8. ElasticSearch全文检索


## 安装教程
为了顺利启动项目，一步一步运行好环境。

为了快速运行此项目所需的文件，提供相关运行环境文件的下载。请移步：[下载地址](http://dev.acyou.cn)

### 必须
#### 1. 无需配置。直接启动Nacos（必须）
```
执行：`PS D:\developer\cloud\nacos\bin> .\startup.cmd -m standalone`

安装完成后地址
打开：`http://127.0.0.1:8848/nacos/index.html`
默认用户名：nacos/nacos
```
#### 2. 启动Seata Server （必须）

第一步： 修改seata配置文件适用Nacos注册中心与配置中心：

[E:\cloud\seata-server-1.4.2\conf\registry.conf](document/3.Seata/registry.conf)

第二步： 导入配置：[==配置文件==](document/1.Nacos/conf) 下面的所有文件
> 包含微服务配置与Seata配置
```
配置完成后，
双击打开：
D:\developer\cloud\seata-server-1.4.2\bin\seata-server.bat
```
#### 3. 安装与运行RocketMQ(推荐，看工程情况)

[参考文档](document/5.RocketMQ/2.RocketMq.md)

#### 4. 安装与运行ElasticSearch(推荐，看工程情况)

[参考文档](document/6.ElasticSearch/1.ElasticSearch介绍与相关工具安装.md)

#### 5. 如果使用网关（需要打开Sentinel）
```
## 运行网关
D:\developer\cloud>java -jar leo-gateway-1.0.0.RELEASE.jar
## 运行Sentinel 
java -Dserver.port=9100 -Dcsp.sentinel.app.type=1 -Dcsp.sentinel.dashboard.server=localhost:9100 -Dproject.name=sentinel-console -jar ./sentinel-dashboard-1.7.2.jar

完成后打开：http://localhost:9100/#/dashboard
默认用户名：sentinel/sentinel
```
#### 6. 链路追踪（根据情况Enable）

```
bootstrap.yml
spring:
  sleuth:
    enabled: true
    rpc:
      enabled: true
  zipkin:
    enabled: true
    base-url: http://localhost:9411/

运行ZipKinServer
D:\developer\cloud\zipkin>java -jar zipkin-server-2.23.2-exec.jar

完成后打开：http://localhost:9411/zipkin/

```

### 可选
3. 启动Dubbo Admin（可选）
```
java -jar dubbo-admin-0.3.0-SNAPSHOT.jar

http://localhost:8080/#/login
默认用户名：root/root
```

已经修改配置文件打的包。直接运行
```
# nacos config, add parameters to url like username=nacos&password=nacos
admin.registry.address=nacos://127.0.0.1:8848?group=DEFAULT_GROUP&namespace=public&username=nacos&password=nacos
admin.config-center=nacos://127.0.0.1:8848?group=dubbo&username=nacos&password=nacos
admin.metadata-report.address=nacos://127.0.0.1:8848?group=dubbo&username=nacos&password=nacos
```

## 测试
```
### 产品列表
GET http://localhost:8056/product/page?pageNum=1&pageSize=10

### 订单列表
GET http://localhost:8055/order/page?pageNum=1&pageSize=10

### 网关
GET http://localhost:12000/product/product/page?pageNum=1&pageSize=10

GET http://localhost:12000/order/order/page?pageNum=1&pageSize=10

### Dubbo服务调用
// 正常
POST http://localhost:12000/order/order/insert?productId=2
Content-Type: application/json

//异常
POST http://localhost:12000/order/order/insert?productId=1
Content-Type: application/json

```


## 使用说明

- [微服务使用教程](document/howtouse/微服务使用教程.md)
- [项目功能说明](document/howtouse/项目功能说明.md)
- [模板项目-SpringBoot](https://gitee.com/f981545521/leo-user)
- [模板项目-SpringCloud](https://gitee.com/f981545521/leo-product)

## 使用技巧
### 一. SpringCache+Redis缓存
1. 配置缓存管理器
```
    /**
     * RedisCacheManager
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        //缓存默认有效期 24小时
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(24));
        //配置序列化
        RedisCacheConfiguration redisCacheConfiguration = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        //构建缓存管理器
        //return RedisCacheManager.builder(redisCacheWriter).cacheDefaults(redisCacheConfiguration).transactionAware().build();
        //自定义缓存管理器， 支持自定义过期时间：@Cacheable(value="sys:student#100", key="#id") 100S过期
        //                                     @Cacheable(value="sys:student#-1", key="#id")  永不过期
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory);
        return new MyRedisCacheManager(redisCacheWriter, redisCacheConfiguration);
    }
```
2. 方法中通过`@Cacheable`注解使用
```
    @Cacheable(value="leo:example:demo#100", key="#id")
    public Student getById(Long id) {
        Student student = new Student(id, RandomUtil.randomUserName(), 12, null);
        log.info("获取用户：" + student);
        return student;
    }
```
- [查看详细介绍](document/howtouse/SpringCache使用说明.md)

## 参与贡献

无需贡献

