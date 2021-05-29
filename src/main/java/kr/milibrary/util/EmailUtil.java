package kr.milibrary.util;

import kr.milibrary.domain.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Component
public class EmailUtil {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailUtil(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    private String buildContext(String mailType, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(mailType, context);
    }

    public void sendEmail(Mail mail, String templateName) {
        String htmlTemplate = buildContext(templateName, mail.getVariables());
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
            message.setSubject(mail.getSubject());
            message.setTo(mail.getToAddr());
            message.setFrom(mail.getFromAddr());
            message.setText(htmlTemplate, true);
        };

        javaMailSender.send(preparator);
    }
}
