package cn.acyou.leo.tool.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;

/**
 * @author youfang
 * @version [1.0.0, 2024/12/27 17:13]
 **/
@Slf4j
public class DBCreateInitializer {

    private static final String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS `%s` DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_bin";

    public DBCreateInitializer(DataSource dataSource) throws Exception {
        try {
            Connection connection = dataSource.getConnection();
            connection.close();
        } catch (Exception e) {
            String message = e.getMessage();
            if (message.startsWith("Unknown database ") && dataSource instanceof HikariDataSource) {
                log.error(message + ": auto create database ...");
                HikariDataSource dataSource1 = (HikariDataSource) dataSource;
                String jdbcUrl = dataSource1.getJdbcUrl().substring(0, dataSource1.getJdbcUrl().indexOf("?"));
                String dbName = jdbcUrl.substring(jdbcUrl.lastIndexOf("/") + 1);
                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(jdbcUrl.substring(0, jdbcUrl.lastIndexOf("/")) + "?serverTimezone=Asia/Shanghai"); // 这里是数据库服务器的地址，不包含数据库名称
                config.setUsername(dataSource1.getUsername());
                config.setPassword(dataSource1.getPassword());
                config.setDriverClassName(dataSource1.getDriverClassName());
                DataSource newDataSource = new HikariDataSource(config);//这里的newDataSource将会被GC自动回收
                Connection connection = newDataSource.getConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate(String.format(createDatabaseSQL, dbName));
                statement.close();
                connection.close();
            }
        }
    }
}
