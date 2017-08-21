package com.example.vanguard.questions.question_viewers.question_editor_viewers;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.questions.Question;
import com.example.vanguard.questions.question_viewers.SimpleQuestionEditViewer;

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

	@Override
	protected void setupInput() {
		super.setupInput();

		int padding = Math.round(MainActivity.dpToPixels * 20);
		this.answerUI.setPadding(padding, 0, padding, padding);
	}
}
