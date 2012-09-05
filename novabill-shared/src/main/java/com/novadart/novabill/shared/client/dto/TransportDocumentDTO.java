package com.novadart.novabill.shared.client.dto;

public class TransportDocumentDTO extends AccountingDocumentDTO {
	
	private Integer numberOfPackages;
	
	private AddressDTO fromLocation;
	
	private AddressDTO toLocation;
	
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

}
