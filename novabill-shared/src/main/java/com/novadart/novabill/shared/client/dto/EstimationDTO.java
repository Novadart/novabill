package com.novadart.novabill.shared.client.dto;

import java.util.Date;

public class EstimationDTO extends AccountingDocumentDTO {
	
	private String limitations;
	
	private Date validTill;

	public String getLimitations() {
		return limitations;
	}

	public void setLimitations(String limitations) {
		this.limitations = limitations;
	}

	public Date getValidTill() {
		return validTill;
	}

	public void setValidTill(Date validTill) {
		this.validTill = validTill;
	}

}
