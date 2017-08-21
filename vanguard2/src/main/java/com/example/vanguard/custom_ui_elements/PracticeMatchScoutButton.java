package com.example.vanguard.custom_ui_elements;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.vanguard.pages.activities.MatchFormActivity;
import com.example.vanguard.R;

/**
 * Created by mbent on 7/11/2017.
 */

public class PracticeMatchScoutButton extends AppCompatButton {
	public PracticeMatchScoutButton(final Activity context) {
		super(context);

		ListView.LayoutParams params = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(params);
		this.setText("Scout Practice Match");
		this.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
		this.setTextColor(ContextCompat.getColor(context, R.color.textColor));

		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, MatchFormActivity.class);
				context.startActivity(intent);
			}
		});
	}
}
