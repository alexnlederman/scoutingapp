package com.example.vanguard.Questions;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.NumberPicker;

import com.example.vanguard.CustomWidgets.CustomNumberPicker;

/**
 * Created by BertTurtle on 4/23/2017.
 */

public class DoubleQuestion extends GenericQuestion {


	public DoubleQuestion(Context context, String question) {
		super(context, question);
	}

	@Override
	protected void addInput() {
		CustomNumberPicker numberPicker = new CustomNumberPicker(context);
		this.addView(numberPicker);
//		this.setBackgroundColor(Color.parseColor("#ff3422"));
		numberPicker.setGravity(Gravity.RIGHT);
		ViewGroup.LayoutParams p = numberPicker.getLayoutParams();
		System.out.println(p.width);
		p.width = 400;
		numberPicker.setLayoutParams(p);
	}
}
