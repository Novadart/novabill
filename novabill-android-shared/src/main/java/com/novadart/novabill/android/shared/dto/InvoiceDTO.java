package com.novadart.novabill.android.shared.dto;


public class InvoiceDTO extends AbstractInvoiceDTO {
	
	private String paymentTypeName;
    
    private PaymentDateType paymentDateGenerator;
    
    private Integer paymentDateDelta;
    
    
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

	
	public String getPaymentTypeName() {
		return paymentTypeName;
	}

	
	public void setPaymentTypeName(String paymentTypeName) {
		this.paymentTypeName = paymentTypeName;
	}
    
}
