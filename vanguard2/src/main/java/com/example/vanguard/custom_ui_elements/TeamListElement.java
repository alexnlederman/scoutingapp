package com.example.vanguard.custom_ui_elements;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by mbent on 6/29/2017.
 */

public class TeamListElement extends RelativeLayout {

	public TeamListElement(final Activity context, final Integer team) {
		super(context);
		TextView textView = new TextView(context);
		textView.setText(String.valueOf(team));
		textView.setTextSize(30);
		this.addView(textView);
	}

	public interface TeamSelectedListener {
		void teamSelected(Context context, int team);
	}
}
