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
		super(context, question);
		this.setOrientation(LinearLayout.VERTICAL);
	}

	public TwoLineFormQuestionViewer(Context context, Question question, Object initialValue) {
		super(context, question, initialValue);
		this.setOrientation(LinearLayout.VERTICAL);
	}

	@Override
	protected void setupLabelLayoutParams() {

	}

}
