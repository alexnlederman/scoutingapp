package com.example.vanguard.questions.question_viewers.question_list_viewers;

import android.app.Activity;
import android.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;

import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.pages.fragments.dialog_fragments.ConfirmationDialogFragment;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.example.vanguard.questions.question_viewers.SimpleQuestionEditViewer;
import com.example.vanguard.R;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

/**
 * Created by BertTurtle on 6/6/2017.
 */

public class QuestionListEditViewer extends DragLinearLayout {

	protected Activity context;
	protected boolean isMatchForm;

	public QuestionListEditViewer(Activity context, boolean isMatchForm) {
		super(context);
		this.context = context;

		this.isMatchForm = isMatchForm;

		this.setOrientation(LinearLayout.VERTICAL);

		this.setOnViewSwapListener(new OnSwapListener());
	}

	private void setupQuestions() {
		this.removeAllViews();
		final AnswerList<Question> questions;
		if (isMatchForm) {
			questions = MainActivity.databaseManager.getMatchQuestions();
		}
		else
			questions = MainActivity.databaseManager.getPitQuestions();
		for (final Question question : questions) {
			if (question.isEditable()) {
				SimpleQuestionEditViewer questionViewer = question.getQuestionEditViewer(this.context);

				questionViewer.setEditTextWatcher(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {

					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {

					}

					@Override
					public void afterTextChanged(Editable s) {
						System.out.println("TEXT CHANGED");
						MainActivity.databaseManager.setQuestionLabel(question, s.toString());
					}
				});
				this.addDragView(questionViewer, questionViewer);
			}
			else {
				this.addView(question.getQuestionViewer(this.context));
			}
		}
	}

	private class OnSwapListener implements OnViewSwapListener {

		@Override
		public void onSwap(View firstView, int firstPosition, View secondView, int secondPosition) {
			MainActivity.databaseManager.swapQuestionIndexes(firstPosition, secondPosition, isMatchForm);
		}
	}

	@Override
	protected void onAttachedToWindow() {

//		setupQuestions();
		super.onAttachedToWindow();
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		if (visibility == View.VISIBLE) {
			setupQuestions();
		}
	}
}
