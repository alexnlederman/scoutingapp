package com.example.vanguard.CustomUIElements;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;

/**
 * Created by mbent on 7/28/2017.
 */

public class DividerTextView extends GenericTextView {
	public DividerTextView(Context context, String text) {
		super(context, text);

		this.setTypeface(Typeface.DEFAULT_BOLD);
	}
}
