package com.novadart.novabill.shared.client.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PaymentTypeDTO implements IsSerializable {

	private Long id;
	
	private String name;
	
	private String defaultPaymentNote;
	
	private PaymentDateType paymentDateGenerator;
	
	private PaymentDateDelta paymentDateDelta;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultPaymentNote() {
		return defaultPaymentNote;
	}

	public void setDefaultPaymentNote(String defaultPaymentNote) {
		this.defaultPaymentNote = defaultPaymentNote;
	}

	public PaymentDateType getPaymentDateGenerator() {
		return paymentDateGenerator;
	}

	public void setPaymentDateGenerator(PaymentDateType paymentDateGenerator) {
		this.paymentDateGenerator = paymentDateGenerator;
	}

	public PaymentDateDelta getPaymentDateDelta() {
		return paymentDateDelta;
	}

	public void setPaymentDateDelta(PaymentDateDelta paymentDateDelta) {
		this.paymentDateDelta = paymentDateDelta;
	}
	
}
