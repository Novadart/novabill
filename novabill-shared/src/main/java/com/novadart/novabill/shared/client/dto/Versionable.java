package com.novadart.novabill.shared.client.dto;

public interface Versionable {
	
	public Long getId();
	
	public void setId(Long id);

	public Integer getVersion();
	
	public void setVersion(Integer version);
	
}
