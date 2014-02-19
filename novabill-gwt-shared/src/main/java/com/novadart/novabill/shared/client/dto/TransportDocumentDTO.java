package com.novadart.novabill.shared.client.dto;

import java.util.Date;

public class TransportDocumentDTO extends AccountingDocumentDTO {
	
	private String numberOfPackages;
	
	private EndpointDTO fromEndpoint;
	
	private String transporter;
	
	private String transportationResponsibility;
	
	private String tradeZone;
	
	private Date transportStartDate;
	
	private String cause;
	
	private String totalWeight;
	
	private String appearanceOfTheGoods;
	
	private Long invoice;
	
	public String getNumberOfPackages() {
		return numberOfPackages;
	}

	public void setNumberOfPackages(String numberOfPackages) {
		this.numberOfPackages = numberOfPackages;
	}

	public EndpointDTO getFromEndpoint() {
		return fromEndpoint;
	}

	public void setFromEndpoint(EndpointDTO fromEndpoint) {
		this.fromEndpoint = fromEndpoint;
	}


	public String getTransporter() {
		return transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public String getTransportationResponsibility() {
		return transportationResponsibility;
	}

	public void setTransportationResponsibility(String transportationResponsibility) {
		this.transportationResponsibility = transportationResponsibility;
	}

	public String getTradeZone() {
		return tradeZone;
	}

	public void setTradeZone(String tradeZone) {
		this.tradeZone = tradeZone;
	}

	public Date getTransportStartDate() {
		return transportStartDate;
	}

	public void setTransportStartDate(Date transportStartDate) {
		this.transportStartDate = transportStartDate;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(String totalWeight) {
		this.totalWeight = totalWeight;
	}

	public Long getInvoice() {
		return invoice;
	}

	public void setInvoice(Long invoice) {
		this.invoice = invoice;
	}

	public String getAppearanceOfTheGoods() {
		return appearanceOfTheGoods;
	}

	public void setAppearanceOfTheGoods(String appearanceOfTheGoods) {
		this.appearanceOfTheGoods = appearanceOfTheGoods;
	}
}
