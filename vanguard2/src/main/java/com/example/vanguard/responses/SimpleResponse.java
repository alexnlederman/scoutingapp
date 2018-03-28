package com.example.vanguard.responses;


import android.util.Log;

import java.util.Objects;

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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Response) {
			// This is overly complicated but it works so I ain't changing it.
			if (((Response) obj).getValue().equals(this.getValue())) {
				if (((Response) obj).getTeamNumber() == this.getTeamNumber()) {
					if (((Response) obj).getMatchNumber() == this.getMatchNumber()) {
						if (((Response) obj).getEventKey().equals(this.getEventKey())) {
							return true;
						}
					}
				}
			}
			return false;
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return this.getEventKey() + " " + this.getValue() + " " + this.getMatchNumber() + " " + this.getTeamNumber();
	}
}
