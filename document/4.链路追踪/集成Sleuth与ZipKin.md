## 链路追踪

1. 使用openFegin调用直接使用即可
2. 使用Dubbo需要借助Filter，不用担心，有第三方Jar包[brave-instrumentation-dubbo]，直接使用


### 使用Dubbo的链路追踪
1. 导入依赖。`spring-cloud-starter-zipkin`已经包含`Sleuth`
```
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zipkin</artifactId>
            <version>2.2.8.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>io.zipkin.brave</groupId>
            <artifactId>brave-instrumentation-dubbo</artifactId>
            <version>5.13.3</version>
        </dependency>
```
2. bootstrap.yml
```
spring:
  sleuth:
    enabled: true
    rpc:
      enabled: true
  zipkin:
    enabled: true
    base-url: http://localhost:9411/
# dubbo中需要借助Filter
dubbo:
  consumer:
    filter: tracing
  provider:
    filter: tracing
```