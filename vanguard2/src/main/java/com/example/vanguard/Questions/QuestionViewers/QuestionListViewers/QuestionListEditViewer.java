package com.example.vanguard.Questions.QuestionViewers.QuestionListViewers;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.vanguard.Questions.MatchScoutQuestionList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionList;
import com.example.vanguard.Questions.QuestionTypes.IntegerQuestion;
import com.example.vanguard.Questions.QuestionTypes.StringQuestion;
import com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers.SingleLineFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers.TwoLineFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.LinearLayoutQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.QuestionEditorViewers.SingleLineEditQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.QuestionEditorViewers.TwoLineEditQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.SimpleFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.SimpleQuestionEditViewer;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import java.util.HashMap;

/**
 * Created by BertTurtle on 6/6/2017.
 */

public class QuestionListEditViewer<T extends QuestionList> extends DragLinearLayout {

	protected Context context;
	protected T questions;
	protected Menu menu;

	public QuestionListEditViewer(Context context, T questions, Menu menu) {
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
				StringQuestion newQuestion = new StringQuestion(new HashMap<String, Object>(), context);
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
				IntegerQuestion newQuestion = new IntegerQuestion(new HashMap<String, Object>(), context);
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
			if (question.isEditable()) {
				SimpleQuestionEditViewer questionViewer = null;
				if (question.getViewStyle().equals(Question.ViewStyle.SINGLE_LINE)) {
					questionViewer = new SingleLineEditQuestionViewer(this.context, question);
				} else if (question.getViewStyle().equals(Question.ViewStyle.TWO_LINE)) {
					questionViewer = new TwoLineEditQuestionViewer(this.context, question);
				}
				questionViewer.getDeleteButton().setOnClickListener(new OnDeleteClickListener(index));
				System.out.println("DRAGGABLE");
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

	public T getQuestions() {
		return questions;
	}

	public void setQuestions(T questions) {
		this.questions = questions;
		setupQuestions();
	}

	@Override
	protected void onAttachedToWindow() {
		setupQuestions();
		super.onAttachedToWindow();
	}
}
