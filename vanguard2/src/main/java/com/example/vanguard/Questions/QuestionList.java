package com.example.vanguard.Questions;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BertTurtle on 6/1/2017.
 */

public class QuestionList<T extends Object> extends ArrayList<Question<? extends T>> {

	public QuestionList<T> getAllOfType(Class type) {
		QuestionList<T> questions = new QuestionList<T>();
		for (Question<? extends T> question : this) {
			if (type.isAssignableFrom(question.getResponse().getResponseValue().getClass())) {
				questions.add(question);
			}
		}
		return questions;
	}
}
