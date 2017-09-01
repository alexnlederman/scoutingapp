package com.example.vanguard.questions;

/**
 * Created by BertTurtle on 6/11/2017.
 */

public interface Answer<T extends Object> {

	T getValue();

	T getGenericValue();

	void setValue(T newValue);

	int getMatchNumber();

	int getTeamNumber();
}
