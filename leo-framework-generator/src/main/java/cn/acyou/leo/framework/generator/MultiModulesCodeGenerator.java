package cn.acyou.leo.framework.generator;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author youfang
 * @version [1.0.0, 2021-7-11]
 **/
public class MultiModulesCodeGenerator {

    public static String tableName = "student";
    public static String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static String USER = "root";
    public static String PASSWORD = "root123";
    public static String AUTHOR = "youfang";
    public static String PACKAGE_PARENT = "cn.acyou.leo.content";
    public static String JDBC_URL = "jdbc:mysql://localhost:3306/scorpio?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=UTC&useSSL=false";

    //Module 文件存放项目配置
    public static Map<String, String> modulesMap = new HashMap<>();

    static {
        modulesMap.put(ConstVal.ENTITY_PATH, "leo-content-mapper");
        modulesMap.put(ConstVal.MAPPER_PATH, "leo-content-mapper");
        modulesMap.put(ConstVal.XML_PATH, "leo-content-mapper");
        modulesMap.put(ConstVal.SERVICE_PATH, "leo-content-service");
        modulesMap.put(ConstVal.SERVICE_IMPL_PATH, "leo-content-service");
        modulesMap.put(ConstVal.CONTROLLER_PATH, "leo-content-web");
    }

    private static final String projectPath = System.getProperty("user.dir");

    private static Map<String, String> packageInfo = CollectionUtils.newHashMapWithExpectedSize(7);

    private static final ITypeConvert typeConvert = new ITypeConvert() {
        /**
         * datetime默认生成的java类型为localDateTime, 改成Date类型
         */
        @Override
        public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
            String t = fieldType.toLowerCase();
            if (t.contains("datetime")) {
                return DbColumnType.DATE;
            }
            return new MySqlTypeConvert().processTypeConvert(globalConfig, fieldType);
        }
    };

    public static void doGenerator() {
        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();
        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();

        globalConfig.setOutputDir(projectPath);//生成文件的输出目录
        globalConfig.setAuthor(AUTHOR);//作者
        globalConfig.setOpen(false);//是否打开输出目录
        globalConfig.setSwagger2(true);//实体属性 Swagger2 注解
        globalConfig.setFileOverride(true);//覆盖已有文件
        globalConfig.setBaseResultMap(true);//开启 BaseResultMap
        globalConfig.setBaseColumnList(true);//开启 baseColumnList
        globalConfig.setServiceName("%sService");//去掉Service文件名前默认的`I`
        autoGenerator.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(JDBC_URL);
        dsc.setDriverName(DRIVER);
        dsc.setUsername(USER);
        dsc.setPassword(PASSWORD);
        dsc.setTypeConvert(typeConvert);
        autoGenerator.setDataSource(dsc);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();
        // templateConfig.setXml(null);
        autoGenerator.setTemplate(templateConfig);


        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("");
        pc.setParent(PACKAGE_PARENT);

        // 包信息
        packageInfo.put(ConstVal.ENTITY, joinPackage(pc.getParent(), pc.getEntity()));
        packageInfo.put(ConstVal.MAPPER, joinPackage(pc.getParent(), pc.getMapper()));
        packageInfo.put(ConstVal.XML, joinPackage(pc.getParent(), pc.getXml()));
        packageInfo.put(ConstVal.SERVICE, joinPackage(pc.getParent(), pc.getService()));
        packageInfo.put(ConstVal.SERVICE_IMPL, joinPackage(pc.getParent(), pc.getServiceImpl()));
        packageInfo.put(ConstVal.CONTROLLER, joinPackage(pc.getParent(), pc.getController()));

        Map<String, String> pathInfo = new HashMap<>();

        pathInfo.put(ConstVal.XML_PATH, globalConfig.getOutputDir() + "\\" + modulesMap.get(ConstVal.XML_PATH) + "\\src\\main\\resources\\mappers");
        setPathInfo(pathInfo, templateConfig.getEntity(globalConfig.isKotlin()), globalConfig.getOutputDir() + "\\" + modulesMap.get(ConstVal.ENTITY_PATH) + "\\src\\main\\java", ConstVal.ENTITY_PATH, ConstVal.ENTITY);
        setPathInfo(pathInfo, templateConfig.getMapper(), globalConfig.getOutputDir() + "\\" + modulesMap.get(ConstVal.MAPPER_PATH) + "\\src\\main\\java", ConstVal.MAPPER_PATH, ConstVal.MAPPER);
        setPathInfo(pathInfo, templateConfig.getService(), globalConfig.getOutputDir() + "\\" + modulesMap.get(ConstVal.SERVICE_PATH) + "\\src\\main\\java", ConstVal.SERVICE_PATH, ConstVal.SERVICE);
        setPathInfo(pathInfo, templateConfig.getServiceImpl(), globalConfig.getOutputDir() + "\\" + modulesMap.get(ConstVal.SERVICE_IMPL_PATH) + "\\src\\main\\java", ConstVal.SERVICE_IMPL_PATH, ConstVal.SERVICE_IMPL);
        setPathInfo(pathInfo, templateConfig.getController(), globalConfig.getOutputDir() + "\\" + modulesMap.get(ConstVal.CONTROLLER_PATH) + "\\src\\main\\java", ConstVal.CONTROLLER_PATH, ConstVal.CONTROLLER);
        pc.setPathInfo(pathInfo);

        autoGenerator.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        autoGenerator.setCfg(cfg);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategy.setSuperEntityClass("BaseEntity");//父类实体,没有就不用设置!
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
        //strategy.setSuperControllerClass("BaseController");//父类控制器,没有就不用设置!
        // 写于父类中的公共字段
        //strategy.setSuperEntityColumns("id");
        strategy.setInclude(tableName);
        strategy.setControllerMappingHyphenStyle(false);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        autoGenerator.setStrategy(strategy);
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
        autoGenerator.execute();
    }

    private static void setPathInfo(Map<String, String> pathInfo, String template, String outputDir, String path, String module) {
        if (StringUtils.isNotBlank(template)) {
            pathInfo.put(path, joinPath(outputDir, packageInfo.get(module)));
        }
    }

    /**
     * 连接路径字符串
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    private static String joinPath(String parentDir, String packageName) {
        if (StringUtils.isBlank(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
        return parentDir + packageName;
    }

    private static String joinPackage(String parent, String subPackage) {
        return StringUtils.isBlank(parent) ? subPackage : (parent + StringPool.DOT + subPackage);
    }
}
