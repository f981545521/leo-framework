### Mybatis Plus代码生成器

基于Mybatis Plus的代码生成器

### 使用说明：

#### 示例一：单Module项目

```
    public static void main(String[] args) {
        CustomCodeGenerator.instance("prompter_upload_video_issue")
                .author("youfang")
                .setDbConfig("com.mysql.cj.jdbc.Driver", "root", "root123")
                .setDbUrl("jdbc:mysql://localhost:3306/scorpio?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=Asia/Shanghai&useSSL=false")
                .packageParent("cn.acyou.leo.pay")
                .put(ConstVal.ENTITY_PATH, "", "entity2")
                .put(ConstVal.MAPPER_PATH, "", "mapper2")
                .put(ConstVal.XML_PATH, "", "mapper2")
                .put(ConstVal.SERVICE_PATH, "", "service2")
                .put(ConstVal.SERVICE_IMPL_PATH, "", "service2.impl")
                .put(ConstVal.CONTROLLER_PATH, "", "controller2")
                .doGenerator();
    }
```

#### 示例二：多Module项目

```
    public static void main(String[] args) {
        CustomCodeGenerator.instance("prompter_upload_video_issue")
                .author("youfang")
                .setDbConfig("com.mysql.cj.jdbc.Driver", "root", "root123")
                .setDbUrl("jdbc:mysql://localhost:3306/scorpio?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=Asia/Shanghai&useSSL=false")
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

> 注：
> - 项目为多`modules`项目的时候需要put module名（第二个字段）。单boot项目直接为空字符串即可。
> - 支持单独传入生成对应的文件。