package com.example.vanguard.Responses;

import com.example.vanguard.Questions.Question;

import java.io.Serializable;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public class SimpleResponse<T> implements Response<T> {

	private T responseValue;
	private int matchNumber;
	private int teamNumber;

	public SimpleResponse(T value, int matchNumber, int teamNumber) {
		this.responseValue = value;
		this.matchNumber = matchNumber;
		this.teamNumber = teamNumber;
	}

	@Override
	public T getValue() {
		return this.responseValue;
	}

	@Override
	public int getMatchNumber() {
		return this.matchNumber;
	}

	@Override
	public int getTeamNumber() {
		return teamNumber;
	}
}
