package com.example.vanguard.Questions.QuestionViewers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanguard.CustomUIElements.MaterialLinearLayout;
import com.example.vanguard.Questions.Question;

/**
 * Created by BertTurtle on 6/1/2017.
 */

public abstract class LinearLayoutQuestionViewer extends MaterialLinearLayout {

	protected final Question<?> question;
	protected final Context context;

	public LinearLayoutQuestionViewer(Context context, Question<?> question) {
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
		View answerUI = this.question.getAnswerUI();
		if (answerUI.getParent() != null) {
			System.out.println("Removed!");
			((ViewGroup) answerUI.getParent()).removeView(answerUI);
		}
		this.addView(answerUI);
		System.out.println("RUN");
	}

	protected abstract void setupLabelView();
}
