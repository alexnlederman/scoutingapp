package com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionViewers.SimpleFormQuestionViewer;

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
