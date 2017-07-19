package com.example.vanguard.Questions.QuestionViewers.QuestionListViewers;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Parcel;
import android.support.annotation.IntDef;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.vanguard.DatabaseManager;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Pages.Fragments.DialogFragments.ConfirmationDialogFragment;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers.SingleLineFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers.TwoLineFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.QuestionEditorViewers.SingleLineEditQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.QuestionEditorViewers.TwoLineEditQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.SimpleFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.SimpleQuestionEditViewer;
import com.example.vanguard.R;
import com.example.vanguard.Responses.Response;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import java.util.List;

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
		if (isMatchForm)
			questions = MainActivity.databaseManager.getMatchQuestions();
		else
			questions = MainActivity.databaseManager.getPitQuestions();
		for (final Question<?> question : questions) {
			if (question.isEditable()) {
				SimpleQuestionEditViewer questionViewer = question.getQuestionEditViewer(this.context);
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
			DialogFragment fragment = ConfirmationDialogFragment.newInstance(R.layout.dialog_confirm_delete_question, new ConfirmationDialogFragment.ConfirmDialogListener() {
				@Override
				public void confirm() {
					MainActivity.databaseManager.deleteQuestion(question);
					setupQuestions();
				}

				@Override
				public int describeContents() {
					return 0;
				}

				@Override
				public void writeToParcel(Parcel dest, int flags) {

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
		setupQuestions();
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
