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
 * <h3>代码生成器</h3>
 * 示例：
 * <pre>
 * {@code
 * import cn.acyou.leo.framework.generator.CustomCodeGenerator;
 * import com.baomidou.mybatisplus.generator.config.ConstVal;
 *
 * public class PayCodeGenerator {
 *     public static void main(String[] args) {
 *         new CustomCodeGenerator("t_point_rule", "t_")
 *                 .author("youfang")
 *                 .setDbConfig("com.mysql.cj.jdbc.Driver", "root", "root123")
 *                 .setDbUrl("jdbc:mysql://localhost:3306/scorpio?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=UTC&useSSL=false")
 *                 .packageParent("cn.acyou.leo.pay")
 *                 .put(ConstVal.ENTITY_PATH, "leo-pay", "entity")
 *                 .put(ConstVal.MAPPER_PATH, "leo-pay", "mapper")
 *                 .put(ConstVal.XML_PATH, "leo-pay", "mappers")
 *                 .put(ConstVal.SERVICE_PATH, "leo-pay", "service")
 *                 .put(ConstVal.SERVICE_IMPL_PATH, "leo-pay", "service.impl")
 *                 .put(ConstVal.CONTROLLER_PATH, "leo-pay", "controller")
 *                 .doGenerator();
 *     }
 * }
 * }
 * </pre>
 *
 * @author youfang
 * @version [1.0.0, 2021-7-11]
 **/
public final class CustomCodeGenerator {

    /**
     * 要生产的表名
     */
    private String tableName = "student";
    /**
     * 移除表前缀
     */
    private String tablePrefix = "";
    /**
     * 数据库驱动
     */
    private String DRIVER = "com.mysql.cj.jdbc.Driver";
    /**
     * 数据库用户名
     */
    private String USER = "root";
    /**
     * 数据库密码
     */
    private String PASSWORD = "root123";
    /**
     * 文件作者
     */
    private String AUTHOR = "youfang";
    /**
     * 父包
     */
    private String PACKAGE_PARENT = "cn.acyou.leo.content";
    /**
     * 数据库驱动
     */
    private String JDBC_URL = "jdbc:mysql://localhost:3306/scorpio?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=UTC&useSSL=false";
    //项目地址，无需关系
    private static final String projectPath = System.getProperty("user.dir");
    //Module 文件存放项目配置
    public Map<String, String[]> modulesMap = new HashMap<>();

    public CustomCodeGenerator(String tableName) {
        this.tableName = tableName;
    }

    public CustomCodeGenerator(String tableName, String removeTablePrefix) {
        this.tableName = tableName;
        this.tablePrefix = removeTablePrefix;
    }

    public CustomCodeGenerator author(String AUTHOR) {
        this.AUTHOR = AUTHOR;
        return this;
    }

    public CustomCodeGenerator setDbConfig(String DRIVER, String USER, String PASSWORD) {
        this.DRIVER = DRIVER;
        this.USER = USER;
        this.PASSWORD = PASSWORD;
        return this;
    }

    public CustomCodeGenerator setDbUrl(String JDBC_URL) {
        this.JDBC_URL = JDBC_URL;
        return this;
    }

    public CustomCodeGenerator packageParent(String PACKAGE_PARENT) {
        this.PACKAGE_PARENT = PACKAGE_PARENT;
        return this;
    }

    public CustomCodeGenerator put(String pathKey, String moduleName, String packageName) {
        modulesMap.put(pathKey, new String[]{moduleName, packageName});
        return this;
    }

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
            if (t.contains("timestamp")) {
                return DbColumnType.DATE;
            }
            return new MySqlTypeConvert().processTypeConvert(globalConfig, fieldType);
        }
    };

    public void doGenerator() {
        if (modulesMap.size() == 0) {
            return;
        }
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
        autoGenerator.setTemplate(templateConfig);
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("");
        pc.setParent(PACKAGE_PARENT);
        Map<String, String> pathInfo = new HashMap<>();

        // 包信息
        if (modulesMap.get(ConstVal.ENTITY_PATH) != null) {
            pc.setEntity(modulesMap.get(ConstVal.ENTITY_PATH)[1]);
            packageInfo.put(ConstVal.ENTITY, joinPackage(pc.getParent(), pc.getEntity()));
            setPathInfo(pathInfo, templateConfig.getEntity(globalConfig.isKotlin()), globalConfig.getOutputDir() + "\\" + modulesMap.get(ConstVal.ENTITY_PATH)[0] + "\\src\\main\\java", ConstVal.ENTITY_PATH, ConstVal.ENTITY);
        }
        if (modulesMap.get(ConstVal.MAPPER_PATH) != null) {
            pc.setMapper(modulesMap.get(ConstVal.MAPPER_PATH)[1]);
            packageInfo.put(ConstVal.MAPPER, joinPackage(pc.getParent(), pc.getMapper()));
            setPathInfo(pathInfo, templateConfig.getMapper(), globalConfig.getOutputDir() + "\\" + modulesMap.get(ConstVal.MAPPER_PATH)[0] + "\\src\\main\\java", ConstVal.MAPPER_PATH, ConstVal.MAPPER);
        }
        if (modulesMap.get(ConstVal.XML_PATH) != null) {
            pc.setXml(modulesMap.get(ConstVal.XML_PATH)[1]);
            packageInfo.put(ConstVal.XML, joinPackage(pc.getParent(), pc.getXml()));
            pathInfo.put(ConstVal.XML_PATH, globalConfig.getOutputDir() + "\\" + modulesMap.get(ConstVal.XML_PATH)[0] + "\\src\\main\\resources\\" + modulesMap.get(ConstVal.XML_PATH)[1]);
        }
        if (modulesMap.get(ConstVal.SERVICE_PATH) != null) {
            pc.setService(modulesMap.get(ConstVal.SERVICE_PATH)[1]);
            packageInfo.put(ConstVal.SERVICE, joinPackage(pc.getParent(), pc.getService()));
            setPathInfo(pathInfo, templateConfig.getService(), globalConfig.getOutputDir() + "\\" + modulesMap.get(ConstVal.SERVICE_PATH)[0] + "\\src\\main\\java", ConstVal.SERVICE_PATH, ConstVal.SERVICE);
        }
        if (modulesMap.get(ConstVal.SERVICE_IMPL_PATH) != null) {
            pc.setServiceImpl(modulesMap.get(ConstVal.SERVICE_IMPL_PATH)[1]);
            packageInfo.put(ConstVal.SERVICE_IMPL, joinPackage(pc.getParent(), pc.getServiceImpl()));
            setPathInfo(pathInfo, templateConfig.getServiceImpl(), globalConfig.getOutputDir() + "\\" + modulesMap.get(ConstVal.SERVICE_IMPL_PATH)[0] + "\\src\\main\\java", ConstVal.SERVICE_IMPL_PATH, ConstVal.SERVICE_IMPL);
        }
        if (modulesMap.get(ConstVal.CONTROLLER_PATH) != null) {
            pc.setController(modulesMap.get(ConstVal.CONTROLLER_PATH)[1]);
            packageInfo.put(ConstVal.CONTROLLER, joinPackage(pc.getParent(), pc.getController()));
            setPathInfo(pathInfo, templateConfig.getController(), globalConfig.getOutputDir() + "\\" + modulesMap.get(ConstVal.CONTROLLER_PATH)[0] + "\\src\\main\\java", ConstVal.CONTROLLER_PATH, ConstVal.CONTROLLER);
        }

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
        strategy.setTablePrefix(tablePrefix);
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
