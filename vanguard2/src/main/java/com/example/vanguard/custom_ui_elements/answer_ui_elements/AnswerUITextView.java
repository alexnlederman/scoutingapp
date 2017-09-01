package com.example.vanguard.custom_ui_elements.answer_ui_elements;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.AnswerUI;
import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.R;


/**
 * Created by mbent on 7/9/2017.
 */

public class AnswerUITextView extends AppCompatTextView implements AnswerUI<Integer> {
	public AnswerUITextView(Context context) {
		super(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.weight = 2.2f;
		// TODO make sure the weight works with more devices.
		int margin = Math.round(MainActivity.dpToPixels * 20);
		params.setMargins(margin, margin, margin, margin);
		this.setLayoutParams(params);

		this.setTextColor(ContextCompat.getColor(context, R.color.textColor));
		this.setTextSize(20);
	}

	@Override
	public Integer getValue() {
		return Integer.valueOf(this.getText().toString());
	}

	@Override
	public void setValue(Integer value) {
		this.setText(String.valueOf(value));
	}
}
