package com.example.vanguard.CustomUIElements;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.vanguard.AnswerUI;
import com.example.vanguard.Pages.Activities.MainActivity;

/**
 * A integer number text box with plus and minus buttons.
 * Created by BertTurtle on 4/23/2017.
 */
public class CustomNumberPicker extends LinearLayout implements AnswerUI<Integer>{

	private final EditText numberPicker;

	private final LinearLayout buttonLayout;

	private final Button plusButton;

	private final Button minusButton;

	/**
	 * Creates a new CustomNumberPicker
	 * @param context Application context
	 * {@inheritDoc}
	 */
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

		setupLayout();
	}

	private void setupNumberPicker() {
		this.numberPicker.setInputType(InputType.TYPE_CLASS_NUMBER);
		this.numberPicker.setText("0");
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

	@Override
	public void setValue(Integer value) {
		this.numberPicker.setText(String.valueOf(value));
	}

	/**
	 * Get value of number picker.
	 * @return The value of the number picker.
	 */
	@Override
	public Integer getValue() {
		return Integer.parseInt(this.numberPicker.getText().toString());
	}

	private void setupLayout() {
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(95 * (int) MainActivity.dpToPixels, LayoutParams.MATCH_PARENT);
		this.setLayoutParams(p);
	}
}
