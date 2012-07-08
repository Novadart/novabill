package com.novadart.novabill.shared.client.dto;

import java.util.List;

public class PageDTO<T> {
	
	private List<T> items;
	
	private Integer offset;
	
	private Integer length;
	
	private Integer total;
	
	public PageDTO(){}
	
	public PageDTO(List<T> items, Integer offset, Integer length, Integer total) {
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

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
