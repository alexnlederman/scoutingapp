package com.example.vanguard.custom_ui_elements.answer_ui_elements;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vanguard.AnswerUI;
import com.example.vanguard.R;

import java.util.Locale;

/**
 * Created by mbent on 8/18/2017.
 */

public class AnswerUITimer extends LinearLayout implements AnswerUI<Double> {


	private static final long DELAY = 10;
	private final static String RESET_VALUE = "0.0";
	TextView timerTextView;
	Handler handler;
	ClockTimer clockTimer;
	ImageButton pausePlayButton;
	ImageButton resetButton;
	boolean paused = true;
	Context context;
	boolean isMatchQuestion;

	public AnswerUITimer(Context context, boolean isMatchQuestion) {
		super(context);
		this.context = context;
		this.isMatchQuestion = isMatchQuestion;

		this.setOrientation(HORIZONTAL);
		this.handler = new Handler();
		this.clockTimer = new ClockTimer();

		this.timerTextView = new TextView(context);

		this.addView(this.timerTextView);

		this.pausePlayButton = new ImageButton(context);
		showPlayButton();
		this.pausePlayButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggle();
			}
		});

		this.resetButton = new ImageButton(context);
		this.resetButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_reset));
		this.resetButton.setVisibility(GONE);
		this.resetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reset();
			}
		});

		this.addView(this.resetButton);

		this.addView(this.pausePlayButton);
	}

	private void toggle() {
		if (this.paused)
			play();
		else
			pause();
	}

	private void showPauseButton() {
		this.pausePlayButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pause_logo));
	}

	private void showPlayButton() {
		this.pausePlayButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_play_arrow));
	}

	private void play() {
		this.resetButton.setVisibility(VISIBLE);
		this.paused = false;
		this.clockTimer.updatePrevTime();
		this.handler.postDelayed(this.clockTimer, DELAY);
		this.showPauseButton();
	}

	private void pause() {
		this.paused = true;
		this.handler.removeCallbacks(this.clockTimer);
		this.showPlayButton();
	}

	private void reset() {
		if (this.paused) {
			this.resetButton.setVisibility(GONE);
		}
		this.clockTimer.resetTime();
		this.timerTextView.setText(RESET_VALUE);
	}

	@Override
	public Double getValue() {
		String text = this.timerTextView.getText().toString();
		if (text.equals("")) {
			if (this.isMatchQuestion) {
				return 0d;
			}
			return null;
		}
		return Double.valueOf(text);
	}

	@Override
	public void setValue(Double value) {
		if (value != null) {
			this.timerTextView.setText(String.format(Locale.US, "%.1f", value));
		}
	}

	private class ClockTimer implements Runnable {

		private Long previousTime;
		private Double time;

		@Override
		public void run() {
			Double currentVal = getValue();
			if (currentVal == null) this.time = 0d;
			if (this.time == null) this.time = currentVal;
			this.time = this.time + ((System.currentTimeMillis() - this.previousTime) / 1000f);
			timerTextView.setText(String.format(Locale.US, "%.1f", time));
			this.previousTime = System.currentTimeMillis();
			handler.postDelayed(this, DELAY);
		}

		private void updatePrevTime() {
			this.previousTime = System.currentTimeMillis();
		}

		private void resetTime() {
			this.time = 0d;
		}
	}
}
