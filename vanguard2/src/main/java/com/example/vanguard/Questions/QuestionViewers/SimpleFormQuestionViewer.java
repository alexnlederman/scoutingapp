package com.example.vanguard.Questions.QuestionViewers;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vanguard.MainActivity;
import com.example.vanguard.Questions.Question;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public abstract class SimpleFormQuestionViewer extends LinearLayoutQuestionViewer {

	protected TextView labelTextView;

	public SimpleFormQuestionViewer(Context context, Question<?> question) {
		super(context, question);
	}

	@Override
	protected void setupLabelView() {
		this.labelTextView = new TextView(this.context);

		this.labelTextView.setText(this.question.getLabel());

		System.out.println(this.labelTextView.getText());

		this.labelTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

		int margin = Math.round(MainActivity.dpToPixels * 20);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.setMargins(margin, margin, margin, margin);
		this.labelTextView.setLayoutParams(params);

		setupLabelLayoutParams();

		this.addView(this.labelTextView);
	}

	protected abstract void setupLabelLayoutParams();
}
