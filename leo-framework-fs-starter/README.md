# Leo-framework-fs-starter
Leo-Framework 文件系统 Starter

## 使用说明

1. Maven的`pom.xml`中
```
        <dependency>
            <groupId>cn.acyou</groupId>
            <artifactId>leo-framework-fs-starter</artifactId>
            <version>1.0.1.RELEASE</version>
        </dependency>
```
2. 项目的`application.yml`中
```
oss:
  endpoint: oss-cn-beijing.aliyuncs.com
  access-key-id: ***
  access-key-secret: ***
```
3. 使用
```
    OSSUtils.uploadWithProcess("dev-acyou-cn", "kodo_temp.zip", new File("D:\\tmp5\\kodo.zip"), new ProgressCallback() {
        @Override
        public void progress(int percent) {
            System.out.println("上传回调：进度" + percent);
        }

        @Override
        public void success() {
            System.out.println("上传回调：完成！");
        }
    });
```
