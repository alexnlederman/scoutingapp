package com.example.vanguard.Questions.Responses;

import com.example.vanguard.Questions.Response;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public class SimpleResponse<T> implements Response<T> {

	public T responseValue;

	public SimpleResponse(T value) {
		this.responseValue = value;
	}

	@Override
	public T getResponseValue() {
		return this.responseValue;
	}
}
