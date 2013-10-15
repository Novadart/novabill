package com.novadart.novabill.shared.client.dto;

public interface IInvoiceDTO extends IAbstractInvoiceDTO {

	public PaymentDateType getPaymentDateGenerator();

	public void setPaymentDateGenerator(PaymentDateType paymentDateGenerator);

	public Integer getPaymentDateDelta();

	public void setPaymentDateDelta(Integer paymentDateDelta);

	public String getPaymentTypeName();

	public void setPaymentTypeName(String paymentTypeName);

}