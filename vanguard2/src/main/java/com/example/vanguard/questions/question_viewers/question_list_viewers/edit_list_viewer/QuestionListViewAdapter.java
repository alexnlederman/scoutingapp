package com.example.vanguard.questions.question_viewers.question_list_viewers.edit_list_viewer;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanguard.R;
import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.questions.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class QuestionListViewAdapter extends RecyclerView.Adapter<QuestionViewHolder> {

	private static final int UNDO_DELAY_MILLIS = 3000;
	private Context context;
	private List<Question> questions;
	private List<Question> questionsPendingRemoval;
	private HashMap<Question, Runnable> pendingRunnables = new HashMap<Question, Runnable>();
	private Handler handler = new Handler();
	private boolean isMatchList;

	public QuestionListViewAdapter(Context context, boolean isMatchList) {
//			this.setHasStableIds(true);
		this.questionsPendingRemoval = new ArrayList<>();
		this.context = context;
		this.isMatchList = isMatchList;
		updateQuestions();
	}

	private void updateQuestions() {
		if (isMatchList)
			this.questions = MainActivity.databaseManager.getMatchQuestions();
		else
			this.questions = MainActivity.databaseManager.getPitQuestions();
		for (Question question : this.questions) {
			System.out.println("LAB: " + question.getLabel());
		}
	}

	@Override
	public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_layout, parent, false);
		return new QuestionViewHolder(view, true);
	}

	@Override
	public void onBindViewHolder(QuestionViewHolder holder, final int position) {
		final Question question = this.questions.get(position);

		holder.setEnabled(question.isEditable());
		System.out.println("BIND");

		if (this.questionsPendingRemoval.contains(question)) {
			holder.itemView.setBackgroundColor(Color.RED);
			if (holder.questionViewer != null) holder.questionViewer.setVisibility(View.GONE);
			holder.undoButton.setVisibility(View.VISIBLE);
			holder.undoButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Runnable pendingRemovalRunnable = pendingRunnables.remove(question);
					if (pendingRemovalRunnable != null)
						handler.removeCallbacks(pendingRemovalRunnable);
					questionsPendingRemoval.remove(question);
					notifyItemChanged(questions.indexOf(question));
				}
			});
		} else {
			holder.setQuestion(this.context, question);
			if (holder.questionViewer != null) holder.questionViewer.setVisibility(View.VISIBLE);
			holder.itemView.setBackgroundColor(Color.WHITE);
			holder.undoButton.setVisibility(View.GONE);
			holder.undoButton.setOnClickListener(null);
		}
	}

//	@Override
//	public long getItemId(int position) {
//		return this.questions.get(position).getID().hashCode();
//	}

	@Override
	public int getItemCount() {
		return questions.size();
	}

	public void pendingRemoval(int position) {
		final Question question = this.questions.get(position);
		if (!this.questionsPendingRemoval.contains(question)) {

			this.questionsPendingRemoval.add(question);

			Runnable pendingRemovalRunnable = new Runnable() {
				@Override
				public void run() {
					remove(questions.indexOf(question));
				}
			};

			handler.postDelayed(pendingRemovalRunnable, UNDO_DELAY_MILLIS);
			this.pendingRunnables.put(question, pendingRemovalRunnable);

			notifyItemChanged(position);
		}
	}

	public void remove(int position) {
		Question question = this.questions.get(position);
		if (this.questionsPendingRemoval.contains(question)) {
			this.questionsPendingRemoval.remove(question);
		}
		if (this.questions.contains(question)) {
			this.questions.remove(question);
			notifyItemRemoved(position);
		}
		MainActivity.databaseManager.deleteQuestion(question);
	}

	public boolean isPendingRemoval(int position) {
		Question question = this.questions.get(position);
		return this.questionsPendingRemoval.contains(question);
	}

	public void moveQuestion(int fromPosition, int toPosition) {
		if (fromPosition < toPosition) {
			for (int i = fromPosition; i < toPosition; i++) {
				Collections.swap(this.questions, i, i + 1);
			}
		} else {
			for (int i = fromPosition; i > toPosition; i--) {
				Collections.swap(this.questions, i, i - 1);
			}
		}
		MainActivity.databaseManager.swapQuestionIndexes(fromPosition, toPosition, this.isMatchList);
		notifyItemMoved(fromPosition, toPosition);
	}

	public void addQuestion() {
		updateQuestions();
		this.notifyItemInserted(this.getItemCount() - 1);
	}

	public void updateLabels() {
		System.out.println("UPDATE LABELS");
		updateQuestions();
		notifyDataSetChanged();
	}
}