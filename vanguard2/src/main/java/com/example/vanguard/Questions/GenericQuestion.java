package com.example.vanguard.Questions;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by BertTurtle on 4/22/2017.
 */

public abstract class GenericQuestion extends LinearLayout {
	protected final Context context;
	private TextView questionTextView;

	public GenericQuestion(Context context, String question) {
		super(context);

		this.context = context;
		this.setOrientation(LinearLayout.HORIZONTAL);
		addQuestionTextView(question);

		addInput();

	}

	private void addQuestionTextView(String question) {
		this.questionTextView = new TextView(context);

		this.questionTextView.setText(question);

		this.addView(questionTextView);
	}

	public void setQuestionText(String text) {
		this.questionTextView.setText(text);
	}

	protected abstract void addInput();
}