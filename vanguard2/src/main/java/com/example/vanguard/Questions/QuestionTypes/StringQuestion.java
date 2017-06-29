package com.example.vanguard.Questions.QuestionTypes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.vanguard.CustomUIElements.AnswerUIEditText;
import com.example.vanguard.DatabaseManager;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.ResponseList;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public class StringQuestion extends Question<String> {

	private AnswerUIEditText answerUI;

	public StringQuestion(Context context, String label, ResponseList responseList, String id, boolean isMatchQuestion) {
		super(label, responseList, id, isMatchQuestion);
		setup(context);
	}

	public StringQuestion(Context context, String label, String id, boolean isMatchQuestion) {
		super(label, id, isMatchQuestion);
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
	public View getAnswerUI() {
		return this.answerUI;
	}

	@Override
	public ViewStyle getViewStyle() {
		return ViewStyle.TWO_LINE;
	}

	@Override
	public DatabaseManager.QuestionTypes getQuestionType() {
		return DatabaseManager.QuestionTypes.STRING;
	}

	@Override
	public Boolean isEditable() {
		return true;
	}
}
