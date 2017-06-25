package com.example.vanguard.Questions.QuestionTypes;


import android.content.Context;
import android.view.View;

import com.example.vanguard.CustomUIElements.CustomNumberPicker;
import com.example.vanguard.DatabaseManager;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.ResponseList;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public class IntegerQuestion extends Question<Integer> {


	protected CustomNumberPicker answerUI;

	public IntegerQuestion(Context context, String label, ResponseList responses, String id) {
		super(label, responses, id);
		setup(context);
	}

	public IntegerQuestion(Context context, String label, String id) {
		super(label, id);
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
	public ViewStyle getViewStyle() {
		return ViewStyle.SINGLE_LINE;
	}

	@Override
	public DatabaseManager.QuestionTypes getQuestionType() {
		return DatabaseManager.QuestionTypes.INTEGER;
	}

	@Override
	public Boolean isEditable() {
		return true;
	}
}
