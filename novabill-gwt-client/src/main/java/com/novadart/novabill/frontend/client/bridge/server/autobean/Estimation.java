package com.novadart.novabill.frontend.client.bridge.server.autobean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.novadart.novabill.shared.client.data.LayoutType;

public interface Estimation {
	
	
	public String getLimitations();

	public void setLimitations(String limitations);

	public String getTermsAndConditions();

	public void setTermsAndConditions(String limitations);
	
	public Date getValidTill();

	public void setValidTill(Date validTill);
	
	/*
	 * AccountingDocumentDTO interface
	 */
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

	public Business getBusiness();

	public void setBusiness(Business business);

	public Client getClient();

	public void setClient(Client client);

	public List<AccountingDocumentItem> getItems();

	public void setItems(List<AccountingDocumentItem> items);

}
