package com.example.vanguard.Questions.QuestionTypes;

import android.content.Context;

import com.example.vanguard.Responses.SimpleMatchResponse;

import java.util.HashMap;

/**
 * Created by BertTurtle on 6/11/2017.
 */

public class MatchNumberQuestion extends IntegerQuestion {
	public MatchNumberQuestion(HashMap<String, Object> hashMap, Context context) {
		super(hashMap, context);
		this.setLabel("Match Number?");
	}

	public MatchNumberQuestion(HashMap<String, Object> hashMap, Context context, int startingValue) {
		this(hashMap, context);
		this.answerUI.setValue(startingValue);
	}

	@Override
	public void saveResponse() {
		this.setMatchNumber(this.getValue());
		super.saveResponse();
	}

	@Override
	public Boolean isEditable() {
		return false;
	}
}
