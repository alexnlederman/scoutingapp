package com.example.vanguard.Questions;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.example.vanguard.Responses.Response;

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

	public AnswerList<T> getAllNotOfType(Class type) {
		AnswerList<T> answers = new AnswerList<T>();
		for (T answer : this) {
			if (!type.isAssignableFrom(answer.getValue().getClass())) {
				answers.add(answer);
			}
		}
		return answers;
	}

	public boolean hasOfType(Class type) {
		for (T answer : this) {
			if (type.isAssignableFrom(type)) {
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
}
