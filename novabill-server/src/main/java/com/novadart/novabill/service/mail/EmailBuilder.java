package com.novadart.novabill.service.mail;

import com.novadart.novabill.domain.Email;
import org.apache.commons.lang3.CharEncoding;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Configurable
public class EmailBuilder {

    @Autowired
    private VelocityEngine velocityEngine;

    @Value("${default.sender}")
    private String defaultSender;

    private List<String> to = new LinkedList<>();

    private String from;

    private String replyTo;

    private String subject;

    private String template;

    private Map<String, Object> model = new HashMap();

    private String attachmentName;

    private byte[] attachment;

    private MailHandlingType handlingType;

    private Map<String, String> variables = new HashMap<>();

    public EmailBuilder to(String email){
        to.add(email);
        return this;
    }

    public EmailBuilder to(String[] emails){
        if(emails != null)
            for(String email: emails)
                to.add(email);
        return this;
    }

    public EmailBuilder from(String email){
        from = email;
        return this;
    }

    public EmailBuilder replyTo(String email){
        replyTo = email;
        return this;
    }

    public EmailBuilder subject(String subject){
        this.subject = subject;
        return this;
    }

    public EmailBuilder template(String template){
        this.template = template;
        return this;
    }

    public EmailBuilder templateVar(String name, Object value){
        this.model.put(name, value);
        return this;
    }

    public EmailBuilder templateVars(Map<String, Object> vars){
        if(vars != null)
            model.putAll(vars);
        return this;
    }

    public EmailBuilder attachmentName(String attachmentName){
        this.attachmentName = attachmentName;
        return this;
    }

    public EmailBuilder attachment(byte[] attachment){
        this.attachment = attachment;
        return this;
    }

    public EmailBuilder handlingType(MailHandlingType handlingType){
        this.handlingType = handlingType;
        return this;
    }

    public EmailBuilder variable(String name, String value){
        this.variables.put(name, value);
        return this;
    }

    public Email build() {
        if(to.size() == 0 || subject == null || template == null || handlingType == null)
            throw new IllegalStateException();
        String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template,
                CharEncoding.UTF_8, model);
        String[] toArray = new String[to.size()];
        to.toArray(toArray);
        return new Email(toArray, from == null? defaultSender: from, subject, text, replyTo, attachment, attachmentName,
                handlingType, variables);
    }

}