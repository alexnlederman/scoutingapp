package com.example.vanguard.Questions.QuestionTypes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanguard.CustomUIElements.AnswerUIEditText;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Responses.Response;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public class StringQuestion extends Question<String> {

	private AnswerUIEditText answerUI;

	public StringQuestion(Context context, String label, AnswerList<Response> responseList, String id, boolean isMatchQuestion) {
		super(label, responseList, id, isMatchQuestion, ViewStyle.TWO_LINE, QuestionType.STRING, true, "", new TreeMap<String, Object>());
		setup(context);
	}

	private void setup(Context context) {
		this.answerUI = new AnswerUIEditText(context);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		this.answerUI.setLayoutParams(params);
	}

	@Override
	public String getValue() {
		return this.answerUI.getText().toString();
	}

	@Override
	public void setValue(String newValue) {
		this.answerUI.setValue(newValue);
	}

	@Override
	public AnswerUIEditText getAnswerUI() {
		return this.answerUI;
	}

	@Override
	public float convertResponseToNumber(Response<String> response) {
		return 1;
	}
}
