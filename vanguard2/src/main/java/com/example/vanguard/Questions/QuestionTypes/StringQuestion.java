package com.example.vanguard.Questions.QuestionTypes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionList;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public class StringQuestion extends Question<String> {

	private EditText answerUI;

	public StringQuestion(HashMap<String, Object> hashMap, Context context) {
		super(hashMap);

		this.answerUI = new EditText(context);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		this.answerUI.setLayoutParams(params);
	}


	@Override
	public String getValue() {
		return this.answerUI.getText().toString();
	}

	@Override
	public View getAnswerUI() {
		return this.answerUI;
	}

	@Override
	public ViewStyle getViewStyle() {
		return ViewStyle.TWO_LINE;
	}

	@Override
	public QuestionList.QuestionTypes getQuestionType() {
		return QuestionList.QuestionTypes.STRING;
	}

	@Override
	public Boolean isEditable() {
		return true;
	}
}
