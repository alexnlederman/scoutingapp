package com.example.vanguard.Questions.QuestionViewers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanguard.AnswerUI;
import com.example.vanguard.CustomUIElements.MaterialLinearLayout;
import com.example.vanguard.Questions.Question;

/**
 * Created by BertTurtle on 6/1/2017.
 */

public abstract class LinearLayoutQuestionViewer extends MaterialLinearLayout {

	protected final Question<?> question;
	protected final Context context;
	View answerUI;

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

	private void setupInput() {
		answerUI = this.question.getAnswerUI();
		if (answerUI.getParent() != null) {
			((ViewGroup) answerUI.getParent()).removeView(answerUI);
		}
		this.addView(answerUI);
	}

	protected abstract void setupLabelView();
}
