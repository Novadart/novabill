package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.novadart.novabill.shared.client.data.LayoutType;

public interface IAccountingDocumentDTO {

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

	public LayoutType getLayoutType();

	public void setLayoutType(LayoutType layoutType);

	public BusinessDTO getBusiness();

	public void setBusiness(BusinessDTO business);

	public ClientDTO getClient();

	public void setClient(ClientDTO client);

	public List<AccountingDocumentItemDTO> getItems();

	public void setItems(List<AccountingDocumentItemDTO> items);

}