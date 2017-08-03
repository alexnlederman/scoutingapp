package com.example.vanguard.CustomUIElements;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.ViewGroup;

import com.example.vanguard.Pages.Activities.MainActivity;

/**
 * Created by mbent on 7/28/2017.
 */

public class GenericTextView extends AppCompatTextView {
	public GenericTextView(Context context, String text) {
		super(context);

		this.setText(text);

		this.setTextSize(16);
		this.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		int padding = Math.round(MainActivity.dpToPixels * 10);
		this.setPadding(padding, padding / 2, padding / 2, padding);
	}
}
