package com.example.vanguard.Questions.QuestionViewers.QuestionEditorViewers;

import android.content.Context;
import android.widget.LinearLayout;

import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionViewers.SimpleQuestionEditViewer;

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

		LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) this.labelEditText.getLayoutParams();
		p.weight = 0.8f;

		this.commentLinearLayout.setLayoutParams(p);
	}
}
