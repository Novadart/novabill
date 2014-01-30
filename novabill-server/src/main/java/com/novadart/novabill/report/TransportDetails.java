package com.novadart.novabill.report;

import java.math.BigDecimal;

import com.novadart.novabill.domain.TransportDocument;


public class TransportDetails {
	
	private String transportationResponsibility;
	
	private String tradeZone;
	
	private String cause;
	
	private String numberOfPackages;
	
	private BigDecimal totalWeight;
	
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

	public BigDecimal getTotalWeight() {
		return totalWeight;
	}
	
}
