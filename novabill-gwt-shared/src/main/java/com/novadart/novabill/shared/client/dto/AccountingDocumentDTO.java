package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.novadart.novabill.shared.client.data.LayoutType;

public class AccountingDocumentDTO implements IsSerializable {
	
	protected Long id;
	
	protected Long documentID;

	protected String documentIDSuffix;
	
	protected Date accountingDocumentDate;
	
    protected String note;
    
    protected BigDecimal total;
    
    protected BigDecimal totalTax;
    
    protected BigDecimal totalBeforeTax;
    
    protected BusinessDTO business;
    
    protected String paymentNote;
    
    protected LayoutType layoutType;

    protected ClientDTO client;
    
    protected EndpointDTO toEndpoint;

    protected List<AccountingDocumentItemDTO> items = new ArrayList<AccountingDocumentItemDTO>();
    
    
	public Long getId() {
		return id;
	}

	
	public void setId(Long id) {
		this.id = id;
	}

	
	public Long getDocumentID() {
		return documentID;
	}

	
	public void setDocumentID(Long documentID) {
		this.documentID = documentID;
	}

	public String getDocumentIDSuffix() {
		return documentIDSuffix;
	}

	public void setDocumentIDSuffix(String documentIDSuffix) {
		this.documentIDSuffix = documentIDSuffix;
	}

	public Date getAccountingDocumentDate() {
		return accountingDocumentDate;
	}

	
	public void setAccountingDocumentDate(Date invoiceDate) {
		this.accountingDocumentDate = invoiceDate;
	}

	
	public String getNote() {
		return note;
	}

	
	public void setNote(String note) {
		this.note = note;
	}
	
	
	public BigDecimal getTotal() {
		return total;
	}

	
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	
	public BigDecimal getTotalTax() {
		return totalTax;
	}

	
	public void setTotalTax(BigDecimal totalTax) {
		this.totalTax = totalTax;
	}

	
	public BigDecimal getTotalBeforeTax() {
		return totalBeforeTax;
	}

	
	public void setTotalBeforeTax(BigDecimal totalBeforeTax) {
		this.totalBeforeTax = totalBeforeTax;
	}
	
	
	public String getPaymentNote() {
		return paymentNote;
	}

	
	public void setPaymentNote(String paymentNote) {
		this.paymentNote = paymentNote;
	}

	
	public LayoutType getLayoutType() {
		return layoutType;
	}

	
	public void setLayoutType(LayoutType layoutType) {
		this.layoutType = layoutType;
	}

	
	public BusinessDTO getBusiness() {
		return business;
	}

	
	public void setBusiness(BusinessDTO business) {
		this.business = business;
	}

	
	public ClientDTO getClient() {
		return client;
	}

	
	public void setClient(ClientDTO client) {
		this.client = client;
	}

	public EndpointDTO getToEndpoint() {
		return toEndpoint;
	}

	public void setToEndpoint(EndpointDTO toEndpoint) {
		this.toEndpoint = toEndpoint;
	}
	
	public List<AccountingDocumentItemDTO> getItems() {
		return items;
	}

	
	public void setItems(List<AccountingDocumentItemDTO> items) {
		this.items = items;
	}

}
