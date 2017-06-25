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
	protected Menu menu;
	protected DatabaseManager databaseManager;
	protected boolean isMatchForm;

	public QuestionListEditViewer(Context context, Menu menu, DatabaseManager databaseManager, boolean isMatchForm) {
		super(context);
		this.context = context;
		this.menu = menu;
		this.databaseManager = databaseManager;

		this.isMatchForm = isMatchForm;

		this.setOrientation(LinearLayout.VERTICAL);

		this.setOnViewSwapListener(new OnSwapListener());

		setupQuestions();

		setupMenu();
	}

	private void setupMenu() {
		final MenuItem addStringQuestion = this.menu.add("Add String Question");
		addStringQuestion.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		addStringQuestion.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				databaseManager.createQuestion("Enter Title Here", DatabaseManager.QuestionTypes.STRING.toString(), isMatchForm);
				setupQuestions();
				return false;
			}
		});

		final MenuItem addIntegerQuestion = this.menu.add("Add Integer Question");
		addIntegerQuestion.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		addIntegerQuestion.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				databaseManager.createQuestion("Enter Title Here", DatabaseManager.QuestionTypes.INTEGER.toString(), isMatchForm);
				setupQuestions();
				return false;
			}
		});
	}

	private void setupQuestions() {
		this.removeAllViews();
		int index = 0;
		final AnswerList<Question> questions = databaseManager.getMatchQuestions();
		for (final Question<?> question : questions) {
			if (question.isEditable()) {
				SimpleQuestionEditViewer questionViewer = null;
				if (question.getViewStyle().equals(Question.ViewStyle.SINGLE_LINE)) {
					questionViewer = new SingleLineEditQuestionViewer(this.context, question);
				} else if (question.getViewStyle().equals(Question.ViewStyle.TWO_LINE)) {
					questionViewer = new TwoLineEditQuestionViewer(this.context, question);
				}
				questionViewer.getDeleteButton().setOnClickListener(new OnDeleteClickListener(index));
				final int finalIndex = index;
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
			index++;
		}
	}

	private class OnDeleteClickListener implements OnClickListener {

		int index;

		public OnDeleteClickListener(int index) {
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			databaseManager.deleteQuestion(index, isMatchForm);
			setupQuestions();
		}
	}

	private class OnSwapListener implements OnViewSwapListener {

		@Override
		public void onSwap(View firstView, int firstPosition, View secondView, int secondPosition) {
			System.out.println("First: " + firstPosition);
			System.out.println("Second: " + secondPosition);
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
