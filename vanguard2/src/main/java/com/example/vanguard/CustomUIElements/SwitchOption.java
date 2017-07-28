package com.example.vanguard.CustomUIElements;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by mbent on 7/26/2017.
 */

public class SwitchOption extends MaterialLinearLayout {

	Switch toggle;
	String description;

	public SwitchOption(Context context, String description) {
		super(context);

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
}
