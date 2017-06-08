package com.example.vanguard.Questions.QuestionViewers.QuestionListViewers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionList;
import com.example.vanguard.Questions.QuestionTypes.DoubleQuestion;
import com.example.vanguard.Questions.QuestionTypes.StringQuestion;
import com.example.vanguard.Questions.QuestionViewers.QuestionEditorViewers.SingleLineEditQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.QuestionEditorViewers.TwoLineEditQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.SimpleQuestionEditViewer;
import com.example.vanguard.R;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

/**
 * Created by BertTurtle on 6/6/2017.
 */

public class QuestionListEditViewer extends DragLinearLayout {

	protected Context context;
	protected QuestionList<Object> questions;
	protected Menu menu;

	public QuestionListEditViewer(Context context, QuestionList<Object> questions, Menu menu) {
		super(context);
		this.context = context;
		this.questions = questions;
		this.menu = menu;

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
				StringQuestion newQuestion = new StringQuestion(context, "Insert Title Here");
				questions.add(newQuestion);
				setupQuestions();
				return false;
			}
		});

		final MenuItem addIntegerQuestion = this.menu.add("Add Integer Question");
		addIntegerQuestion.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		addIntegerQuestion.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				DoubleQuestion newQuestion = new DoubleQuestion(context, "Insert Title Here");
				questions.add(newQuestion);
				setupQuestions();
				return false;
			}
		});
	}

	private void setupQuestions() {
		this.removeAllViews();
		int index = 0;
		for (Question<?> question : this.questions) {
			SimpleQuestionEditViewer questionViewer = null;
			if (question.getViewStyle().equals(Question.ViewStyle.SINGLE_LINE)) {
				questionViewer = new SingleLineEditQuestionViewer(this.context, question);
			}
			else if (question.getViewStyle().equals(Question.ViewStyle.TWO_LINE)) {
				questionViewer = new TwoLineEditQuestionViewer(this.context, question);
			}
			this.addView(questionViewer);
			questionViewer.getDeleteButton().setOnClickListener(new OnDeleteClickListener(index));
			this.setViewDraggable(questionViewer, questionViewer);
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
			questions.remove(this.index);
			setupQuestions();
		}
	}

	private class OnSwapListener implements OnViewSwapListener {

		@Override
		public void onSwap(View firstView, int firstPosition, View secondView, int secondPosition) {
			System.out.println("First: " + firstPosition);
			System.out.println("Second: " + secondPosition);
			Question question = questions.get(firstPosition);
			questions.remove(firstPosition);
			questions.add(secondPosition, question);
		}
	}

	public QuestionList<Object> getQuestions() {
		return questions;
	}

	public void setQuestions(QuestionList<Object> questions) {
		this.questions = questions;
		setupQuestions();
	}

	@Override
	protected void onAttachedToWindow() {
		setupQuestions();
		super.onAttachedToWindow();
	}
}
