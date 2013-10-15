package com.novadart.novabill.shared.client.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InvoiceDTO extends AbstractInvoiceDTO implements IsSerializable, IInvoiceDTO {
	
	private String paymentTypeName;
    
    private PaymentDateType paymentDateGenerator;
    
    private Integer paymentDateDelta;
    
    @Override
	public PaymentDateType getPaymentDateGenerator() {
		return paymentDateGenerator;
	}

	@Override
	public void setPaymentDateGenerator(PaymentDateType paymentDateGenerator) {
		this.paymentDateGenerator = paymentDateGenerator;
	}

	@Override
	public Integer getPaymentDateDelta() {
		return paymentDateDelta;
	}

	@Override
	public void setPaymentDateDelta(Integer paymentDateDelta) {
		this.paymentDateDelta = paymentDateDelta;
	}

	@Override
	public String getPaymentTypeName() {
		return paymentTypeName;
	}

	@Override
	public void setPaymentTypeName(String paymentTypeName) {
		this.paymentTypeName = paymentTypeName;
	}
    
}
