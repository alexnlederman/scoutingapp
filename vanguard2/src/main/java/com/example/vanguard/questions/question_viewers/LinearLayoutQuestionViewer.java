package com.example.vanguard.questions.question_viewers;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanguard.AnswerUI;
import com.example.vanguard.custom_ui_elements.MaterialLinearLayout;
import com.example.vanguard.questions.Question;

/**
 * Created by BertTurtle on 6/1/2017.
 */

public abstract class LinearLayoutQuestionViewer extends MaterialLinearLayout {

	protected final Question<?> question;
	protected final Context context;
	protected View answerUI;

	public LinearLayoutQuestionViewer(Context context, Question question, Object initialValue) {
		this(context, question);
		((AnswerUI) answerUI).setValue(initialValue);
	}

	public LinearLayoutQuestionViewer(Context context, Question question) {
		super(context);
		this.question = question;
		this.context = context;

		setupLayout();

		setupLabelView();

		setupInput();
	}

	private void setupLayout() {

	}

	protected void setupInput() {
		this.answerUI = this.question.getAnswerUI();
		if (this.answerUI.getParent() != null) {
			((ViewGroup) this.answerUI.getParent()).removeView(this.answerUI);
		}
		this.addView(this.answerUI);
		LayoutParams params = (LayoutParams) this.answerUI.getLayoutParams();
		params.gravity = Gravity.CENTER_VERTICAL;
	}

	protected abstract void setupLabelView();
}
