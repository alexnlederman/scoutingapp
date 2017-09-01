package com.example.vanguard.questions.question_viewers.question_list_viewers;

import android.app.Activity;
import android.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.example.vanguard.custom_ui_elements.CustomViewPager;
import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.pages.fragments.dialog_fragments.ConfirmationDialogFragment;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.example.vanguard.questions.question_viewers.SimpleQuestionEditViewer;
import com.example.vanguard.R;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by BertTurtle on 6/6/2017.
 */

public class QuestionListEditViewer extends DragLinearLayout {

	protected Activity context;
	protected boolean isMatchForm;
	CustomViewPager viewPager;

	public QuestionListEditViewer(Activity context, boolean isMatchForm) {
		super(context);
		this.context = context;

		this.isMatchForm = isMatchForm;

		this.viewPager = (CustomViewPager) context.findViewById(R.id.view_pager);

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
//				questionViewer.getDeleteButton().setOnClickListener(new OnDeleteClickListener(question));
				questionViewer.getDeleteButton().setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						System.out.println("EVENT: " + event.getAction());
//						ACTION_
						if (event.getAction() == ACTION_MOVE) {
							viewPager.setSwippable(false);
						}
						else if (event.getAction() == ACTION_UP || event.getAction() == ACTION_CANCEL) {
							viewPager.setSwippable(true);
						}

						return false;
					}
				});

				questionViewer.setEditTextWatcher(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {

					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {

					}

					@Override
					public void afterTextChanged(Editable s) {
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

	private class OnDeleteClickListener implements OnClickListener {

		Question question;

		public OnDeleteClickListener(Question question) {
			this.question = question;
		}

		@Override
		public void onClick(View v) {
			DialogFragment fragment = ConfirmationDialogFragment.newInstance(R.string.confirm_delete_question_title, R.string.confirm_delete_question_text, new ConfirmationDialogFragment.ConfirmDialogListener() {
				@Override
				public void confirm() {
					MainActivity.databaseManager.deleteQuestion(question);
					setupQuestions();
				}
			});
			fragment.show(context.getFragmentManager(), "Event Selector");
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
