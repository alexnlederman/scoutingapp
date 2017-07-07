package com.example.vanguard.Questions.QuestionViewers.QuestionListViewers;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.vanguard.CustomUIElements.CustomNumberPicker;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers.SingleLineFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers.TwoLineFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.SimpleFormQuestionViewer;
import com.example.vanguard.Responses.Response;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public class QuestionListFormViewer extends LinearLayout {

	private Activity context;
	private Button submitButton;
	private AnswerList<Question> questions;
	private boolean isMatchForm;
	private int teamNumber;

	public QuestionListFormViewer(Activity context, int teamNumber) {
		this(context, teamNumber, 0);
		this.context = context;
		this.isMatchForm = false;
		this.teamNumber = teamNumber;
		setupUI(teamNumber, 0);
	}

	public QuestionListFormViewer(Activity context, int teamNumber, int qualNumber) {
		super(context);
		this.context = context;
		this.isMatchForm = true;

		setupUI(teamNumber, qualNumber);
	}

	private void setupUI(int teamNumber, int qualNumber) {
		this.setOrientation(LinearLayout.VERTICAL);

		setupQuestions();

		// TODO this code is nasty. Make there be a better way to get the match/team number questions and to set the value of an answer.
		if (isMatchForm) {
			((CustomNumberPicker) questions.get(0).getAnswerUI()).setValue(qualNumber);
			((CustomNumberPicker) questions.get(1).getAnswerUI()).setValue(teamNumber);
		}
		else
			((CustomNumberPicker) questions.get(0).getAnswerUI()).setValue(teamNumber);
	}

	private void setupQuestions() {
		this.removeAllViews();
		if (isMatchForm)
			this.questions = MainActivity.databaseManager.getMatchQuestions();
		else
			this.questions = MainActivity.databaseManager.getPitQuestions();
		for (Question<?> question : questions) {
			this.addQuestion(question);
		}
		this.submitButton = new Button(context);
		this.submitButton.setText("Submit");
		this.addView(this.submitButton);
		this.submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setClickable(false);
				for (Question question : questions) {
					// TODO make these numbers be better than hard coded.
					if (isMatchForm) {
						question.setMatchNumber((int) questions.get(0).getValue());
						question.setTeamNumber((int) questions.get(1).getValue());
					}
					else
						question.setTeamNumber((int) questions.get(0).getValue());
					question.saveResponse();
				}
				MainActivity.databaseManager.saveResponses(questions);
				context.finish();
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
		if (!isMatchForm) {
			AnswerList<Response> currentResponses = question.getResponses();
			for (Response response : currentResponses) {
				if (response.getTeamNumber() == teamNumber) {
					questionViewer.setValue(response.getValue());
				}
			}
		}
		this.addView(questionViewer);
	}
}
