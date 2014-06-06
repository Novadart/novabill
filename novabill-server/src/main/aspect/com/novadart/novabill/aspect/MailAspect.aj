package com.novadart.novabill.aspect;

import java.util.Map;

import javax.mail.MessagingException;
import org.apache.commons.lang3.CharEncoding;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.MailSendException;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.novadart.novabill.annotation.MailMixin;
import com.novadart.novabill.domain.Email;
import com.novadart.novabill.domain.EmailStatus;

privileged aspect MailAspect {
	
	private VelocityEngine velocityEngine;
	private String from;

	public void setFrom(String from) {
		this.from = from;
	}

	
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	declare parents : @MailMixin * implements MailSender;


	public void MailSender.sendMessage(String[] to, String replyTo, String subject, Map<String, Object> model, String templateLocation){
		MailAspect thisAspect = MailAspect.aspectOf();
		Email email = null;
		try {
			String text = VelocityEngineUtils.mergeTemplateIntoString(thisAspect.velocityEngine, templateLocation, 
					CharEncoding.UTF_8, model);
			email = new Email(to, thisAspect.from, subject, text);
			email.send();
		} catch (MessagingException | MailSendException e) {
			email.setTries(1);
			email.setStatus(EmailStatus.PENDING);
			email.persist();
		}
	}
	
	public void MailSender.sendMessage(String to, String replyTo, String subject, Map<String, Object> model, String templateLocation){
		sendMessage(new String[]{to}, replyTo, subject, model, templateLocation);
	}
	
	public void MailSender.sendMessage(String[] to, String subject, Map<String, Object> model, String templateLocation){
		sendMessage(to, null, subject, model, templateLocation);
	}
	
	public void MailSender.sendMessage(String to, String subject, Map<String, Object> model, String templateLocation){
		sendMessage(new String[]{to}, null, subject, model, templateLocation);
	}
	
}
