package com.novadart.novabill.domain;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.novadart.novabill.annotation.Trimmed;
import com.novadart.novabill.service.mail.MailDispatcherService;
import com.novadart.novabill.service.mail.MailHandlingType;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Configurable
public class Email {

	public static final int SUBJECT_MAX_LENGTH = 78;
	
	public static final int TEXT_MAX_LENGTH = 15_000;
	
	public static final int EMAIL_MAX_LENGTH = 255;
	
	
	@Autowired
	private MailDispatcherService mailDispatcherService;

	@Size(max = EMAIL_MAX_LENGTH)
	@Trimmed
	private String to;
	
	@Size(max = EMAIL_MAX_LENGTH)
	@Trimmed
	private String from;

	@Size(max = SUBJECT_MAX_LENGTH)
	private String subject;
	
	@Size(max = TEXT_MAX_LENGTH)
	private String text;
	
	@Size(max = EMAIL_MAX_LENGTH)
	@Trimmed
	private String replyTo;

    private byte[] attachment;
	
	private String attachmentName;
	
	private MailHandlingType handlingType;
	
	public Email(){}
	
	public Email(String[] to, String from, String subject, String text, String replyTo, byte[] attachment, String attachmentName,
				 MailHandlingType handlingType) {
		this.setTo(to);
		this.from = from;
		this.subject = subject;
		this.text = text;
		this.replyTo = replyTo;
		this.attachment = attachment;
		this.attachmentName = attachmentName;
		this.handlingType = handlingType;
	}
	
	public Email(String[] to, String from, String subject, String text, String replyTo, MailHandlingType handlingType) {
		this(to, from, subject, text, replyTo, null, null, handlingType);
	}
	
	public Email(String[] to, String from, String subject, String text, MailHandlingType handlingType) {
		this(to, from, subject, text, null, null, null, handlingType);
	}

	public boolean send() {
		return mailDispatcherService.sendEmail(this, s -> {}, throwable -> {});
	}

	public boolean send(Consumer<String> onSuccess, Consumer<Throwable> onFailure){
		return mailDispatcherService.sendEmail(this, onSuccess, onFailure);
	}

	public String[] getTo() {
		List<String> r = new ArrayList<>();
		for(String email: Splitter.on(";").split(this.to))
			r.add(email);
		return r.toArray(new String[r.size()]);
	}

	public void setTo(String[] to) {
		this.to = Joiner.on(";").join(to);
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public MailHandlingType getHandlingType() {
		return handlingType;
	}

	public void setHandlingType(MailHandlingType handlingType) {
		this.handlingType = handlingType;
	}

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
	
}
