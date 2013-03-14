package com.novadart.novabill.shared.client.tuple;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Triple<T1 extends IsSerializable, T2 extends IsSerializable, T3 extends IsSerializable> implements IsSerializable {

	private T1 first;
	
	private T2 second;
	
	private T3 third;

	public Triple(T1 first, T2 second, T3 third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public T1 getFirst() {
		return first;
	}

	public T2 getSecond() {
		return second;
	}

	public T3 getThird() {
		return third;
	}
	
}
