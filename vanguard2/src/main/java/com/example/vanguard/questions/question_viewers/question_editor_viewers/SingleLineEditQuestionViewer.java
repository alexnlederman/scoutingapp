package com.example.vanguard.questions.question_viewers.question_editor_viewers;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.questions.Question;
import com.example.vanguard.questions.question_viewers.SimpleQuestionEditViewer;

/**
 * Created by BertTurtle on 6/6/2017.
 */

public class SingleLineEditQuestionViewer extends SimpleQuestionEditViewer {

	public SingleLineEditQuestionViewer(Context context, Question<?> question) {
		super(context, question);
		this.setOrientation(LinearLayout.HORIZONTAL);
	}

	@Override
	protected void setupLabelLayoutParams() {

		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
		p.weight = 0.8f;

		this.commentLinearLayout.setLayoutParams(p);
	}
}
