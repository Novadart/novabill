package com.novadart.novabill.service.mail;

import com.novadart.novabill.domain.Email;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.function.Consumer;

@Qualifier(JavaMailService.QUALIFIER)
@Service
public class JavaMailService implements MailSender {

    public static final String QUALIFIER = "JavaMailSender";

    @Autowired
    private transient JavaMailSender mailSender;

    @Override
    public boolean send(Email email, Consumer<String> onSuccess, Consumer<Throwable> onFailure) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        boolean hasAttachment = email.getAttachment() != null && StringUtils.isNotBlank(email.getAttachmentName());
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, hasAttachment, CharEncoding.UTF_8);
            messageHelper.setTo(email.getTo());
            messageHelper.setFrom(email.getFrom());
            messageHelper.setSubject(email.getSubject());
            messageHelper.setText(email.getText(), true);
            if(StringUtils.isNotBlank(email.getReplyTo()))
                messageHelper.setReplyTo(email.getReplyTo());
            if(hasAttachment)
                messageHelper.addAttachment(email.getAttachmentName(),
                        new ByteArrayResource(email.getAttachment()));
            mailSender.send(mimeMessage);
            onSuccess.accept("");
            return true;
        } catch (Throwable t) {
            onFailure.accept(t);
            return false;
        }
    }
}
