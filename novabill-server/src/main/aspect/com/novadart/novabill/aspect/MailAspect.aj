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


	public boolean MailSender.sendMessage(String[] to, String from, String replyTo, String subject, Map<String, Object> model, String templateLocation, boolean retry,
			byte[] attachment, String attachmentName){
		MailAspect thisAspect = MailAspect.aspectOf();
		Email email = null;
		try {
			String text = VelocityEngineUtils.mergeTemplateIntoString(thisAspect.velocityEngine, templateLocation, 
					CharEncoding.UTF_8, model);
			email = new Email(to, from == null? thisAspect.from: from, subject, text, replyTo, attachment, attachmentName);
			email.send();
			return true;
		} catch (MessagingException | MailSendException e) {
			if(retry) {
				email.setTries(1);
				email.setStatus(EmailStatus.PENDING);
				email.persist();
			}
			return false;
		}
	}
	
	public boolean MailSender.sendMessage(String to, String replyTo, String subject, Map<String, Object> model, String templateLocation, boolean retry){
		return sendMessage(new String[]{to}, null, replyTo, subject, model, templateLocation, retry, null, null);
	}
	
	public boolean MailSender.sendMessage(String to, String from, String replyTo, String subject, Map<String, Object> model, String templateLocation, boolean retry){
		return sendMessage(new String[]{to}, from, replyTo, subject, model, templateLocation, retry, null, null);
	}
	
	public boolean MailSender.sendMessage(String[] to, String subject, Map<String, Object> model, String templateLocation){
		return sendMessage(to, null, null, subject, model, templateLocation, true, null, null);
	}
	
	public boolean MailSender.sendMessage(String to, String subject, Map<String, Object> model, String templateLocation){
		return sendMessage(new String[]{to}, null, null, subject, model, templateLocation, true, null, null);
	}
	
	public boolean MailSender.sendMessage(String to, String subject, Map<String, Object> model, String templateLocation, byte[] attachment, String attachmentName){
		return sendMessage(new String[]{to}, null, null, subject, model, templateLocation, true, attachment, attachmentName);
	}
	
}
