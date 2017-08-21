package com.example.vanguard.custom_ui_elements.answer_ui_elements;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.vanguard.AnswerUI;

/**
 * Created by mbent on 8/5/2017.
 */

public class AnswerUIBooleanRadioButtons extends RadioGroup implements AnswerUI<Boolean> {

	RadioButton yesButton;
	RadioButton noButton;
	AnswerUIBooleanRadioButtons that;

	// TODO allow this to be unchecked if it is a pit question.
	public AnswerUIBooleanRadioButtons(Context context) {
		super(context);
		this.that = this;

		this.yesButton = new RadioButton(context);
		this.yesButton.setText("Yes");
		this.addView(this.yesButton);

		this.noButton = new RadioButton(context);
		this.noButton.setText("No");
		this.addView(this.noButton);
	}

	@Override
	public Boolean getValue() {
		int id = this.getCheckedRadioButtonId();
		if (this.yesButton.getId() == id) {
			return true;
		} else if (this.noButton.getId() == id) {
			return false;
		}
		return null;
	}

	@Override
	public void setValue(Boolean value) {
		if (value == null) {
			this.clearCheck();
		} else if (!value) {
			this.noButton.setChecked(true);
		} else {
			this.yesButton.setChecked(true);
		}
	}
}
