package com.novadart.novabill.shared.client.dto;

import java.util.List;

public interface IPageDTO<T> {

	public abstract List<T> getItems();

	public abstract void setItems(List<T> items);

	public abstract Integer getOffset();

	public abstract void setOffset(Integer offset);

	public abstract Integer getLength();

	public abstract void setLength(Integer length);

	public abstract Long getTotal();

	public abstract void setTotal(Long total);

}