package com.example.vanguard.Questions.Properties;

/**
 * Created by mbent on 7/13/2017.
 */

public interface QuestionProperty<T extends Object> {

	T getValue();

	String getName();

	void setValue(T newValue);

}
