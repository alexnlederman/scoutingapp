package com.example.vanguard.questions.question_viewers.question_list_viewers.edit_list_viewer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.vanguard.R;
import com.example.vanguard.pages.activities.AddQuestionActivity;

public class QuestionTouchCallback extends ItemTouchHelper.SimpleCallback {

	public static final int QUESTION_EDITED = 4543;
	private Drawable deleteBackground;
	private Drawable deleteMark;
	private int markMargin;
	private Drawable editBackground;
	private Drawable editMark;
	private boolean initiated;
	private Activity context;
	private QuestionListViewAdapter adapter;

	public QuestionTouchCallback(Activity context, QuestionListViewAdapter adapter) {
		super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
//		super(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT);
		this.context = context;
		this.adapter = adapter;
	}

//	@Override
//	public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
//		System.out.println("MOVED");
//		super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
//		((QuestionListViewAdapter) recyclerView.getAdapter()).moveQuestion(fromPos, toPos);
//	}

	private void init() {
		this.deleteBackground = new ColorDrawable(Color.RED);
		this.deleteMark = ContextCompat.getDrawable(this.context, R.drawable.ic_clear_24dp);
		this.deleteMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
		this.markMargin = (int) this.context.getResources().getDimension(R.dimen.ic_clear_margin);

		this.editBackground = new ColorDrawable(Color.BLUE);
		this.editMark = ContextCompat.getDrawable(this.context, R.drawable.ic_reset);
		this.editMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

		initiated = true;
	}

	@Override
	public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//		System.out.println(println"MOVED");
		((QuestionListViewAdapter) recyclerView.getAdapter()).moveQuestion(viewHolder.getAdapterPosition(), target.getAdapterPosition());
		return false;
	}

	@Override
	public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
		if (direction == ItemTouchHelper.LEFT) {
			this.adapter.pendingRemoval(viewHolder.getAdapterPosition());
		} else {
			this.adapter.notifyItemChanged(viewHolder.getAdapterPosition());
			Intent intent = new Intent(context, AddQuestionActivity.class);
			intent.putExtra(AddQuestionActivity.IS_MATCH_QUESTION, ((QuestionViewHolder) viewHolder).questionViewer.getQuestion().isMatchQuestion());
			intent.putExtra(AddQuestionActivity.EDIT_QUESTION, ((QuestionViewHolder) viewHolder).questionViewer.getQuestion().getID());
			context.startActivityForResult(intent, QUESTION_EDITED);
		}
	}

	@Override
	public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
		View questionView = viewHolder.itemView;

		if (viewHolder.getAdapterPosition() == -1) {
			// TODO might want to run super with this.
			return;
		}

		if (!this.initiated) {
			init();
		}

		if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
			// TODO this might have to be adjusted because it can be swiped either direction.

			int itemHeight = questionView.getBottom() - questionView.getTop();
			int intrinsicWidth = this.deleteMark.getIntrinsicWidth();
			int intrinsicHeight = this.deleteMark.getIntrinsicHeight();

			int markTop = questionView.getTop() + (itemHeight - intrinsicHeight) / 2;
			int markBottom = markTop + intrinsicHeight;

			int markLeft;
			int markRight;
			if (dX > 0) {
				this.editBackground.setBounds(questionView.getLeft(), questionView.getTop(), (int) (questionView.getLeft() + dX), questionView.getBottom());
				this.editBackground.draw(c);
				markLeft = questionView.getLeft() + this.markMargin;
				markRight = questionView.getLeft() + this.markMargin + intrinsicWidth;
				this.editMark.setBounds(markLeft, markTop, markRight, markBottom);
				this.editMark.draw(c);
			} else {
				this.deleteBackground.setBounds((int) (questionView.getRight() + dX), questionView.getTop(), questionView.getRight(), questionView.getBottom());
				this.deleteBackground.draw(c);
				markLeft = questionView.getRight() - this.markMargin - intrinsicWidth;
				markRight = questionView.getRight() - this.markMargin;
				this.deleteMark.setBounds(markLeft, markTop, markRight, markBottom);
				this.deleteMark.draw(c);
			}
		}
		super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
	}

	@Override
	public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
		int position = viewHolder.getAdapterPosition();
		if (this.adapter.isPendingRemoval(position)) {
			return 0;
		}
		return super.getSwipeDirs(recyclerView, viewHolder);
	}

	@Override
	public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
		if (((QuestionViewHolder) viewHolder).isEnabled())
			return super.getMovementFlags(recyclerView, viewHolder);
		else
			return 0;
	}
}