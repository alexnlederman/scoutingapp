package com.example.vanguard.questions.question_viewers.form_question_viewers;

import android.content.Context;
import android.widget.LinearLayout;

import com.example.vanguard.questions.Question;
import com.example.vanguard.questions.question_viewers.SimpleFormQuestionViewer;

/**
 * Created by BertTurtle on 6/1/2017.
 */

public class TwoLineFormQuestionViewer extends SimpleFormQuestionViewer {


	public TwoLineFormQuestionViewer(Context context, Question question) {
		super(context, question, Question.ViewStyle.TWO_LINE);
		this.setOrientation(LinearLayout.VERTICAL);
	}

	public TwoLineFormQuestionViewer(Context context, Question question, Object initialValue) {
		super(context, question, initialValue, Question.ViewStyle.TWO_LINE);
		this.setOrientation(LinearLayout.VERTICAL);
	}

	@Override
	protected void setupLabelLayoutParams() {

	}

}
