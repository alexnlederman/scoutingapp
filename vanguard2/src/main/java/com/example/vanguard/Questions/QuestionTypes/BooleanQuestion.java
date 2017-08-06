package com.example.vanguard.Questions.QuestionTypes;

import android.content.Context;
import android.view.View;

import com.example.vanguard.CustomUIElements.AnswerUIBooleanRadioButtons;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Responses.Response;

import java.util.TreeMap;

/**
 * Created by mbent on 8/5/2017.
 */

public class BooleanQuestion extends Question<Boolean> {

	private AnswerUIBooleanRadioButtons answerUI;

	public BooleanQuestion(Context context, String label, AnswerList<Response> responses, String id, boolean isMatchQuestion) {
		super(label, responses, id, isMatchQuestion, ViewStyle.SINGLE_LINE, QuestionType.BOOLEAN, true, null, new TreeMap<String, Object>());
		this.answerUI = new AnswerUIBooleanRadioButtons(context);
		if (isMatchQuestion) {
			this.setValue(false);
		}
	}

	@Override
	public Boolean getValue() {
		return this.answerUI.getValue();
	}

	@Override
	public void setValue(Boolean newValue) {
		this.answerUI.setValue(newValue);
	}

	@Override
	public View getAnswerUI() {
		return this.answerUI;
	}

	@Override
	public float convertResponseToNumber(Response<Boolean> response) {
		if (response.getValue()) {
			return 1;
		}
		return 0;
	}
}
