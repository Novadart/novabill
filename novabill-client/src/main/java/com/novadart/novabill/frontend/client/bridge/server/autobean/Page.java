package com.novadart.novabill.frontend.client.bridge.server.autobean;

import java.util.List;

public interface Page<T> {

	public List<T> getItems();

	public void setItems(List<T> items);

	public Integer getOffset();

	public void setOffset(Integer offset);

	public Integer getLength();

	public void setLength(Integer length);

	public Long getTotal();

	public void setTotal(Long total);
	
}
