package com.example.vanguard.custom_ui_elements;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vanguard.R;

/**
 * Created by mbent on 6/29/2017.
 */

public class SettingsView extends LinearLayout {

	public SettingsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public SettingsView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		int padding = Math.round(20);
		this.setPadding(padding, padding / 2, padding / 2, padding);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingsView);
		String description = a.getString(R.styleable.SettingsView_description);
		String title = a.getString(R.styleable.SettingsView_title);
		a.recycle();
		this.setOrientation(VERTICAL);
		TextView titleTextView = new TextView(context);
		titleTextView.setTextSize(22);
		titleTextView.setText(title);
		this.addView(titleTextView);
		TextView descriptionTextView = new TextView(context);
		descriptionTextView.setText(description);
		descriptionTextView.setTextSize(10);
		descriptionTextView.setTextColor(ContextCompat.getColor(context, R.color.lightGrey));
		this.addView(descriptionTextView);
	}
}
