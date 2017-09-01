package com.example.vanguard.questions.question_viewers.question_list_viewers;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.vanguard.R;
import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.example.vanguard.questions.question_viewers.SimpleQuestionEditViewer;
import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.swipe.ListSwipeItem;

import java.util.List;

/**
 * Created by mbent on 8/26/2017.
 */

public class QuestionEditListView extends DragListView {


	protected Context context;
	protected boolean isMatchForm;

	public QuestionEditListView(Context context) {
		super(context);
		this.context = context;
		setup();
	}

	public QuestionEditListView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.context = context;
		setup();
	}

	public QuestionEditListView(Context context, AttributeSet attributeSet, int defAttr) {
		super(context, attributeSet, defAttr);
		this.context = context;
		setup();
	}

	public void updateQuestions(boolean isMatchForm) {
		this.isMatchForm = isMatchForm;
		setupQuestions();
	}

	private void setup() {
		this.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	}

	private void setupQuestions() {
		final AnswerList<Question> questions;
		if (isMatchForm) {
			questions = MainActivity.databaseManager.getMatchQuestions();
		} else
			questions = MainActivity.databaseManager.getPitQuestions();
		System.out.println("SETUP");
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false);
		this.setLayoutManager(linearLayoutManager);
//		this.setAdapter(new ListViewAdapter(questions), false);
	}

	public static class ViewHolder extends DragItemAdapter.ViewHolder {

		// TODO set this so that there is a more defined layout for it. I should also not be able to change the order of the default questions. Also this should be from a handle to allow swipping.
		public ViewHolder(View itemView) {
			super(itemView, R.id.handle2, false);
		}

		private void setQuestion(Context context, Question question) {
			ListSwipeItem swipeItem = (ListSwipeItem) itemView.findViewById(R.id.swipe_layout);
			swipeItem.setSupportedSwipeDirection(ListSwipeItem.SwipeDirection.RIGHT);
			RelativeLayout layout = (RelativeLayout) this.itemView.findViewById(R.id.question_layout);
			if (layout.getChildCount() == 1) {
				layout.addView(question.getQuestionEditViewer(context));
			}
		}
	}

	public static class ListViewAdapter extends DragItemAdapter<Question, ViewHolder> {

		Context context;

		public ListViewAdapter(Context context, List<Question> questions) {
			this.setHasStableIds(true);
			this.setItemList(questions);
			this.context = context;
			System.out.println("Questions: " + String.valueOf(questions));
		}

		@Override
		public QuestionEditListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			System.out.println("CREATE");
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_layout, parent, false);
			return new QuestionEditListView.ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(QuestionEditListView.ViewHolder holder, int position) {
			super.onBindViewHolder(holder, position);
			System.out.println("BIND");
			holder.setQuestion(this.context, this.getItemList().get(position));
		}

		@Override
		public long getItemId(int position) {
			return mItemList.get(position).getID().hashCode();
		}
	}
}
