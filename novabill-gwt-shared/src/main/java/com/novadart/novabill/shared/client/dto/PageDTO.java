package com.novadart.novabill.shared.client.dto;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PageDTO<T> implements IsSerializable {
	
	private List<T> items;
	
	private Integer offset;
	
	private Integer length;
	
	private Long total;
	
	public PageDTO(){}
	
	public PageDTO(List<T> items, Integer offset, Integer length, Long total) {
		this.items = items;
		this.offset = offset;
		this.length = length;
		this.total = total;
	}

	
	public List<T> getItems() {
		return items;
	}

	
	public void setItems(List<T> items) {
		this.items = items;
	}

	
	public Integer getOffset() {
		return offset;
	}

	
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	
	public Integer getLength() {
		return length;
	}

	
	public void setLength(Integer length) {
		this.length = length;
	}

	
	public Long getTotal() {
		return total;
	}

	
	public void setTotal(Long total) {
		this.total = total;
	}

}
