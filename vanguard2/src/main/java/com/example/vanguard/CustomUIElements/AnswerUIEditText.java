package com.example.vanguard.CustomUIElements;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import com.example.vanguard.AnswerUI;

/**
 * Created by mbent on 6/29/2017.
 */

public class AnswerUIEditText extends AppCompatEditText implements AnswerUI<String> {
	public AnswerUIEditText(Context context) {
		super(context);
	}

	@Override
	public String getValue() {
		return String.valueOf(this.getText());
	}

	@Override
	public void setValue(String value) {
		this.setText(value);
	}
}
