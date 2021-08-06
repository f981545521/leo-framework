# leo-framework

## 介绍
SpringCloud项目

## 软件架构
1. Nacos作为注册中心、配置中心
2. Apache Dubbo作为服务调用（RPC）框架
3. 


## 安装教程
为了顺利启动项目，一步一步运行好环境。

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
#### 3. 运行RocketMQ(推荐，看工程情况)

[参考文档](https://gitee.com/f981545521/scorpio/blob/master/document/notes/mq/2.RocketMq.md)

#### 4. 如果使用网关（需要打开Sentinel）
```
## 运行网关
D:\developer\cloud>java -jar leo-gateway-1.0.0.RELEASE.jar
## 运行Sentinel 
java -Dserver.port=9100 -Dcsp.sentinel.app.type=1 -Dcsp.sentinel.dashboard.server=localhost:9100 -Dproject.name=sentinel-console -jar ./sentinel-dashboard-1.7.2.jar

完成后打开：http://localhost:9100/#/dashboard
默认用户名：sentinel/sentinel
```
#### 5. 链路追踪（根据情况Enable）

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

无

## 参与贡献

无需贡献

