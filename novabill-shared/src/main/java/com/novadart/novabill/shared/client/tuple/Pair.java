package com.novadart.novabill.shared.client.tuple;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Pair<T1, T2> implements IsSerializable{
	
	private T1 first;
	
	private T2 second;

	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}

	public T1 getFirst() {
		return first;
	}

	public T2 getSecond() {
		return second;
	}
	
	

}
