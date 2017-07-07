package com.example.vanguard.Questions.QuestionViewers.QuestionListViewers;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.vanguard.DatabaseManager;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers.SingleLineFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers.TwoLineFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.QuestionEditorViewers.SingleLineEditQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.QuestionEditorViewers.TwoLineEditQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.SimpleFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.SimpleQuestionEditViewer;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

/**
 * Created by BertTurtle on 6/6/2017.
 */

public class QuestionListEditViewer extends DragLinearLayout {

	protected Context context;
	protected DatabaseManager databaseManager;
	protected boolean isMatchForm;
	MenuItem addStringQuestion;
	MenuItem addIntegerQuestion;

	public QuestionListEditViewer(Context context, DatabaseManager databaseManager, boolean isMatchForm) {
		super(context);
		this.context = context;
		this.databaseManager = databaseManager;

		this.isMatchForm = isMatchForm;

		this.setOrientation(LinearLayout.VERTICAL);

		this.setOnViewSwapListener(new OnSwapListener());

		setupQuestions();
	}

	public void addStringQuestion() {
		databaseManager.createQuestion("Enter Title Here", Question.QuestionTypes.STRING.toString(), isMatchForm);
		setupQuestions();
	}

	public void addIntegerQuestion() {
		databaseManager.createQuestion("Enter Title Here", Question.QuestionTypes.INTEGER.toString(), isMatchForm);
		setupQuestions();
	}

	private void setupQuestions() {
		this.removeAllViews();
		final AnswerList<Question> questions;
		if (isMatchForm)
			questions = databaseManager.getMatchQuestions();
		else
			questions = databaseManager.getPitQuestions();
		for (final Question<?> question : questions) {
			if (question.isEditable()) {
				SimpleQuestionEditViewer questionViewer = null;
				if (question.getViewStyle().equals(Question.ViewStyle.SINGLE_LINE)) {
					questionViewer = new SingleLineEditQuestionViewer(this.context, question);
				} else if (question.getViewStyle().equals(Question.ViewStyle.TWO_LINE)) {
					questionViewer = new TwoLineEditQuestionViewer(this.context, question);
				}
				questionViewer.getDeleteButton().setOnClickListener(new OnDeleteClickListener(question));
				questionViewer.setEditTextWatcher(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {

					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {

					}

					@Override
					public void afterTextChanged(Editable s) {
						databaseManager.setQuestionLabel(question, s.toString());
					}
				});
				this.addDragView(questionViewer, questionViewer);
			}
			else {
				SimpleFormQuestionViewer questionViewer = null;
				if (question.getViewStyle().equals(Question.ViewStyle.SINGLE_LINE)) {
					questionViewer = new SingleLineFormQuestionViewer(this.context, question);
				} else if (question.getViewStyle().equals(Question.ViewStyle.TWO_LINE)) {
					questionViewer = new TwoLineFormQuestionViewer(this.context, question);
				}
				this.addView(questionViewer);
			}
		}
	}

	private class OnDeleteClickListener implements OnClickListener {

		Question question;

		public OnDeleteClickListener(Question question) {
			this.question = question;
		}

		@Override
		public void onClick(View v) {
//			System.out.println("Index: " + index);
			databaseManager.deleteQuestion(question);
			setupQuestions();
		}
	}

	private class OnSwapListener implements OnViewSwapListener {

		@Override
		public void onSwap(View firstView, int firstPosition, View secondView, int secondPosition) {
			// TODO make this lag less.
			databaseManager.swapQuestionIndexes(firstPosition, secondPosition, isMatchForm);
		}
	}

	@Override
	protected void onAttachedToWindow() {
		setupQuestions();
		super.onAttachedToWindow();
	}
}
