package com.example.vanguard.responses;


/**
 * Created by BertTurtle on 6/5/2017.
 */

public class SimpleResponse<T> implements Response<T> {

	private T responseValue;
	private int matchNumber;
	private int teamNumber;
	private boolean isPracticeMatchResponse;
	private String eventKey;

	public SimpleResponse(T value, int matchNumber, int teamNumber, boolean isPracticeMatchResponse, String eventKey) {
		this.responseValue = value;
		this.matchNumber = matchNumber;
		this.teamNumber = teamNumber;
		this.isPracticeMatchResponse = isPracticeMatchResponse;
		this.eventKey = eventKey;
	}

	@Override
	public T getValue() {
		return this.responseValue;
	}

	@Override
	public T getGenericValue() {
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

	@Override
	public void setValue(T newValue) {
		this.responseValue = newValue;
	}

	@Override
	public boolean isPracticeMatchResponse() {
		return isPracticeMatchResponse;
	}

	@Override
	public String getEventKey() {
		return eventKey;
	}
}
