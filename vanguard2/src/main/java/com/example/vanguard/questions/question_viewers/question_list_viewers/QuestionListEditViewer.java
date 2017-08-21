package com.example.vanguard.questions.question_viewers.question_list_viewers;

import android.app.Activity;
import android.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.R;
import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.pages.fragments.dialog_fragments.ConfirmationDialogFragment;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.example.vanguard.questions.question_viewers.SimpleQuestionEditViewer;
import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragListView;

/**
 * Created by BertTurtle on 6/6/2017.
 */

public class QuestionListEditViewer extends DragListView {

	protected Activity context;
	protected boolean isMatchForm;
	private final static int HANDLE_ID = 442;

	public QuestionListEditViewer(Activity context, final boolean isMatchForm) {
		super(context);
		this.context = context;

		this.isMatchForm = isMatchForm;

		this.setDragListCallback(new DragListCallback() {
			@Override
			public boolean canDragItemAtPosition(int i) {
				if (isMatchForm) {
					return i > 2;
				} else {
					return i > 1;
				}
			}

			@Override
			public boolean canDropItemAtPosition(int i) {
				if (isMatchForm) {
					return i > 2;
				} else {
					return i > 1;
				}
			}
		});

		this.setDragListListener(new DragListListener() {
			@Override
			public void onItemDragStarted(int i) {

			}

			@Override
			public void onItemDragging(int i, float v, float v1) {
				System.out.println("I: " + i);
				System.out.println("V: " + v);
				System.out.println("V1: " + v1);
			}

			@Override
			public void onItemDragEnded(int i, int i1) {

			}
		});
//		this.setOnViewSwapListener(new OnSwapListener());
	}

	private void setupQuestions() {
		// TODO
		final AnswerList<Question> questions;
		if (isMatchForm) {
			questions = MainActivity.databaseManager.getMatchQuestions();
		} else
			questions = MainActivity.databaseManager.getPitQuestions();
		this.setLayoutManager(new LinearLayoutManager(this.context));
		this.setAdapter(new ItemAdapter(questions), true);
	}

	@Override
	protected void onAttachedToWindow() {

//		setupQuestions();
		super.onAttachedToWindow();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		setupQuestions();

	}

//	@Override
//	protected void onWindowVisibilityChanged(int visibility) {
//		super.onWindowVisibilityChanged(visibility);
//		if (visibility == View.VISIBLE) {
//			setupQuestions();
//		}
//	}

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

	private class LayoutViewHolder extends DragItemAdapter.ViewHolder {

		public LayoutViewHolder(View itemView) {
			super(itemView, HANDLE_ID, false);
		}
	}

	private class ItemAdapter extends DragItemAdapter<Question, LayoutViewHolder> {

		AnswerList<Question> questions;

		public ItemAdapter(AnswerList<Question> questions) {
			this.questions = questions;
			this.setItemList(questions);
		}

		@Override
		public LayoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LinearLayout layout = new LinearLayout(context);
			return new LayoutViewHolder(layout);
		}

		@Override
		public void onBindViewHolder(LayoutViewHolder holder, int position) {
			final Question question =  questions.get(position);
			if (question.isEditable()) {
				SimpleQuestionEditViewer questionViewer = question.getQuestionEditViewer(context);
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
				((LinearLayout)holder.itemView).addView(questionViewer);
			}else {
				((LinearLayout)holder.itemView).addView(question.getQuestionViewer(context));
			}
		}
	}

//	private class OnSwapListener implements OnViewSwapListener {
//
//		@Override
//		public void onSwap(View firstView, int firstPosition, View secondView, int secondPosition) {
//			MainActivity.databaseManager.swapQuestionIndexes(firstPosition, secondPosition, isMatchForm);
//		}
//	}
}
