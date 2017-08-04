package com.example.vanguard.Questions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BertTurtle on 6/11/2017.
 */

public class AnswerList<T extends Answer> extends ArrayList<T> {

	public AnswerList() {
		super();
	}

	public AnswerList(List<T> arrayList) {
		super(arrayList);
	}

	public AnswerList<T> getAllOfType(Class type) {
		AnswerList<T> answers = new AnswerList<T>();
		for (T answer : this) {
			if (type.isInstance(answer.getValue())) {
				answers.add(answer);
			}
		}
		return answers;
	}

	public AnswerList<T> getAllNotOfType(Class type) {
		AnswerList<T> answers = new AnswerList<T>();
		for (T answer : this) {
			if (!type.isInstance(answer.getValue())) {
				answers.add(answer);
			}
		}
		return answers;
	}

	public boolean hasOfType(Class type) {
		for (T answer : this) {
			if (answer.getValue().getClass().isInstance(type)) {
				return true;
			}
		}
		return false;
	}

	public AnswerList<T> getMatchAnswers(int matchNumber) {
		AnswerList<T> matchAnswers = new AnswerList<>();
		for (T answer : this) {
			if (answer.getMatchNumber() == matchNumber) {
				matchAnswers.add(answer);
			}
		}
		return matchAnswers;
	}

	public AnswerList<T> getTeamAnswers(int teamNumber) {
		AnswerList<T> teamAnswers = new AnswerList<>();
		for (T answer : this) {
			if (answer.getTeamNumber() == teamNumber) {
				teamAnswers.add(answer);
			}
		}
		return teamAnswers;
	}

	public Question getQuestionById(String id) {
		for (T answer : this) {
			if (answer instanceof Question) {
				if (((Question) answer).getID().equals(id)) {
					return (Question) answer;
				}
			}
		}
		return null;
	}
}
