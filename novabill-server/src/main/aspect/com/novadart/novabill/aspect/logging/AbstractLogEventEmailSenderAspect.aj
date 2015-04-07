package com.novadart.novabill.aspect.logging;

import com.novadart.novabill.annotation.MailMixin;
import org.slf4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@MailMixin
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
			Map<String, Object> templateVars = new HashMap<String, Object>();
			templateVars.put("eventType", eventType);
			templateVars.put("principal", principal);
			templateVars.put("time", time);
			if(otherTemplateVars != null)
				templateVars.put("otherVars", otherTemplateVars);
			sendMessage(emailAddresses, eventType, templateVars, templatePath);
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
