package com.novadart.novabill.service.mail.mailgun;


import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface MailAcknowledgeHandler extends Consumer<List<Map<String, Object>>>{
}
