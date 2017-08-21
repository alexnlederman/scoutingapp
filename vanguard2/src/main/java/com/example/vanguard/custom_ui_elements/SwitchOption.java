package com.example.vanguard.custom_ui_elements;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.vanguard.R;

/**
 * Created by mbent on 7/26/2017.
 */

public class SwitchOption extends MaterialLinearLayout {

	Switch toggle;
	String description;

	public SwitchOption(Context context, String description) {
		super(context);

		setup(description, context);
	}

	public SwitchOption(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public SwitchOption(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwitchOption);
		String description = a.getString(R.styleable.SwitchOption_text);
		a.recycle();
		setup(description, context);
	}

	private void setup(String description, Context context) {
		this.description = description;

		this.toggle = new Switch(context);
		this.addView(this.toggle);

		TextView descriptionTextView = new TextView(context);
		descriptionTextView.setText(description);
		this.addView(descriptionTextView);

		this.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	public boolean isEnabled() {
		return this.toggle.isChecked();
	}

	public Switch getSwitch() {
		return this.toggle;
	}

	public boolean isChecked() {
		return this.toggle.isChecked();
	}

	public String getDescription() {
		return this.description;
	}

	public void setChecked(boolean checked) {
		this.getSwitch().setChecked(checked);
	}

	public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
		this.getSwitch().setOnCheckedChangeListener(listener);
	}
}
