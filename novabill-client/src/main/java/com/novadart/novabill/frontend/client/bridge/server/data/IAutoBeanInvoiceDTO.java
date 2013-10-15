package com.novadart.novabill.frontend.client.bridge.server.data;

import java.math.BigDecimal;
import java.util.Date;

import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public interface IAutoBeanInvoiceDTO {

	public String getPaymentDateGenerator();

	public void setPaymentDateGenerator(String paymentDateGenerator);

	public Integer getPaymentDateDelta();

	public void setPaymentDateDelta(Integer paymentDateDelta);

	public String getPaymentTypeName();

	public void setPaymentTypeName(String paymentTypeName);

	public Date getPaymentDueDate();

	public void setPaymentDueDate(Date paymentDueDate);

	public boolean getPayed();

	public void setPayed(boolean payed);

	public Long getId();

	public void setId(Long id);

	public Long getDocumentID();

	public void setDocumentID(Long documentID);

	public Date getAccountingDocumentDate();

	public void setAccountingDocumentDate(Date invoiceDate);

	public String getNote();

	public void setNote(String note);

	public BigDecimal getTotal();

	public void setTotal(BigDecimal total);

	public BigDecimal getTotalTax();

	public void setTotalTax(BigDecimal totalTax);

	public BigDecimal getTotalBeforeTax();

	public void setTotalBeforeTax(BigDecimal totalBeforeTax);

	public String getPaymentNote();

	public void setPaymentNote(String paymentNote);

	public String getLayoutType();

	public void setLayoutType(String layoutType);

	public BusinessDTO getBusiness();

	public void setBusiness(BusinessDTO business);

	public ClientDTO getClient();

	public void setClient(ClientDTO client);

	public void setItems(IAccountingDocumentItemData items);

	public IAccountingDocumentItemData getItems();

}