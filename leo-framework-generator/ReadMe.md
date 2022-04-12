### Mybatis Plus代码生成器

#### 使用说明：

示例一：

```
    public static void main(String[] args) {
        new CustomCodeGenerator("prompter_upload_video_issue")
                .author("youfang")
                .setDbConfig("com.mysql.cj.jdbc.Driver", "root", "root123")
                .setDbUrl("jdbc:mysql://localhost:3306/scorpio?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=UTC&useSSL=false")
                .packageParent("cn.acyou.leo.pay")
                .put(ConstVal.ENTITY_PATH, "leo-pay", "entity2")
                .put(ConstVal.MAPPER_PATH, "leo-pay", "mapper2")
                .put(ConstVal.XML_PATH, "leo-pay", "mapper2")
                .put(ConstVal.SERVICE_PATH, "leo-pay", "service2")
                .put(ConstVal.SERVICE_IMPL_PATH, "leo-pay", "service2.impl")
                .put(ConstVal.CONTROLLER_PATH, "leo-pay", "controller2")
                .doGenerator();
    }
```

示例二：

```
    public static void main(String[] args) {
        new CustomCodeGenerator("prompter_upload_video_issue")
                .author("youfang")
                .setDbConfig("com.mysql.cj.jdbc.Driver", "root", "root123")
                .setDbUrl("jdbc:mysql://localhost:3306/scorpio?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=UTC&useSSL=false")
                .packageParent("cn.acyou.leo.content")
                .put(ConstVal.ENTITY_PATH, "leo-content-mapper", "entity2")
                .put(ConstVal.MAPPER_PATH, "leo-content-mapper", "mapper2")
                .put(ConstVal.XML_PATH, "leo-content-mapper", "mapper2")
                .put(ConstVal.SERVICE_PATH, "leo-content-service", "service2")
                .put(ConstVal.SERVICE_IMPL_PATH, "leo-content-service", "service2.impl")
                .put(ConstVal.CONTROLLER_PATH, "leo-content-web", "controller2")
                .doGenerator();
    }
```