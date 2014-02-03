package com.novadart.novabill.report;

import com.novadart.novabill.domain.TransportDocument;


public class TransportDetails {
	
	private String transportationResponsibility;
	
	private String tradeZone;
	
	private String cause;
	
	private String numberOfPackages;
	
	private String totalWeight;
	
	public TransportDetails(TransportDocument transDoc){
		this.transportationResponsibility = transDoc.getTransportationResponsibility();
		this.tradeZone = transDoc.getTradeZone();
		this.cause = transDoc.getCause();
		this.numberOfPackages = transDoc.getNumberOfPackages();
		this.totalWeight = transDoc.getTotalWeight();
	}

	public String getTransportationResponsibility() {
		return transportationResponsibility;
	}

	public String getTradeZone() {
		return tradeZone;
	}

	public String getCause() {
		return cause;
	}

	public String getNumberOfPackages() {
		return numberOfPackages;
	}

	public String getTotalWeight() {
		return totalWeight;
	}
	
}
