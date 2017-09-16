package com.example.vanguard.questions.question_viewers;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vanguard.AnswerUI;
import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.questions.Question;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public abstract class SimpleFormQuestionViewer extends LinearLayoutQuestionViewer {

	protected TextView labelTextView;
	Question.ViewStyle viewStyle;

	public SimpleFormQuestionViewer(Context context, Question question, Question.ViewStyle viewStyle) {
		super(context, question);
		this.viewStyle = viewStyle;
	}

	public SimpleFormQuestionViewer(Context context, Question question, Object initialValue, Question.ViewStyle viewStyle) {
		super(context, question, initialValue);
		this.viewStyle = viewStyle;
	}

	@Override
	protected void setupLabelView() {
		this.labelTextView = new TextView(this.context);
		this.labelTextView.setText(this.question.getLabel());

		this.labelTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

		int margin = Math.round(MainActivity.dpToPixels * 20);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(margin, margin, margin, margin);
		this.labelTextView.setLayoutParams(params);

		setupLabelLayoutParams();

		this.addView(this.labelTextView);
	}

	protected abstract void setupLabelLayoutParams();

	public Object getValue() {
		return ((AnswerUI) this.answerUI).getValue();
	}

	public void setValue(Object value) {
		((AnswerUI) this.answerUI).setValue(value);
	}

	public void refreshView() {
		this.labelTextView.setText(this.question.getLabel());

		int index = this.indexOfChild(this.answerUI);
		this.removeView(this.answerUI);

		this.answerUI = this.question.getAnswerUI();
		this.addView(this.answerUI, index);
	}

	public Question.ViewStyle getViewStyle() {
		return this.viewStyle;
	}

	public void updateLabel() {
		this.labelTextView.setText(this.question.getLabel());
	}
}
