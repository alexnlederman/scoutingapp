package com.example.vanguard.Questions.QuestionViewers.QuestionListViewers;

import android.content.Context;
import android.widget.LinearLayout;

import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionList;
import com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers.SingleLineFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers.TwoLineFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.SimpleFormQuestionViewer;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public class QuestionListFormViewer extends LinearLayout {

	private QuestionList<? extends Object> questions;
	private Context context;

	public QuestionListFormViewer(Context context, QuestionList<? extends Object> questions) {
		super(context);
		this.questions = questions;
		this.context = context;

		this.setOrientation(LinearLayout.VERTICAL);

		setupQuestions();
	}

	private void setupQuestions() {
		this.removeAllViews();
		for (Question<?> question : this.questions) {
			SimpleFormQuestionViewer questionViewer = null;
			if (question.getViewStyle().equals(Question.ViewStyle.SINGLE_LINE)) {
				questionViewer = new SingleLineFormQuestionViewer(this.context, question);
			}
			else if (question.getViewStyle().equals(Question.ViewStyle.TWO_LINE)) {
				questionViewer = new TwoLineFormQuestionViewer(this.context, question);
			}
			this.addView(questionViewer);
		}
	}

	public void setQuestions(QuestionList<? extends Object> questions) {
		this.questions = questions;
		setupQuestions();
	}
}
