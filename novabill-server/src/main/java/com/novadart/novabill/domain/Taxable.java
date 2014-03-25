package com.novadart.novabill.domain;

public interface Taxable {
	
	public Long getId();
	
	public String getVatID();
    
    public void setVatID(String vatID);
    
    public String getSsn();
    
    public void setSsn(String ssn);
    
    public Taxable findByVatID(String vatID);

}
