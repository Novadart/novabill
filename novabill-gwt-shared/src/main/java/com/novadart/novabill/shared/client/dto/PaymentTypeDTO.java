package com.novadart.novabill.shared.client.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PaymentTypeDTO implements IsSerializable {

	private Long id;
	
	private String name;
	
	private String defaultPaymentNote;
	
	private PaymentDateType paymentDateGenerator;
	
	private Integer paymentDateDelta;
	
	private Integer secondaryPaymentDateDelta;
	
	private BusinessDTO business;

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

	public Integer getPaymentDateDelta() {
		return paymentDateDelta;
	}

	public void setPaymentDateDelta(Integer paymentDateDelta) {
		this.paymentDateDelta = paymentDateDelta;
	}

	public Integer getSecondaryPaymentDateDelta() {
		return secondaryPaymentDateDelta;
	}

	public void setSecondaryPaymentDateDelta(Integer secondaryPaymentDateDelta) {
		this.secondaryPaymentDateDelta = secondaryPaymentDateDelta;
	}

	public BusinessDTO getBusiness() {
		return business;
	}

	public void setBusiness(BusinessDTO business) {
		this.business = business;
	}
	
}
