package com.novadart.novabill.frontend.client.bridge.server.data;

import java.math.BigDecimal;
import java.util.Date;

import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class AutoBeanInvoiceDTO implements IAutoBeanInvoiceDTO{

	private String paymentTypeName;
	private String paymentDateGenerator;
	private Integer paymentDateDelta;
	private Long id;
	private Long documentID;
	private Date accountingDocumentDate;
	private String note;
	private BigDecimal total;
	private BigDecimal totalTax;
	private BigDecimal totalBeforeTax;
	private BusinessDTO business;
	private String paymentNote;
	private String layoutType;
	private ClientDTO client;
	private Date paymentDueDate;
	private boolean payed;
	protected IAccountingDocumentItemData items;


	@Override
	public String getPaymentDateGenerator() {
		return paymentDateGenerator;
	}

	@Override
	public void setPaymentDateGenerator(String paymentDateGenerator) {
		this.paymentDateGenerator = paymentDateGenerator;
	}

	@Override
	public Integer getPaymentDateDelta() {
		return paymentDateDelta;
	}

	@Override
	public void setPaymentDateDelta(Integer paymentDateDelta) {
		this.paymentDateDelta = paymentDateDelta;
	}

	@Override
	public String getPaymentTypeName() {
		return paymentTypeName;
	}

	@Override
	public void setPaymentTypeName(String paymentTypeName) {
		this.paymentTypeName = paymentTypeName;
	}

	@Override
	public Date getPaymentDueDate() {
		return paymentDueDate;
	}

	@Override
	public void setPaymentDueDate(Date paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}

	@Override
	public boolean getPayed() {
		return payed;
	}

	@Override
	public void setPayed(boolean payed) {
		this.payed = payed;
	}

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
	public String getLayoutType() {
		return layoutType;
	}

	@Override
	public void setLayoutType(String layoutType) {
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
	public void setItems(IAccountingDocumentItemData items) {
		this.items = items;
	}

	@Override
	public IAccountingDocumentItemData getItems() {
		return items;
	}
	
}