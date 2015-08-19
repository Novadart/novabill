package com.novadart.novabill.service.mail;

import com.novadart.novabill.domain.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class MailDispatcherService {

    @Autowired
    @Qualifier(JavaMailService.QUALIFIER)
    private MailSender javaMailSender;

    @Autowired
    @Qualifier(MailGunService.QUALIFIER)
    private MailSender mailGunSender;

    public boolean sendEmail(Email email, Consumer<String> onSuccess, Consumer<Throwable> onFailure){
        switch (email.getHandlingType()) {
            case INTERNAL:
                return javaMailSender.send(email, onSuccess, onFailure);
            case EXTERNAL_UNACKNOWLEDGED:
                return mailGunSender.send(email, onSuccess, onFailure);
            default: throw new IllegalArgumentException("No such email handling type");
        }
    }

}
