package cn.acyou.leo.framework.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fangyou
 * @version [1.0.0, 2021-09-01 10:56]
 */
@ConfigurationProperties(prefix = "fs.ftp")
public class FTPProperty {
    /**
     * 主机
     */
    private String host;
    /**
     * 端口
     */
    private int port;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 基础路径
     */
    private String basePath;
    /**
     * 基础路径对应的域名访问
     */
    private String basePathUrl;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getBasePathUrl() {
        return basePathUrl;
    }

    public void setBasePathUrl(String basePathUrl) {
        this.basePathUrl = basePathUrl;
    }
}
