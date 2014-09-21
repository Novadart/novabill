package com.novadart.novabill.web.mvc.command;

import java.io.Serializable;

import com.novadart.novabill.domain.EmailPasswordHolder;


/*
 * Registration class holds the data of a account registration request.
 */
public class Registration extends EmailPasswordHolder implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private boolean agreementAccepted;
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Registration registration = copy(new Registration()); 
		registration.setAgreementAccepted(isAgreementAccepted());
		return registration;
	}
	
	public boolean isAgreementAccepted() {
		return agreementAccepted;
	}
	
	public void setAgreementAccepted(boolean agreementAccepted) {
		this.agreementAccepted = agreementAccepted;
	}

}
