package com.example.vanguard.Questions.QuestionTypes;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by BertTurtle on 6/11/2017.
 */

public class TeamNumberQuestion extends IntegerQuestion {
	public TeamNumberQuestion(HashMap<String, Object> hashMap, Context context) {
		super(hashMap, context);
		this.setLabel("Team Number?");
	}

	public TeamNumberQuestion(HashMap<String, Object> hashMap, Context context, int startingValue) {
		this(hashMap, context);
		this.answerUI.setValue(startingValue);
	}

	@Override
	public Boolean isEditable() {
		return false;
	}
}
