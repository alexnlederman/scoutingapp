package com.example.vanguard.Questions;

import java.util.ArrayList;

/**
 * Created by BertTurtle on 6/11/2017.
 */

public class AnswerList<T extends Answer> extends ArrayList<T> {

	public AnswerList() {
		super();
	}

	public AnswerList(ArrayList<T> arrayList) {
		super(arrayList);
	}

	public AnswerList<T> getAllOfType(Class type) {
		AnswerList<T> answers = new AnswerList<T>();
		for (T answer : this) {
			if (type.isAssignableFrom(answer.getValue().getClass())) {
				answers.add(answer);
			}
		}
		return answers;
	}
}
