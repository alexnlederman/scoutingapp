package com.example.vanguard.Responses;

import com.example.vanguard.Questions.Question;

import java.io.Serializable;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public class SimpleMatchResponse<T> implements Response<T> {

	private T responseValue;
	private int matchNumber;


	// TODO work out alternate identifier rather than question. It skrews with serializability.
	public SimpleMatchResponse(T value, int matchNumber) {
		this.responseValue = value;
		this.matchNumber = matchNumber;
	}

	@Override
	public T getValue() {
		return this.responseValue;
	}

	public int getMatchNumber() {
		return this.matchNumber;
	}
}
