package com.example.vanguard.custom_ui_elements;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by mbent on 7/28/2017.
 */

public class DividerTextView extends GenericTextView {
	public DividerTextView(Context context, String text) {
		super(context, text);

		this.setTypeface(Typeface.DEFAULT_BOLD);
	}
}
