package com.example.vanguard.CustomUIElements;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.widget.EditText;

import com.example.vanguard.AnswerUI;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.R;

/**
 * Created by mbent on 6/29/2017.
 */

public class AnswerUIEditText extends AppCompatEditText implements AnswerUI<String> {
	public AnswerUIEditText(Context context) {
		super(context);
		int padding = Math.round(MainActivity.dpToPixels * 16);
		this.setPadding(padding, padding, padding, padding);
		this.setTextColor(ContextCompat.getColor(context, R.color.textColor));
		this.setFocusableInTouchMode(true);
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
