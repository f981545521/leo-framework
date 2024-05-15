package cn.acyou.leo.framework.util.component;

import cn.acyou.leo.framework.prop.EmailProperty;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author youfang
 * @version [1.0.0, 2024/5/13 18:17]
 **/
@Slf4j
public class EmailUtil {

    private final EmailProperty emailProperty;

    public EmailUtil(EmailProperty emailProperty) {
        this.emailProperty = emailProperty;
    }

    public boolean sendEmail(String toMail, String title, String content) {
        String username = emailProperty.getUsername();
        String password = emailProperty.getPassword();
        Properties props = new Properties();
        props.put("mail.smtp.auth", emailProperty.getSmtpAuth());
        props.put("mail.smtp.host", emailProperty.getSmtpHost());
        props.put("mail.smtp.port", emailProperty.getSmtpPort());
        if (username.endsWith("@qq.com")) {
            props.put("mail.smtp.ssl.enable", "true");
        }
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMail));
            message.setSubject(title);
            message.setText(content);
            Transport.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
