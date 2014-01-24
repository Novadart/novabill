package com.novadart.novabill.frontend.client.bridge.server.autobean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


import com.novadart.novabill.shared.client.data.LayoutType;

public interface TransportDocument {
	
	public String getNumberOfPackages();

	public void setNumberOfPackages(String numberOfPackages);

	public EndPoint getFromEndpoint();

	public void setFromEndpoint(EndPoint fromEndpoint);

	public EndPoint getToEndpoint();

	public void setToEndpoint(EndPoint toEndpoint);

	public String getTransporter();

	public void setTransporter(String transporter);

	public String getTransportationResponsibility();

	public void setTransportationResponsibility(String transportationResponsibility);

	public String getTradeZone();

	public void setTradeZone(String tradeZone);

	public Date getTransportStartDate();

	public void setTransportStartDate(Date transportStartDate);

	public String getCause();

	public void setCause(String cause);
	
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
