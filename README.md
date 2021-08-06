# leo-framework

#### 介绍
SpringCloud项目

#### 软件架构
1. Nacos作为注册中心、配置中心
2. Apache Dubbo作为服务调用（RPC）框架
3. 


#### 安装教程

#### 必须
1. 启动Nacos（必须）
```
执行：`PS D:\developer\cloud\nacos\bin> .\startup.cmd -m standalone`
安装完成后地址
打开：`http://127.0.0.1:8848/nacos/index.html`
```
2. 启动Seata Server （必须）
```
双击打开：
D:\developer\cloud\seata-server-1.4.2\bin\seata-server.bat
```
4. 如果使用网关（需要打开Sentinel）
```
java -Dserver.port=9100 -Dcsp.sentinel.app.type=1 -Dcsp.sentinel.dashboard.server=localhost:9100 -Dproject.name=sentinel-console -jar ./sentinel-dashboard-1.7.2.jar
```
#### 可选
3. 启动Dubbo Admin（可选）

修改配置文件，打包运行
```
# nacos config, add parameters to url like username=nacos&password=nacos
admin.registry.address=nacos://127.0.0.1:8848?group=DEFAULT_GROUP&namespace=public&username=nacos&password=nacos
admin.config-center=nacos://127.0.0.1:8848?group=dubbo&username=nacos&password=nacos
admin.metadata-report.address=nacos://127.0.0.1:8848?group=dubbo&username=nacos&password=nacos
```

#### 使用说明

无

#### 参与贡献

无需贡献

