package com.example.vanguard.Questions.QuestionTypes;


import android.content.Context;
import android.view.View;

import com.example.vanguard.CustomUIElements.CustomNumberPicker;
import com.example.vanguard.DatabaseManager;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Responses.Response;

/**
 * Created by BertTurtle on 6/5/2017.
 * A question that uses the {@link CustomNumberPicker} for its answer.
 */
public class IntegerQuestion extends Question<Integer> {


	protected CustomNumberPicker answerUI;

	public IntegerQuestion(Context context, String label, AnswerList<Response> responses, String id, boolean isMatchQuestion) {
		super(label, responses, id, isMatchQuestion);
		setup(context);
	}

	public IntegerQuestion(Context context, String label, String id, boolean isMatchQuestion) {
		super(label, id, isMatchQuestion);
		setup(context);
	}

	private void setup(Context context) {
		this.answerUI = new CustomNumberPicker(context);
	}

	@Override
	public Integer getValue() {
		return this.answerUI.getValue();
	}

	@Override
	public View getAnswerUI() {
		return answerUI;
	}

	@Override
	public float convertResponseToNumber(Response<Integer> response) {
		return response.getValue().floatValue();
	}

	@Override
	public ViewStyle getViewStyle() {
		return ViewStyle.SINGLE_LINE;
	}

	@Override
	public Question.QuestionTypes getQuestionType() {
		return Question.QuestionTypes.INTEGER;
	}

	@Override
	public Boolean isEditable() {
		return true;
	}
}
