package com.example.vanguard.CustomUIElements;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by mbent on 8/5/2017.
 */

public class AnswerUISeekbar extends LinearLayout implements AnswerUIInteger {

	int minValue;
	int maxValue;
	int step;
	SeekBar seekBar;
	TextView minValueTextView;
	TextView maxValueTextView;
	TextView currentValueTextView;

	public AnswerUISeekbar(Context context, int minValue, int maxValue, int step) {
		super(context);
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.step = step;
		this.seekBar = new SeekBar(context);
		ViewGroup.LayoutParams maxWidthWrapHeight = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		this.seekBar.setLayoutParams(maxWidthWrapHeight);

		this.addView(this.seekBar);
		this.setOrientation(VERTICAL);

		this.setLayoutParams(maxWidthWrapHeight);
		LinearLayout descriptionLayout = new LinearLayout(context);
		descriptionLayout.setOrientation(HORIZONTAL);
		descriptionLayout.setLayoutParams(maxWidthWrapHeight);

		this.minValueTextView = new TextView(context);
		this.maxValueTextView = new TextView(context);
		this.currentValueTextView = new TextView(context);

		LayoutParams valueTextViewLayoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
		valueTextViewLayoutParams.weight = 1;
		LayoutParams rightLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		this.minValueTextView.setLayoutParams(valueTextViewLayoutParams);
		this.maxValueTextView.setLayoutParams(rightLayoutParams);
		this.currentValueTextView.setLayoutParams(valueTextViewLayoutParams);


		descriptionLayout.addView(this.minValueTextView);
		descriptionLayout.addView(this.currentValueTextView);
		descriptionLayout.addView(this.maxValueTextView);


		this.addView(descriptionLayout);

		setupSeekbar();
	}

	private void setupSeekbar() {
		this.seekBar.setMax(this.maxValue - this.minValue);
		this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (progress == (maxValue - minValue)) {
					seekBar.setProgress(maxValue - minValue);
				} else {
					seekBar.setProgress((progress / step) * step);
				}
				currentValueTextView.setText(String.valueOf(seekBar.getProgress() + minValue));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});

		this.minValueTextView.setText(String.valueOf(this.minValue));
		this.maxValueTextView.setText(String.valueOf(this.maxValue));
	}

	@Override
	public Integer getValue() {
		return this.seekBar.getProgress() + this.minValue;
	}

	@Override
	public void setValue(Integer value) {
		this.seekBar.setProgress(value - this.minValue);
	}

	@Override
	public void setMinValue(int minValue) {
		this.minValue = minValue;
		setupSeekbar();
	}

	@Override
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		setupSeekbar();
	}

	@Override
	public void setIncrementation(int incrementation) {
		this.step = incrementation;
		setupSeekbar();
	}
}
