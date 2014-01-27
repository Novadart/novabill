package com.novadart.novabill.shared.client.dto;

import java.util.Date;

public class TransportDocumentDTO extends AccountingDocumentDTO {
	
	private String numberOfPackages;
	
	private EndpointDTO fromEndpoint;
	
	private EndpointDTO toEndpoint;
	
	private String transporter;
	
	private String transportationResponsibility;
	
	private String tradeZone;
	
	private Date transportStartDate;
	
	private String cause;
	
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

	public EndpointDTO getToEndpoint() {
		return toEndpoint;
	}

	public void setToEndpoint(EndpointDTO toEndpoint) {
		this.toEndpoint = toEndpoint;
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

	public Long getInvoice() {
		return invoice;
	}

	public void setInvoice(Long invoice) {
		this.invoice = invoice;
	}

}
