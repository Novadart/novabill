package com.novadart.novabill.shared.client.exception;

public class PremiumUpgradeException extends Exception {

	private static final long serialVersionUID = 8413524665681339386L;
	
	private String username;
	
	private String transactionID;

	
	public PremiumUpgradeException() {
		super();
	}

	public PremiumUpgradeException(Throwable cause) {
		super(cause);
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

}
