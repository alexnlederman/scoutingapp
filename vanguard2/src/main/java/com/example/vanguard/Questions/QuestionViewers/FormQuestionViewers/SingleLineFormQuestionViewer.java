package com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionViewers.SimpleFormQuestionViewer;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public class SingleLineFormQuestionViewer extends SimpleFormQuestionViewer {


	public SingleLineFormQuestionViewer(Context context, Question<?> question) {
		super(context, question);
		this.setOrientation(LinearLayout.HORIZONTAL);
	}

	@Override
	protected void setupLabelLayoutParams() {
		LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) this.labelTextView.getLayoutParams();
		p.weight = 0.8f;
		
		this.labelTextView.setLayoutParams(p);
	}


}
