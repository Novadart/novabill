package com.novadart.novabill.shared.client.dto;

public class EstimationDTO extends AccountingDocumentDTO {
	
	private String limitations;

	public String getLimitations() {
		return limitations;
	}

	public void setLimitations(String limitations) {
		this.limitations = limitations;
	}

}
