package com.example.vanguard.Questions.QuestionViewers;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vanguard.AnswerUI;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.R;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public abstract class SimpleFormQuestionViewer extends LinearLayoutQuestionViewer {

	protected TextView labelTextView;

	public SimpleFormQuestionViewer(Context context, Question question) {
		super(context, question);
	}

	public SimpleFormQuestionViewer(Context context, Question question, Object initialValue) {
		super(context, question, initialValue);
	}

	@Override
	protected void setupLabelView() {
		this.labelTextView = new TextView(this.context);
		this.labelTextView.setText(this.question.getLabel());

		this.labelTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

		int margin = Math.round(MainActivity.dpToPixels * 20);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.setMargins(margin, margin, margin, margin);
		this.labelTextView.setLayoutParams(params);

		setupLabelLayoutParams();

		this.addView(this.labelTextView);
	}

	protected abstract void setupLabelLayoutParams();

	public void setValue(Object value) {
		((AnswerUI) this.answerUI).setValue(value);
	}

	public Object getValue() {
		return ((AnswerUI) this.answerUI).getValue();
	}

	public void refreshView() {
		this.labelTextView.setText(this.question.getLabel());

		int index = this.indexOfChild(this.answerUI);
		this.removeView(this.answerUI);

		this.answerUI = this.question.getAnswerUI();
		this.addView(this.answerUI, index);
	}
}
