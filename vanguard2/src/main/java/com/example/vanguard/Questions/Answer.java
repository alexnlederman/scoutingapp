package com.example.vanguard.Questions;

/**
 * Created by BertTurtle on 6/11/2017.
 */

public interface Answer<T extends Object> {

	T getValue();

	int getMatchNumber();

	int getTeamNumber();

	void setValue(T newValue);

}
