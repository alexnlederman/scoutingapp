package com.example.vanguard.Questions.QuestionTypes;


import android.content.Context;
import android.view.View;

import com.example.vanguard.CustomUIElements.CustomNumberPicker;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionList;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public class IntegerQuestion extends Question<Integer> {


	protected CustomNumberPicker answerUI;

	public IntegerQuestion(HashMap<String, Object> hashMap, Context context) {
		super(hashMap);
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
	public QuestionList.QuestionTypes getQuestionType() {
		return QuestionList.QuestionTypes.INTEGER;
	}

	@Override
	public Boolean isEditable() {
		return true;
	}
}
