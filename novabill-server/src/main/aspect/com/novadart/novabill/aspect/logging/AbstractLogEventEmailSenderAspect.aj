package com.novadart.novabill.aspect.logging;

import com.novadart.novabill.service.mail.EmailBuilder;
import com.novadart.novabill.service.mail.MailHandlingType;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

public abstract aspect AbstractLogEventEmailSenderAspect {

	protected boolean sendEmail;
	
	protected String[] emailAddresses;
	
	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}
	
	public void setEmailAddresses(String[] emailAddresses){
		this.emailAddresses = emailAddresses;
	}
	
	protected void sendEmailMessage(String eventType, String principal, Date time, Map<String, Object> otherTemplateVars, String templatePath){
		if(sendEmail){
			new EmailBuilder().to(emailAddresses)
					.subject("[Novabill Notification] ~ "+eventType)
					.template(templatePath)
					.templateVar("eventType", eventType)
					.templateVar("principal", principal)
					.templateVar("time", time)
					.templateVar("otherVars", otherTemplateVars == null? Collections.emptyMap(): otherTemplateVars)
					.handlingType(MailHandlingType.INTERNAL)
					.from("no-reply@s1.novabill.it")
					.build().send();
		}
	}
	
	protected void sendEmailMessage(String eventType, String principal, Date time, Map<String, Object> otherTemplateVars){
		sendEmailMessage(eventType, principal, time, otherTemplateVars, "mail-templates/log-event-notification.vm");
	}
	
	protected void handleEvent(Logger logger,  String eventType, String principal, Date eventTime, Map<String, Object> templateVars){
		Date time = eventTime == null? new Date(System.currentTimeMillis()): eventTime;
		logger.info("[{}, {}, {}]", new Object[]{principal, eventType.toLowerCase(), time});
		sendEmailMessage(eventType, principal, time, templateVars);
	}
	
}
