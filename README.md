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
4. 整合 [PageHelper件](https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md) 分页，一行代码搞定分页
```
    @PostMapping(value = "pageSo")
    @ApiOperation("测试分页")
    public Result<PageData<Student>> pageSo(@RequestBody StudentSo studentSo) {
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
7. 参数校验，使用JSR303规范

      示例一：`@RequestBody`实体中
      ``` java
      @Data
      @PropertyScriptAssert(script = "_this.password==_this.confirmation", message = "密码输入不一致！")
      public class StudentReq implements Serializable {
      @NotNull(message = "name 不能为空！")
      private String name;
   
          @NotNull(message = "interests 不能为空！")
          @Size(min = 1, message = "interests 取值不正确")
          private List<String> interests;
   
          @ListValue(values = {1,2,3}, message = "age 取值不正确")
          private Integer age;
   
          @ListValue(strValues = {"Y","N"}, message = "enable 取值不正确")
          private String enable;
   
          @Valid
          @NotNull(message = "studentScoreReq 不能为空！")
          private StudentScoreReq studentScoreReq;
   
          private String password;
   
          private String confirmation;
      }
      
       // Controler
       @RequestMapping(value = "valid/test1", method = {RequestMethod.POST})
       @ApiOperation("测试参数校验1")
       public Result<?> validTest1(@Validated @RequestBody StudentReq studentReq){
           System.out.println("NAME" + studentReq);
           return Result.success(studentReq);
       }
      ```

      示例二：简单的非空校验，通过`@RequestParam`注解（默认是非空）会走统一异常处理返回结果。
      ``` java
       @RequestMapping(value = "get", method = {RequestMethod.GET})
       @ApiOperation("get")
       public Result<?> get(@RequestParam Long id, @RequestParam(required = false) String name) {
           Student stu = studentService.getById(id);
           return Result.success(stu);
       }
      ```
   
      示例三：直接在Controller参数上的参数校验
      ``` java
       @GetMapping(value = "valid/test2")
       @ApiOperation("测试参数校验2")
       public Result<?> validTest2(@Validated @NotBlank(message = "name不能为空") String name){
           return Result.success();
       }
      ```

      示例四：参数校验分组
      ``` java
       @Data
       public class StudentReq2 implements Serializable {
           @NotNull(message = "id 不能为空！", groups = {Insert.class})
           @Null(message = "id 必须为空！", groups = {Update.class})
           private Long id;
           @NotBlank(message = "name 不能为空！")
           private String name;
       }
       //Controller
       @RequestMapping(value = "valid/test3", method = {RequestMethod.POST})
       @ApiOperation("测试参数校验2")
       public Result<?> validTest3(@Validated({Insert.class, Default.class}) @RequestBody StudentReq2 studentReq){
           return Result.success(studentReq);
       }
       @RequestMapping(value = "valid/test4", method = {RequestMethod.POST})
       @ApiOperation("测试参数校验2")
       public Result<?> validTest4(@Validated({Update.class, Default.class}) @RequestBody StudentReq2 studentReq){
           return Result.success(studentReq);
       }
      ```

9. ElasticSearch全文检索

   - [1.ElasticSearch介绍与相关工具安装](document/6.ElasticSearch/1.ElasticSearch介绍与相关工具安装.md)
   - [2.IK分词器](document/6.ElasticSearch/2.IK分词器.md)
   - [3.ElasticSearch基础语法](document/6.ElasticSearch/3.ElasticSearch基础语法.md)
   - [4.SpringBoot集成ES](document/6.ElasticSearch/4.SpringBoot集成ES.md)
   - [5.同步数据](document/6.ElasticSearch/5.同步数据.md)
   - [6.Logstash是什么](document/6.ElasticSearch/6.Logstash到底是干嘛的.md)
   - [Ext.ES字段类型详解](document/6.ElasticSearch/Ext.ES字段类型详解.md)


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
### 一、 SpringCache+Redis缓存
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
    //示例1：使用单个参数作为缓存KEY
    @Cacheable(value="leo:example:demo#100", key="#id")
    public Student getById(Long id) {
        Student student = new Student(id, RandomUtil.randomUserName(), 12, null);
        log.info("获取用户：" + student);
        return student;
    }
    //示例2：使用object作为缓存，会转为JSON字符串
    @Cacheable(value="leo:example:demo22#100", key="#jo + '-' + #jo2")
    public Student getById(JSONObject jo, JSONObject jo2) {
        Student student = new Student(jo.getLong("id"), RandomUtil.randomUserName(), 12, null);
        log.info("获取用户：" + student);
        return student;
    }
```
- [查看详细介绍](document/howtouse/SpringCache使用说明.md)

### 二、 全局ID生成器
1. 在项目中配置`SnowFlake`
```
    @Bean
    public SnowFlake snowFlake() {
        return new SnowFlake(1, 1);
    }
```
2. 使用示例：
```
//雪花算法ID
System.out.println(IdUtil.nextId());                  //631216897488195584
System.out.println(IdUtil.nextIdStr());               //631216897492389888
System.out.println(IdUtil.nextIdPrefix("RK"));        //RK631216897492389889
//UUID
System.out.println(IdUtil.uuidStr());                 //325b3cfd-b2fc-4e40-a2d5-5b08774f2ec8
System.out.println(IdUtil.uuidStrWithoutLine());      //63e1c93bbc4a4126a433e9f2f25aa545
//MongoDB ObjectId
System.out.println(IdUtil.objectId());                //6131e6cd89042fde6a230649
//基于Redis Increment 的递增序列ID
System.out.println(IdUtil.getDatePrefixId("RK"));     //RK2021090300003
System.out.println(IdUtil.getDatePrefixId("RK", 8));  //RK2021090300000004
```

### 三、防止重复提交

通过`@AccessLimit`注解，有两个参数：

1. interval： 访问间隔（毫秒）
2. includeArgs：包含参数（不同的参数访问间隔不一样）

示例：

```
    @RequestMapping(value = "get", method = {RequestMethod.GET})
    @ApiOperation("防止重复提交测试")
    @AccessLimit(interval = 5000, includeArgs = true)
    public Result<String> get(String name) {
        return Result.success(name);
    }
```

### 四、权限控制

没有使用`shiro`、`Spring Security`，自己通过Aspect实现。

1. `@RequiresRoles`：标识需要角色
2. `@RequiresPermissions`：标识需要指定权限

示例：

```
    @RequestMapping(value = "get", method = {RequestMethod.GET})
    @ApiOperation("获取一条记录")
    @RequiresRoles("auditor")
    @RequiresPermissions("student:get")
    public Result<?> get(Long id) {
        Student stu = studentService.getById(id);
        return Result.success(stu);
    }
```
> 思考： 为什么要使用`shiro`、`Spring Security`？？？我一个切面就能搞定的事情。ி

## 项目使用定制化配置

``` properties
leo:
  debug:
    # 打印错误堆栈到Result中
    print-to-result: false
    # 是否开启接口统计分析功能
    interface-call-statistics: false
    # 统计分析功能忽略的路径
    ignore-uri-list:
      - /student/get
xss:
  # 开启XSS过滤
  enabled: true
  # XSS过滤排除路径
  excludes: 
```

## 参与贡献

无需贡献

