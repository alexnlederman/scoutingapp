package com.example.vanguard.questions.question_viewers;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.vanguard.custom_ui_elements.MaterialLinearLayout;
import com.example.vanguard.questions.Question;
import com.example.vanguard.R;

/**
 * Created by mbent on 7/6/2017.
 */

public class CheckBoxQuestionViewer extends MaterialLinearLayout {

	CheckBox checkBox;
	Question question;

	public CheckBoxQuestionViewer(Context context, Question question) {
		super(context);

		this.setOrientation(HORIZONTAL);

		this.question = question;

		checkBox = new CheckBox(context);
		this.addView(checkBox);

		TextView label = new TextView(context);
		label.setText(question.getQualifiedLabel());
		label.setTextColor(ContextCompat.getColor(context, R.color.textColor));
		this.addView(label);
	}

	public boolean isChecked() {
		return checkBox.isChecked();
	}

	public Question getQuestion() {
		return this.question;
	}
}
