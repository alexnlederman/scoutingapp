package com.example.vanguard.Questions.Properties;

/**
 * Created by mbent on 7/13/2017.
 */

public class SimpleQuestionProperty<T> implements QuestionProperty<T> {

	T value;
	String name;

	public SimpleQuestionProperty(T value, String name) {
		this.value = value;
		this.name = name;
	}

	@Override
	public T getValue() {
		return this.value;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setValue(T newValue) {
		this.value = newValue;
	}
}
