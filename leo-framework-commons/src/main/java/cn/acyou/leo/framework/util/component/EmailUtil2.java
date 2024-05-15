package cn.acyou.leo.framework.util.component;

import cn.acyou.leo.framework.util.SpringHelper;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;

/**
 * @author youfang
 * @version [1.0.0, 2024/5/14 16:25]
 **/
public class EmailUtil2 {
    private static final JavaMailSenderImpl mailSender;

    static {
        mailSender = SpringHelper.getBean(JavaMailSenderImpl.class);
    }

    public static boolean send(String toMail, String title, String content) {
        return send(toMail, title, content, false, null, null);
    }

    public static boolean send(String toMail, String title, String content, boolean isHtml) {
        return send(toMail, title, content, isHtml, null, null);
    }


    public static boolean send(String toMail, String title, String content, boolean isHtml, String attachmentFilename, InputStreamSource inputStreamSource) {
        try {
            String username = mailSender.getUsername();
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(username);
            helper.setTo(toMail);
            helper.setSubject(title);
            if (attachmentFilename != null) {
                helper.addAttachment(attachmentFilename, inputStreamSource);
            }
            helper.setText(content, isHtml);
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
