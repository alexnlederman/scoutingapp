package com.example.vanguard.CustomWidgets;

import android.content.Context;
import android.text.InputType;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by BertTurtle on 4/23/2017.
 */

public class CustomNumberPicker extends LinearLayout {

	private final EditText numberPicker;

	private final LinearLayout buttonLayout;

	private final Button plusButton;

	private final Button minusButton;

	public CustomNumberPicker(Context context) {
		super(context);

		this.setOrientation(LinearLayout.VERTICAL);

		this.numberPicker = new EditText(context);

		this.buttonLayout = new LinearLayout(context);

		this.plusButton = new Button(context);

		this.minusButton = new Button(context);

		setupNumberPicker();

		setupButtonLayout();

		setupPlusButton();
		setupMinusButton();

		setupGenericButton(this.minusButton);
		setupGenericButton(this.plusButton);
	}

	private void setupNumberPicker() {
		this.numberPicker.setInputType(InputType.TYPE_CLASS_NUMBER);
		this.numberPicker.setWidth(30);
		this.addView(this.numberPicker);
	}

	private void setupGenericButton(Button button) {
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		p.weight = 0.5f;
		button.setLayoutParams(p);
		this.buttonLayout.addView(button);
	}

	private void setupMinusButton() {
		this.minusButton.setText("-");
		this.minusButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					setValue(getValue() - 1);
				}
				catch (Exception e) {
					setValue(-1);
				}
			}
		});
	}

	private void setupPlusButton() {
		this.plusButton.setText("+");
		this.plusButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					setValue(getValue() + 1);
				}
				catch (Exception e) {
					setValue(1);
				}
			}
		});
	}

	private void setupButtonLayout() {
		this.buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
		this.addView(this.buttonLayout);
	}

	public void setValue(int value) {
		this.numberPicker.setText(String.valueOf(value));
	}

	public int getValue() {
		return Integer.parseInt(this.numberPicker.getText().toString());
	}
}
