package com.example.vanguard.Responses;

/**
 * Created by BertTurtle on 6/14/2017.
 */

public class SimplePitResponse<T> implements Response<T> {

	T responseValue;

	public SimplePitResponse(T value) {
		this.responseValue = value;
	}

	@Override
	public T getValue() {
		return this.responseValue;
	}
}
