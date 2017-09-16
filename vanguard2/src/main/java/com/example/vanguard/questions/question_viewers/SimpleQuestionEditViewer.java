package com.example.vanguard.questions.question_viewers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.questions.Question;

/**
 * Created by BertTurtle on 6/6/2017.
 */

public abstract class SimpleQuestionEditViewer extends LinearLayoutQuestionViewer {

	public final static int LABEL_ID = View.generateViewId();
	protected final Button deleteButton;
	protected EditText labelEditText;
	protected LinearLayout commentLinearLayout;


	public SimpleQuestionEditViewer(Context context, Question<?> question) {
		super(context, question);
		this.deleteButton = new Button(context);
		setupDeleteButton();
	}

	private void setupCommentLinearLayout() {
		this.commentLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		p.gravity = Gravity.CENTER;
		this.commentLinearLayout.setLayoutParams(p);
		int padding = Math.round(MainActivity.dpToPixels * 20);
		this.commentLinearLayout.setPadding(padding, padding, padding, padding);
	}

	protected void setupDeleteButton() {
		this.deleteButton.setText("X");
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Math.round(40 * MainActivity.dpToPixels), ViewGroup.LayoutParams.MATCH_PARENT);
		params.gravity = Gravity.CENTER;
		this.deleteButton.setLayoutParams(params);
		this.commentLinearLayout.addView(this.deleteButton, 0);
	}

	@Override
	protected void setupLabelView() {
		this.labelEditText = new EditText(this.context);

		this.labelEditText.setText(this.question.getLabel());


		this.labelEditText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
		this.labelEditText.setId(LABEL_ID);

		int margin = Math.round(MainActivity.dpToPixels * 15);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(margin, 0, 0, 0);
		params.gravity = Gravity.CENTER;
		this.labelEditText.setLayoutParams(params);

		this.commentLinearLayout = new LinearLayout(context);

		this.commentLinearLayout.addView(this.labelEditText);

		this.addView(this.commentLinearLayout);

		setupCommentLinearLayout();

		setupLabelLayoutParams();
	}

	public Button getDeleteButton() {
		return this.deleteButton;
	}

	public void setEditTextWatcher(TextWatcher watcher) {
		this.labelEditText.addTextChangedListener(watcher);
	}

	protected abstract void setupLabelLayoutParams();

	public void updateLabel() {
		this.labelEditText.setText(this.question.getLabel());
	}
}
