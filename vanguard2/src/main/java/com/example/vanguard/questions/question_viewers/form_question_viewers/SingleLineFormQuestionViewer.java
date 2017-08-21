package com.example.vanguard.questions.question_viewers.form_question_viewers;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.example.vanguard.questions.Question;
import com.example.vanguard.questions.question_viewers.SimpleFormQuestionViewer;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public class SingleLineFormQuestionViewer extends SimpleFormQuestionViewer {


	public SingleLineFormQuestionViewer(Context context, Question question) {
		super(context, question, Question.ViewStyle.SINGLE_LINE);
		this.setOrientation(LinearLayout.HORIZONTAL);
	}

	public SingleLineFormQuestionViewer(Context context, Question question, Object initialValue) {
		super(context, question, initialValue, Question.ViewStyle.SINGLE_LINE);
		this.setOrientation(LinearLayout.HORIZONTAL);
	}

	@Override
	protected void setupLabelLayoutParams() {
		LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) this.labelTextView.getLayoutParams();
		p.weight = 0.8f;
		p.gravity = Gravity.CENTER;
		this.labelTextView.setLayoutParams(p);
	}
}
