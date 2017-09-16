package com.example.vanguard.questions.question_viewers.question_list_viewers.edit_list_viewer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.vanguard.R;
import com.example.vanguard.questions.Question;
import com.example.vanguard.questions.question_viewers.LinearLayoutQuestionViewer;

import java.util.Objects;

public class QuestionViewHolder extends RecyclerView.ViewHolder {
	public LinearLayoutQuestionViewer questionViewer;
	public Button undoButton;
	boolean isEnabled;

	public QuestionViewHolder(View itemView, boolean isEnabled) {
		super(itemView);
		this.isEnabled = isEnabled;
		this.undoButton = (Button) itemView.findViewById(R.id.undo_button);
	}

	public void setQuestion(Context context, Question question) {
		LinearLayout layout = (LinearLayout) itemView.findViewById(R.id.question_layout);
		if (layout.getChildCount() == 0 || (this.questionViewer != null && !this.questionViewer.getQuestion().getID().equals(question.getID()))) {
			System.out.println("UPDATE Question: " + question.getLabel());
			layout.removeAllViews();
			if (question.isEditable())
				this.questionViewer = question.getQuestionEditViewer(context);
			else
				this.questionViewer = question.getQuestionViewer(context);
			layout.addView(this.questionViewer);
		}
		else {
			this.questionViewer.getQuestion().setLabel(question.getLabel());
			this.questionViewer.updateLabel();
		}
	}

	public boolean isEnabled() {
		return this.isEnabled;
	}

	public void setEnabled(boolean enabled) {
		this.isEnabled = enabled;
	}
}