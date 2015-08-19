package com.novadart.novabill.service.mail;

import com.novadart.novabill.domain.Email;

import java.util.function.Consumer;

public interface MailSender {

    boolean send(Email email, Consumer<String> onSuccess, Consumer<Throwable> onFailure);

}
