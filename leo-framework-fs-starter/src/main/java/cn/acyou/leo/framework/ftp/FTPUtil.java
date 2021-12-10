package cn.acyou.leo.framework.ftp;

import cn.acyou.leo.framework.exception.FsException;
import cn.acyou.leo.framework.prop.FTPProperty;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Date;

/**
 * @author fangyou
 * @version [1.0.0, 2021-12-10 10:41]
 */
@Component
@EnableConfigurationProperties(value = FTPProperty.class)
public class FTPUtil {
    private static final Logger log = LoggerFactory.getLogger(FTPUtil.class);

    private static FTPProperty ftpProperty;

    /**
     * 本地字符编码
     */
    private static final String LOCAL_CHARSET = "GBK";
    /**
     * FTP协议里面，规定文件名编码为iso-8859-1
     */
    private static final String SERVER_CHARSET = "ISO-8859-1";

    @Autowired
    public void setFtpProperties(FTPProperty ftpProperty) {
        if (!StringUtils.hasText(ftpProperty.getHost())) {
            log.warn("FTP 未配置，将影响FTP使用！");
            return;
        }
        FTPUtil.ftpProperty = ftpProperty;
    }

    /**
     * 上传文件
     *
     * @param filePath 文件路径
     * @param filename 文件名
     * @param input    输入
     * @return boolean
     */
    public String uploadFile(String filePath, String filename, InputStream input) {
        boolean b = uploadFile(ftpProperty.getHost(), ftpProperty.getPort(), ftpProperty.getUsername(), ftpProperty.getPassword(), ftpProperty.getBasePath(), filePath, filename, input);
        if (b) {
            return ftpProperty.getBasePathUrl() + filePath + filename;
        }
        return null;
    }

    /**
     * 下载文件
     *
     * @param filePath 文件路径
     * @param filename 文件名
     * @param os       操作系统
     * @return boolean
     */
    public boolean downloadFile(String filePath, String filename, OutputStream os) {
        return downloadFile(ftpProperty.getHost(), ftpProperty.getPort(), ftpProperty.getUsername(), ftpProperty.getPassword(), ftpProperty.getBasePath(), filePath + "/" + filename, os);
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @param filename 文件名
     * @return boolean
     */
    public boolean deleteFile(String filePath, String filename) {
        return deleteFile(ftpProperty.getHost(), ftpProperty.getPort(), ftpProperty.getUsername(), ftpProperty.getPassword(), ftpProperty.getBasePath(), filePath + "/" + filename);
    }

    /**
     * Description: 向FTP服务器上传文件
     *
     * <pre>
     *         FileInputStream in = new FileInputStream(new File("D:\\images\\6.jpg"));
     *         FTPUtil.uploadFile("192.168.0.202", 3721, "", "", "/home/ftpuser/","/2018/04/11", "5.jpg", in);
     * </pre>
     *
     * @param host     FTP服务器hostname
     * @param port     FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param basePath FTP服务器基础目录
     * @param filePath FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
     * @param filename 上传到FTP服务器上的文件名
     * @param input    输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String host, int port, String username, String password, String basePath,
                                     String filePath, String filename, InputStream input) {
        FTPClient ftp = checkGetFTPClient(host, port, username, password);
        try {
            //切换目录
            if (!ftp.changeWorkingDirectory(basePath + filePath)) {
                //如果目录不存在创建目录
                String[] dirs = filePath.split("/");
                String tempPath = basePath;
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) continue;
                    tempPath += "/" + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            return false;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            //设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            //上传文件
            log.info("准备上传文件：{}", ftp.printWorkingDirectory() + "/" + filename);
            return ftp.storeFile(filename, input);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                if (ftp.isConnected()) {
                    ftp.logout();
                    ftp.disconnect();
                }
            } catch (IOException ioe) {
                //ignore
            }
        }
        return false;
    }

    /**
     * Description: 从FTP服务器下载文件
     *
     * @param host       FTP服务器hostname
     * @param port       FTP服务器端口
     * @param username   FTP登录账号
     * @param password   FTP登录密码
     * @param remotePath FTP服务器上的相对路径
     * @param fileName   要下载的文件名
     * @param os         输出流
     * @return true/false
     */
    public static boolean downloadFile(String host, int port, String username, String password, String remotePath,
                                       String fileName, OutputStream os) {
        FTPClient ftp = checkGetFTPClient(host, port, username, password);
        try {
            return ftp.retrieveFile(remotePath + fileName, os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.logout();
                    ftp.disconnect();
                } catch (IOException ioe) {
                    //ignore
                }
            }
        }
        return false;
    }


    /**
     * 检查并获取  FTPClient 对象
     *
     * @param host     主机
     * @param port     FTP端口 默认为21
     * @param userName 用户名
     * @param password 登录密码
     * @return FTPClient
     */
    private static FTPClient checkGetFTPClient(String host, int port, String userName, String password) {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(host, port);// 连接FTP服务器
            ftpClient.login(userName, password);// 登陆FTP服务器
            int replyCode = ftpClient.getReplyCode();// 验证FTP服务器是否登录成功
            if (FTPReply.isPositiveCompletion(replyCode)) {
                log.info("FTP连接成功。");
                return ftpClient;
            } else {
                log.info("未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("FTP连接失败，请检查。");
        }
        throw new FsException("FTP Client 获取失败！");
    }

    /**
     * 删除文件
     *
     * <pre>
     *     FTPUtil.deleteFile("192.168.0.202", 3721, "", "", "/home/ftpuser/2018/04/11","5.jpg");
     * </pre>
     *
     * @param host       FTP服务器地址
     * @param port       FTP服务器端口号
     * @param username   FTP登录帐号
     * @param password   FTP登录密码
     * @param remotePath FTP服务器保存目录
     * @param filename   要删除的文件名称
     * @return 删除成功/失败
     */
    public static boolean deleteFile(String host, int port, String username, String password, String remotePath, String filename) {
        boolean flag = false;
        FTPClient ftpClient = checkGetFTPClient(host, port, username, password);
        try {
            // 切换FTP目录
            ftpClient.changeWorkingDirectory(remotePath);
            // 设置上传文件的类型为二进制类型
            String charset = LOCAL_CHARSET;
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
                charset = "UTF-8";
            }
            ftpClient.enterLocalPassiveMode();// 设置被动模式
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);// 设置传输的模式
            //对中文名称进行转码
            filename = new String(filename.getBytes(charset), SERVER_CHARSET);
            ftpClient.dele(filename);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.logout();//退出登录
                    ftpClient.disconnect();//关闭连接
                } catch (IOException e) {
                    //ignore
                }
            }
        }
        return flag;
    }


    public static void main(String[] args) throws Exception {
        boolean success = false;
        //上传文件
        //success = FTPUtil.uploadFile("192.168.0.202", 3721, "", "", "/home/ftpuser", "/2021/12/10", "ab.jpeg", new FileInputStream("D:\\images\\ab.jpeg"));
        //下载文件
        //success = FTPUtil.downloadFile("192.168.0.202", 3721, "", "", "/home/ftpuser", "/2018/04/11/6.jpg", new FileOutputStream("D:\\temp\\ftpDown.jpg"));
        //删除文件
        //success = FTPUtil.deleteFile("192.168.0.202", 3721, "", "", "/home/ftpuser/2018/04/11", "5.jpg");
        System.out.println(success);
    }
}
