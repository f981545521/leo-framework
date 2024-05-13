package cn.acyou.leo.framework.tests;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author youfang
 * @version [1.0.0, 2024/5/13 17:07]
 **/
public class MainTest3 {
    public static void main(String[] args) throws Exception {
        final String username = "iblog_admin@126.com"; // 替换为你的邮箱用户名
        final String password = "xxx"; // 替换为你的邮箱密码

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.126.com");
        props.put("mail.smtp.port", "25");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username)); // 设置发件人
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("youfang@acyou.cn")); // 设置收件人
            message.setSubject("Testing Subject"); // 设置邮件主题
            message.setText("This is a test email message"); // 设置邮件正文
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            throw new RuntimeException("Error: cannot send email", e);
        }
    }
}
