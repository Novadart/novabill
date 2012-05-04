package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.gwt.user.client.rpc.IsSerializable;

public class AccountingDocumentDTO implements IsSerializable{
	
	protected Long id;
	
	protected Long documentID;
	
	protected Date invoiceDate;
	
    protected String note;
    
    protected BigDecimal total;
    
    protected BigDecimal totalTax;
    
    protected BigDecimal totalBeforeTax;

    protected BusinessDTO business;

    protected ClientDTO client;

    protected List<InvoiceItemDTO> items = new ArrayList<InvoiceItemDTO>();
    
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

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
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

	public List<InvoiceItemDTO> getItems() {
		return items;
	}

	public void setItems(List<InvoiceItemDTO> items) {
		this.items = items;
	}

}
