## XXL-JOB
XXL-JOB是一个分布式任务调度平台

- 中文文档：https://www.xuxueli.com/xxl-job/

- 源码地址：https://gitee.com/xuxueli0323/xxl-job


### 第一步：创建XXL-JOB的内置表
sql: [XXL-JOB的内置表SQL](tables_xxl_job.sql)

### 第二步：下载打好的包
https://gitee.com/f981545521/developer_tool/releases/tag/v1
下载文件：xxl-job-admin-3.0.1-SNAPSHOT.jar

执行命令启动Admin
```shell
PS D:\workspace\dev-tools> java -jar .\xxl-job-admin-2.4.2-SNAPSHOT.jar --spring.datasource.username=root --spring.datasource.password=root123 --spring.datasource.url='jdbc:mysql://127.0.0.1:3306/scorpio?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai'
PS D:\workspace\dev-tools> java -jar .\xxl-job-admin-3.0.1-SNAPSHOT.jar --spring.datasource.username=root --spring.datasource.password=root123 --spring.datasource.url='jdbc:mysql://127.0.0.1:3306/scorpio?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai'
```
完成后访问：http://localhost:8080/xxl-job-admin/

默认用户名密码：admin/123456


### 本项目中使用

定义任务：

```java
@Slf4j
@Component
public class XxlTask {

    @XxlJob("oneHandler")
    public void oneHandler() throws Exception {
        log.info("执行任务成功");
        XxlJobHelper.log("XXL-JOB, 执行任务成功.");
    }
}

```
启用执行器
```yaml
leo:
  xxl-job:
    enable: true
    admin-addresses: http://127.0.0.1:8080/xxl-job-admin
    admin-token: default_token
    appname: xxl-job-executor
```