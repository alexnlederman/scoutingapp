package com.example.vanguard.CustomUIElements;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.R;

/**
 * Created by mbent on 7/11/2017.
 */

public class ErrorTextView extends AppCompatTextView {
	public ErrorTextView(Context context, int message) {
		super(context);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		this.setLayoutParams(params);

		this.setGravity(Gravity.CENTER);
		this.setText(message);
	}
}
