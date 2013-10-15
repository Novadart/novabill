package com.novadart.novabill.shared.client.dto;

import java.util.Date;

public interface IAbstractInvoiceDTO extends IAccountingDocumentDTO {

	public Date getPaymentDueDate();

	public void setPaymentDueDate(Date paymentDueDate);

	public boolean getPayed();

	public void setPayed(boolean payed);

}