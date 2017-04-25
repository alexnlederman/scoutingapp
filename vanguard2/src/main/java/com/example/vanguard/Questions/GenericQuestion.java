package com.example.vanguard.Questions;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.vanguard.MainActivity;

/**
 * Created by BertTurtle on 4/22/2017.
 */

public abstract class GenericQuestion extends LinearLayout {
	protected final Context context;
	private TextView questionTextView;

	public GenericQuestion(Context context, String question) {
		super(context);

		this.context = context;
		this.setOrientation(LinearLayout.HORIZONTAL);
//		setupLayout();

		addQuestionTextView(question);

		addInput();

	}

	private void setupLayout() {
		ViewGroup.LayoutParams p = this.getLayoutParams();
		p.height = 2 * (int) MainActivity.dpToPixels;
		System.out.println("Conversion: " + MainActivity.dpToPixels);
		this.setLayoutParams(p);
		this.setBackgroundColor(Color.parseColor("#458423"));
	}

	private void addQuestionTextView(String question) {
		this.questionTextView = new TextView(context);

		this.questionTextView.setText(question);

		this.questionTextView.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		p.weight = 0.5f;
//		p.height = 100 * (int) MainActivity.dpToPixels;
		this.questionTextView.setLayoutParams(p);
		this.addView(this.questionTextView);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		System.out.println("IT HAS BEEN DRAWN");
		setupLayout();
	}

	public void setQuestionText(String text) {
		this.questionTextView.setText(text);
	}

	protected abstract void addInput();
}