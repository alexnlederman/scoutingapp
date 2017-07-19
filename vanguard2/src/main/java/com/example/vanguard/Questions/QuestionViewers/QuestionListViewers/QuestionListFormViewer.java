package com.example.vanguard.Questions.QuestionViewers.QuestionListViewers;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.vanguard.CustomUIElements.AnswerUICustomNumberPicker;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionTypes.PitTeamNumberQuestion;
import com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers.SingleLineFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers.TwoLineFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.SimpleFormQuestionViewer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public class QuestionListFormViewer extends LinearLayout {

	private Activity context;
	private Button submitButton;
	private AnswerList<Question> questions;
	private List<SimpleFormQuestionViewer> viewers;
	private boolean isMatchForm;
	private boolean isPracticeMatch;

	public QuestionListFormViewer(Activity context, int teamNumber) {
		this(context, teamNumber, 0);
		this.context = context;
		this.isMatchForm = false;
		setupUI(teamNumber, 0);
	}

	public QuestionListFormViewer(Activity context, int teamNumber, int matchNumber) {
		super(context);
		this.context = context;
		this.isMatchForm = true;

		setupUI(teamNumber, matchNumber);
	}

	public QuestionListFormViewer(Activity context) {
		super(context);
		this.isPracticeMatch = true;
		this.context = context;
		this.isMatchForm = true;
		setupUI(0, 0);
	}

	private void setupUI(int teamNumber, int qualNumber) {
		this.setOrientation(LinearLayout.VERTICAL);

		setupQuestions();

		if (isMatchForm) {
			setMatchScoutMatchNumberQuestionValue(qualNumber);
			setMatchScoutTeamNumberQuestionValue(teamNumber);
		}
		else if (!isPracticeMatch)
			((PitTeamNumberQuestion) questions.get(0)).setTeam(teamNumber);

		if (!isMatchForm) {
			for (Question question : this.questions) {
				question.setTeamNumber(teamNumber);
			}
		}
	}

	private void setupQuestions() {
		this.removeAllViews();
		this.viewers = new ArrayList<>();
		if (isMatchForm || isPracticeMatch)
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
					if (isMatchForm) {
						question.setMatchNumber(getMatchScoutMatchNumberQuestionValue());
						question.setTeamNumber(getMatchScoutTeamNumberQuestionValue());
						question.setIsPracticeMatchQuestion(isPracticeMatch);
					}
					question.saveResponse();
				}
				MainActivity.databaseManager.saveResponses(questions);
				context.finish();
			}
		});
	}

	private void addQuestion(Question question) {
		SimpleFormQuestionViewer viewer = question.getQuestionViewer(this.context);
		this.viewers.add(viewer);
		this.addView(viewer);
	}

	private int getMatchScoutTeamNumberQuestionValue() {
		return (int) this.viewers.get(1).getValue();
	}

	private int getMatchScoutMatchNumberQuestionValue() {
		return (int) this.viewers.get(0).getValue();
	}

	private void setMatchScoutTeamNumberQuestionValue(int value) {
		this.viewers.get(1).setValue(value);
	}

	private void setMatchScoutMatchNumberQuestionValue(int value) {
		this.viewers.get(0).setValue(value);
	}
}
