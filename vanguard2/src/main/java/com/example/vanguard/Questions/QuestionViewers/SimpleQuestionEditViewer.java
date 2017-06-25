package com.example.vanguard.Questions.QuestionViewers;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vanguard.MainActivity;
import com.example.vanguard.Questions.Question;

/**
 * Created by BertTurtle on 6/6/2017.
 */

public abstract class SimpleQuestionEditViewer extends LinearLayoutQuestionViewer {

	protected EditText labelEditText;
	protected final Button deleteButton;
	protected LinearLayout commentLinearLayout;


	public SimpleQuestionEditViewer(Context context, Question<?> question) {
		super(context, question);
		this.deleteButton = new Button(context);
		this.commentLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		setupDeleteButton();
	}

	protected void setupDeleteButton() {
		this.deleteButton.setText("X");
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Math.round(40 * MainActivity.dpToPixels), ViewGroup.LayoutParams.MATCH_PARENT);
		this.deleteButton.setLayoutParams(params);
		this.commentLinearLayout.addView(this.deleteButton, 0);
	}

	@Override
	protected void setupLabelView() {
		this.labelEditText = new EditText(this.context);

		this.labelEditText.setText(this.question.getLabel());


		this.labelEditText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

		int margin = Math.round(MainActivity.dpToPixels * 15);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(margin, margin, margin, margin);
		this.labelEditText.setLayoutParams(params);

		this.commentLinearLayout = new LinearLayout(context);

		this.commentLinearLayout.addView(this.labelEditText);

		this.addView(this.commentLinearLayout);

		setupLabelLayoutParams();

	}

	public Button getDeleteButton() {
		return this.deleteButton;
	}

	public void setEditTextWatcher(TextWatcher watcher) {
		this.labelEditText.addTextChangedListener(watcher);
	}

	protected abstract void setupLabelLayoutParams();
}
