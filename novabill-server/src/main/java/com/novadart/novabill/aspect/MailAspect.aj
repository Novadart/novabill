package com.novadart.novabill.aspect;

import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;
import com.novadart.novabill.annotation.MailMixin;

privileged aspect MailAspect {
	
	private org.springframework.mail.javamail.JavaMailSender mailSender;
	private VelocityEngine velocityEngine;
	private String from;

	public void setFrom(String from) {
		this.from = from;
	}

	public void setMailSender(org.springframework.mail.javamail.JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	declare parents : @MailMixin * implements MailSender;


	public void MailSender.sendMessage(String to, String subject, Map<String, Object> model, String templateLocation){
		MailAspect thisAspect = MailAspect.aspectOf();
		MimeMessage mimeMessage = thisAspect.mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
		try {
			messageHelper.setTo(to);
			messageHelper.setFrom(thisAspect.from);
			messageHelper.setSubject(subject);
			String text = VelocityEngineUtils.mergeTemplateIntoString(thisAspect.velocityEngine, templateLocation, model);
			messageHelper.setText(text, true);
			thisAspect.mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
}
