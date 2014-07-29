package com.novadart.novabill.web.mvc.ajax.dto;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.novadart.novabill.annotation.Trimmed;

public class RecommendByMailDTO {
	
	@NotEmpty
	@Trimmed
	@Email
	@Size(max = com.novadart.novabill.domain.Email.EMAIL_MAX_LENGTH)
	private String to;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

}
