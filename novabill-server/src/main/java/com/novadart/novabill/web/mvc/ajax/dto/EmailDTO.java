package com.novadart.novabill.web.mvc.ajax.dto;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.novadart.novabill.annotation.Trimmed;


public class EmailDTO {
	
	@NotEmpty
	@Trimmed
	@Email
	@Size(max = com.novadart.novabill.domain.Email.EMAIL_MAX_LENGTH)
	private String to;
	
	@NotEmpty
	@Trimmed
	@Email
	@Size(max = com.novadart.novabill.domain.Email.EMAIL_MAX_LENGTH)
	private String replyTo;
	
	@NotEmpty
	@Trimmed
	@Size(max = com.novadart.novabill.domain.Email.SUBJECT_MAX_LENGTH)
	private String subject;
	
	@NotEmpty
	@Trimmed
	@Size(max = com.novadart.novabill.domain.Email.TEXT_MAX_LENGTH)
	private String message;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
