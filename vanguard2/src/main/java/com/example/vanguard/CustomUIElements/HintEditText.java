package com.example.vanguard.CustomUIElements;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by mbent on 8/3/2017.
 */

public class HintEditText extends TextInputLayout {

	EditText editText;

	public HintEditText(Context context, String hint) {
		super(context);
		setup(context, hint);
	}

	public HintEditText(Context context, AttributeSet attrs, String hint) {
		super(context, attrs);
		setup(context, hint);
	}

	public HintEditText(Context context, AttributeSet attrs, int defStyleAttr, String hint) {
		super(context, attrs, defStyleAttr);
		setup(context, hint);
	}

	private void setup(Context context, String hint) {
		this.editText = new EditText(context);
		this.editText.setHint(hint);
		this.addView(this.editText);
	}

	public EditText getEditText() {
		return this.editText;
	}
}
