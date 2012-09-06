package com.novadart.novabill.shared.client.dto;

public class TransportDocumentDTO extends AccountingDocumentDTO {
	
	private Integer numberOfPackages;
	
	private AddressDTO fromLocation;
	
	private AddressDTO toLocation;
	
	private String transporter;
	
	private String transportationResponsibility;
	
	private String tradeZone;
	
	public Integer getNumberOfPackages() {
		return numberOfPackages;
	}

	public void setNumberOfPackages(Integer numberOfPackages) {
		this.numberOfPackages = numberOfPackages;
	}

	public AddressDTO getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(AddressDTO fromLocation) {
		this.fromLocation = fromLocation;
	}

	public AddressDTO getToLocation() {
		return toLocation;
	}

	public void setToLocation(AddressDTO toLocation) {
		this.toLocation = toLocation;
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

}
