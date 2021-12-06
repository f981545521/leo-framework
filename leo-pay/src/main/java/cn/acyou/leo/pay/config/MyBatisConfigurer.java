package cn.acyou.leo.pay.config;

import cn.acyou.leo.framework.mybatis.BaseEntityInterceptor;
import cn.acyou.leo.framework.mybatis.MyPageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author youfang
 * @date 2017-12-07 17:37
 **/
@Configuration
@EnableTransactionManagement
public class MyBatisConfigurer implements TransactionManagementConfigurer {

    @Autowired
    private DataSource dataSource;//注入数据源

    @Bean(name = "sqlSessionFactory")
    protected SqlSessionFactoryBean getSqlSessionFactoryBean() {
        SqlSessionFactoryBean sqlsession = new SqlSessionFactoryBean();
        sqlsession.setDataSource(dataSource);
        // typeAliasesPackage：它一般对应我们的实体类所在的包，这个时候会自动取对应包中不包括包名的简单类名作为包括包名的别名。多个package之间可以用逗号或者分号等来进行分隔。(value的值一定要是包的全名)
        sqlsession.setTypeAliasesPackage("cn.acyou.leo.pay.mapper.entity");//扫描entity包 使用别名
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setUseGeneratedKeys(true);//使用jdbc的getGeneratedKeys获取数据库自增主键值
        configuration.setUseColumnLabel(true);//使用列别名替换列名 select user as User
        //-自动使用驼峰命名属性映射字段   userId    user_id  **这样使用resultType也会自动映射了**
        //sqlsession.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        configuration.setMapUnderscoreToCamelCase(true);
        sqlsession.setConfiguration(configuration);
        sqlsession.setFailFast(true);

        //配置分页插件，详情请查阅官方文档 {@link https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md}
        MyPageInterceptor pageInterceptor = new MyPageInterceptor();
        Properties properties = new Properties();
        //PageSize是0时的处理：默认值为 false
        //当该参数设置为 true 时，如果 pageSize=0 或者 RowBounds.limit = 0 就会查询出全部的结果
        // （相当于没有执行分页查询，但是返回结果仍然是 Page 类型）。会导致传0后数量巨大
        //properties.setProperty("pageSizeZero", "false");
        //分页合理化参数：默认值为false。当该参数设置为 true 时，pageNum<=0 时会查询第一页，
        // pageNum>pages（超过总数时），会查询最后一页。默认false 时，直接根据参数进行查询。
        //properties.setProperty("reasonable", "false");
        //如果vo里有pageNum和pageSize 再加上配置文件里supportMethodsArguments= true，
        // 那么即使不使用PageHelper.startPage(pageNum,PageSize) 也会自动分页。
        //properties.setProperty("supportMethodsArguments", "false");
        pageInterceptor.setProperties(properties);
        //添加插件
        sqlsession.setPlugins(new Interceptor[]{pageInterceptor, new BaseEntityInterceptor()});
        //sqlsession.setPlugins(new Interceptor[]{pageHelper, new PerformanceInterceptor()});

        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            // mapperLocations：它表示我们的Mapper文件存放的位置，当我们的Mapper文件跟对应的Mapper接口处于同一位置的时候可以不用指定该属性的值。
            sqlsession.setMapperLocations(resolver.getResources("classpath*:mapper/*.xml"));
            return sqlsession;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }




}
