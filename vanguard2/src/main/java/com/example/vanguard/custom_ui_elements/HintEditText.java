package com.example.vanguard.custom_ui_elements;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by mbent on 8/3/2017.
 */

public class HintEditText extends TextInputLayout {

	TextInputEditText editText;

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
		this.editText = new TextInputEditText(context);
		this.editText.setHint(hint);
		this.addView(this.editText);
	}

	public TextInputEditText getEditText() {
		return this.editText;
	}
}
