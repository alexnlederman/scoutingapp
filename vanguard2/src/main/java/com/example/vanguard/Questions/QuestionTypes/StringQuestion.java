package com.example.vanguard.Questions.QuestionTypes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.Response;
import com.example.vanguard.Questions.Responses.SimpleResponse;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public class StringQuestion extends Question<String> {

	private EditText answerUI;

	public StringQuestion(Context context, String label) {
		super(label);

		this.answerUI = new EditText(context);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		this.answerUI.setLayoutParams(params);
	}

	@Override
	public Response<String> getResponse() {
		return new SimpleResponse<>(this.answerUI.getText().toString());
	}

	@Override
	public View getAnswerUI() {
		return this.answerUI;
	}

	@Override
	public ViewStyle getViewStyle() {
		return ViewStyle.TWO_LINE;
	}
}
