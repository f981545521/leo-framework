### Dubbo 与 Feign 的区别
#### 一、协议

- Dubbo：

支持多传输协议(Dubbo、Rmi、http、redis等等)，可以根据业务场景选择最佳的方式。非常灵活。
默认的Dubbo协议：利用Netty，TCP传输，单一、异步、长连接，适合数据量小、高并发和服务提供者远远少于消费者的场景。

- Feign：

基于Http传输协议，短连接，不适合高并发的访问。

### Dubbo-admin

[Dubbo-Admin中文文档](https://github.com/apache/dubbo-admin/blob/develop/README_ZH.md)

修改配置文件，打包运行
```
# nacos config, add parameters to url like username=nacos&password=nacos
admin.registry.address=nacos://127.0.0.1:8848?group=DEFAULT_GROUP&namespace=public&username=nacos&password=nacos
admin.config-center=nacos://127.0.0.1:8848?group=dubbo&username=nacos&password=nacos
admin.metadata-report.address=nacos://127.0.0.1:8848?group=dubbo&username=nacos&password=nacos
```