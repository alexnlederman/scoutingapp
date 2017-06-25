package com.example.vanguard.Questions.QuestionViewers.QuestionListViewers;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.vanguard.DatabaseManager;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.QuestionViewers.MatchScoutQuestionList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers.SingleLineFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers.TwoLineFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.SimpleFormQuestionViewer;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public class QuestionListFormViewer extends LinearLayout {

	private Context context;
	private Button submitButton;
	private DatabaseManager databaseManager;
	private AnswerList<Question> questions;

	public QuestionListFormViewer(Context context, DatabaseManager databaseManager) {
		super(context);
		this.databaseManager = databaseManager;
		this.context = context;

		this.setOrientation(LinearLayout.VERTICAL);

		setupQuestions();
	}

	private void setupQuestions() {
		this.removeAllViews();
		this.questions = this.databaseManager.getMatchQuestions();
		for (Question<?> question : questions) {
			this.addQuestion(question);
		}
		this.submitButton = new Button(context);
		this.submitButton.setText("Submit");
		this.addView(this.submitButton);
		this.submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (Question question : questions) {
					// TODO make these numbers be better than hard coded.
					question.setMatchNumber((int) questions.get(0).getValue());
					question.setTeamNumber((int) questions.get(1).getValue());
					question.saveResponse();
				}
				databaseManager.saveResponses(questions);
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

	public void prepareQuestions() {
		setupQuestions();
	}
}
