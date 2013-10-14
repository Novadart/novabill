package com.novadart.novabill.shared.client.dto;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PageDTO<T> implements IsSerializable, IPageDTO<T> {
	
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

	@Override
	public List<T> getItems() {
		return items;
	}

	@Override
	public void setItems(List<T> items) {
		this.items = items;
	}

	@Override
	public Integer getOffset() {
		return offset;
	}

	@Override
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	@Override
	public Integer getLength() {
		return length;
	}

	@Override
	public void setLength(Integer length) {
		this.length = length;
	}

	@Override
	public Long getTotal() {
		return total;
	}

	@Override
	public void setTotal(Long total) {
		this.total = total;
	}

}
