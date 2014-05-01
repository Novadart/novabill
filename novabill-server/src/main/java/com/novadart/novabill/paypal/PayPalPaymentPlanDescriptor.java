package com.novadart.novabill.paypal;

import java.math.BigDecimal;

public class PayPalPaymentPlanDescriptor {

	private String hostedButtonID;
	
	private BigDecimal mcGross;
	
	private String itemName;
	
	private short payedPeriodInMonths;
	
	public PayPalPaymentPlanDescriptor(){}
	
	public PayPalPaymentPlanDescriptor(String hostedButtonID, BigDecimal mcGross, String itemName, short payedPeriodInMonths) {
		this.hostedButtonID = hostedButtonID;
		this.mcGross = mcGross;
		this.itemName = itemName;
		this.payedPeriodInMonths = payedPeriodInMonths;
	}

	public String getHostedButtonID() {
		return hostedButtonID;
	}

	public BigDecimal getMcGross() {
		return mcGross;
	}

	public String getItemName() {
		return itemName;
	}

	public short getPayedPeriodInMonths() {
		return payedPeriodInMonths;
	}
	
}
