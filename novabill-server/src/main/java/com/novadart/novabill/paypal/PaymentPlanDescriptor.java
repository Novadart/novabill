package com.novadart.novabill.paypal;

import java.math.BigDecimal;

public class PaymentPlanDescriptor {

	private String hostedButtonID;
	
	private BigDecimal totalAfterTax;
	
	private BigDecimal tax;
	
	private BigDecimal totalWithoutTax;
	
	private BigDecimal discount;
	
	private BigDecimal totalTax;
	
	private String itemName;
	
	private short payedPeriodInMonths;
	
	public PaymentPlanDescriptor(){}
	
	public PaymentPlanDescriptor(String hostedButtonID, BigDecimal totalAfterTax, String itemName, short payedPeriodInMonths) {
		this.hostedButtonID = hostedButtonID;
		this.totalAfterTax = totalAfterTax;
		this.itemName = itemName;
		this.payedPeriodInMonths = payedPeriodInMonths;
	}

	public String getHostedButtonID() {
		return hostedButtonID;
	}

	public BigDecimal getTotalAfterTax() {
		return totalAfterTax;
	}

	public String getItemName() {
		return itemName;
	}

	public short getPayedPeriodInMonths() {
		return payedPeriodInMonths;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public BigDecimal getTotalWithoutTax() {
		return totalWithoutTax;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public BigDecimal getTotalTax() {
		return totalTax;
	}
	
}
