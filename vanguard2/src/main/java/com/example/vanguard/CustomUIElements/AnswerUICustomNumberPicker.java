package com.example.vanguard.CustomUIElements;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.R;

/**
 * A integer number text box with plus and minus buttons.
 * Created by BertTurtle on 4/23/2017.
 */
public class AnswerUICustomNumberPicker extends LinearLayout implements AnswerUIInteger {

	private final EditText numberPicker;

	private final LinearLayout buttonLayout;

	private final Button plusButton;

	private final Button minusButton;

	private int minValue;
	private int maxValue;
	private int incremenation;

	/**
	 * Creates a new CustomNumberPicker
	 *
	 * @param context Application context
	 *                {@inheritDoc}
	 */
	public AnswerUICustomNumberPicker(Context context, int minValue, int maxValue, int incrementation) {
		super(context);

		this.setOrientation(LinearLayout.VERTICAL);

		this.minValue = minValue;
		this.maxValue = maxValue;
		this.incremenation = incrementation;

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

		setupLayout();
	}

	private void setupNumberPicker() {
		this.numberPicker.setInputType(InputType.TYPE_CLASS_NUMBER);

		this.setValue(0);
		this.numberPicker.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
		this.numberPicker.setGravity(Gravity.CENTER);
		this.numberPicker.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
		this.addView(this.numberPicker);
	}

	private void setupGenericButton(Button button) {
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		p.weight = 0.5f;
		button.setLayoutParams(p);
		button.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
		button.setTextSize(20);
		Typeface typeface = Typeface.defaultFromStyle(Typeface.NORMAL);
		button.setTypeface(typeface);

		button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
		this.buttonLayout.addView(button);
	}

	private void setupMinusButton() {
		this.minusButton.setText("-");
		this.minusButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Integer value = getValue();
				value = (value == null) ? getDefaultValue() : value;
				setValue(value - incremenation);
			}
		});
	}

	private int getDefaultValue() {
		return (this.minValue > 0) ? this.minValue : 0;
	}

	private void setupPlusButton() {
		this.plusButton.setText("+");
		this.plusButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Integer value = getValue();
				value = (value == null) ? getDefaultValue() : value;
				setValue(value + incremenation);
			}
		});
	}

	private void setupButtonLayout() {
		this.buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
		this.addView(this.buttonLayout);
	}

	/**
	 * Get value of number picker.
	 *
	 * @return The value of the number picker.
	 */
	@Override
	public Integer getValue() {
		if (!this.numberPicker.getText().toString().equals("")) {
			return Integer.parseInt(this.numberPicker.getText().toString());
		} else {
			return null;
		}
	}

	@Override
	public void setValue(Integer value) {
		if (value == null) {
			this.numberPicker.setText("");
		} else {
			if (value > this.maxValue)
				this.numberPicker.setText(String.valueOf(maxValue));
			else if (value < this.minValue)
				this.numberPicker.setText(String.valueOf(minValue));
			else {
				this.numberPicker.setText(String.valueOf(value));
			}
		}
	}

	private void setupLayout() {
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(125 * (int) MainActivity.dpToPixels, LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(p);
	}

	@Override
	public void setMinValue(int minValue) {
		this.minValue = minValue;
		this.setValue(getValue());
	}

	@Override
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		this.setValue(getValue());
	}

	@Override
	public void setIncrementation(int incrementation) {
		this.incremenation = incrementation;
	}
}
