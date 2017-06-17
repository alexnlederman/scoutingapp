package com.example.vanguard.Questions.QuestionViewers.QuestionListViewers;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.vanguard.Questions.MatchScoutQuestionList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers.SingleLineFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers.TwoLineFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.SimpleFormQuestionViewer;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public class QuestionListFormViewer extends LinearLayout {

	private MatchScoutQuestionList questions;
	private Context context;
	private Button submitButton;

	public QuestionListFormViewer(Context context, MatchScoutQuestionList questions) {
		super(context);
		this.questions = questions;
		this.context = context;

		this.setOrientation(LinearLayout.VERTICAL);

		setupQuestions();
	}

	private void setupQuestions() {
		this.removeAllViews();
		for (Question<?> question : this.questions) {
			this.addQuestion(question);
		}
		this.submitButton = new Button(context);
		this.submitButton.setText("Submit");
		this.addView(this.submitButton);
		this.submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (Question question : questions) {
					question.setMatchNumber(questions.getMatchNumberQuestion().getValue());
					System.out.println(questions.getMatchNumberQuestion().getValue());
					question.saveResponse();
					questions.saveQuestions();
				}
			}
		});
	}

	private void addQuestion(Question question) {
		SimpleFormQuestionViewer questionViewer = null;
		if (question.getViewStyle().equals(Question.ViewStyle.SINGLE_LINE)) {
			questionViewer = new SingleLineFormQuestionViewer(this.context, question);
		}
		else if (question.getViewStyle().equals(Question.ViewStyle.TWO_LINE)) {
			questionViewer = new TwoLineFormQuestionViewer(this.context, question);
		}
		this.addView(questionViewer);
	}

	public void setQuestions(MatchScoutQuestionList questions) {
		this.questions = questions;
		setupQuestions();
	}
}
