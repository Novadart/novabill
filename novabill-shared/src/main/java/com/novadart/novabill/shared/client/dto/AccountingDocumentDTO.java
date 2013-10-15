package com.novadart.novabill.shared.client.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.novadart.novabill.shared.client.data.LayoutType;

public class AccountingDocumentDTO implements IsSerializable, IAccountingDocumentDTO{
	
	protected Long id;
	
	protected Long documentID;
	
	protected Date accountingDocumentDate;
	
    protected String note;
    
    protected BigDecimal total;
    
    protected BigDecimal totalTax;
    
    protected BigDecimal totalBeforeTax;
    
    protected BusinessDTO business;
    
    protected String paymentNote;
    
    protected LayoutType layoutType = LayoutType.TIDY;

    protected ClientDTO client;

    protected List<AccountingDocumentItemDTO> items = new ArrayList<AccountingDocumentItemDTO>();
    
    @Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getDocumentID() {
		return documentID;
	}

	@Override
	public void setDocumentID(Long documentID) {
		this.documentID = documentID;
	}

	@Override
	public Date getAccountingDocumentDate() {
		return accountingDocumentDate;
	}

	@Override
	public void setAccountingDocumentDate(Date invoiceDate) {
		this.accountingDocumentDate = invoiceDate;
	}

	@Override
	public String getNote() {
		return note;
	}

	@Override
	public void setNote(String note) {
		this.note = note;
	}
	
	@Override
	public BigDecimal getTotal() {
		return total;
	}

	@Override
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	@Override
	public BigDecimal getTotalTax() {
		return totalTax;
	}

	@Override
	public void setTotalTax(BigDecimal totalTax) {
		this.totalTax = totalTax;
	}

	@Override
	public BigDecimal getTotalBeforeTax() {
		return totalBeforeTax;
	}

	@Override
	public void setTotalBeforeTax(BigDecimal totalBeforeTax) {
		this.totalBeforeTax = totalBeforeTax;
	}
	
	@Override
	public String getPaymentNote() {
		return paymentNote;
	}

	@Override
	public void setPaymentNote(String paymentNote) {
		this.paymentNote = paymentNote;
	}

	@Override
	public LayoutType getLayoutType() {
		return layoutType;
	}

	@Override
	public void setLayoutType(LayoutType layoutType) {
		this.layoutType = layoutType;
	}

	@Override
	public BusinessDTO getBusiness() {
		return business;
	}

	@Override
	public void setBusiness(BusinessDTO business) {
		this.business = business;
	}

	@Override
	public ClientDTO getClient() {
		return client;
	}

	@Override
	public void setClient(ClientDTO client) {
		this.client = client;
	}

	@Override
	public List<AccountingDocumentItemDTO> getItems() {
		return items;
	}

	@Override
	public void setItems(List<AccountingDocumentItemDTO> items) {
		this.items = items;
	}

}
