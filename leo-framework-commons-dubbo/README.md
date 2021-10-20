# Leo-framework-commons-dubbo
Leo-Framework Dubbo支持

## 使用说明

1. Maven的`pom.xml`中
```
        <dependency>
            <groupId>cn.acyou</groupId>
            <artifactId>leo-framework-commons-dubbo</artifactId>
            <version>1.1.1.RELEASE</version>
        </dependency>
```
2. 项目的`application.yml`中， 增加：`appContextFilter`

    ``` yaml
    dubbo:
      consumer:
        # 关闭所有服务的启动时检查（没有提供者时会报错）
        check: false
        # Dubbo在调用时出现非业务异常，重试2次（不含第一次）
        retries: 2
        filter: tracing,appContextFilter
      provider:
        filter: tracing,appContextFilter
    ```
3. 使用dubbo服务之间 都可以使用`AppContext`了 ~
