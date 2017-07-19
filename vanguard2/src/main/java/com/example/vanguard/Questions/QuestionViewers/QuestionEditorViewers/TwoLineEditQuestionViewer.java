package com.example.vanguard.Questions.QuestionViewers.QuestionEditorViewers;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionViewers.SimpleQuestionEditViewer;

/**
 * Created by BertTurtle on 6/6/2017.
 */

public class TwoLineEditQuestionViewer extends SimpleQuestionEditViewer {

	public TwoLineEditQuestionViewer(Context context, Question<?> question) {
		super(context, question);
		this.setOrientation(LinearLayout.VERTICAL);
	}

	@Override
	protected void setupLabelLayoutParams() {
		LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		this.commentLinearLayout.setLayoutParams(p);
	}
}
