package com.example.vanguard.questions.question_types;

import android.content.Context;
import android.view.ViewGroup;

import com.example.vanguard.custom_ui_elements.answer_ui_elements.AnswerUIEditText;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.example.vanguard.responses.Response;

import java.util.TreeMap;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public class StringQuestion extends Question<String> {

	private AnswerUIEditText answerUI;

	public StringQuestion(Context context, String label, AnswerList<Response> responseList, String id, boolean isMatchQuestion) {
		super(label, responseList, id, isMatchQuestion, ViewStyle.TWO_LINE, QuestionType.STRING, true, "", new TreeMap<QuestionPropertyDescription, Object>());
		setup(context);
	}

	private void setup(Context context) {
		this.answerUI = new AnswerUIEditText(context);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		this.answerUI.setLayoutParams(params);
	}

	@Override
	public AnswerUIEditText getAnswerUI() {
		return this.answerUI;
	}

	@Override
	public float convertResponseToNumber(Response<String> response) {
		return 1;
	}

	@Override
	public String getGenericValue() {
		return "HI";
	}
}
