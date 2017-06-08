package com.example.vanguard.Questions.QuestionTypes;


import android.content.Context;
import android.view.View;

import com.example.vanguard.CustomUIElements.CustomNumberPicker;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.Response;
import com.example.vanguard.Questions.Responses.SimpleResponse;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public class DoubleQuestion extends Question<Integer> {


	private CustomNumberPicker answerUI;

	public DoubleQuestion(Context context, String label) {
		super(label);
		this.answerUI = new CustomNumberPicker(context);
	}

	@Override
	public Response<Integer> getResponse() {
		return new SimpleResponse<Integer>(answerUI.getValue());
	}

	@Override
	public View getAnswerUI() {
		return answerUI;
	}

	@Override
	public ViewStyle getViewStyle() {
		return ViewStyle.SINGLE_LINE;
	}
}
