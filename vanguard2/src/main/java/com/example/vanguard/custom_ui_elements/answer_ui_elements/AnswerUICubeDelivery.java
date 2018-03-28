package com.example.vanguard.custom_ui_elements.answer_ui_elements;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.vanguard.AnswerUI;
import com.example.vanguard.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mbent on 3/13/2018.
 */

public class AnswerUICubeDelivery extends FrameLayout implements AnswerUI<Map<String, Number>> {

	Button exchangeButton;
	Button portalButton;
	Button scaleButton;
	Button oppositionSwitchInsideButton;
	Button allianceSwitchInsideButton;
	Button oppositionSwitchOutsideButton;
	Button allianceSwitchOutsideButton;
	Map<String, Number> cubeCount;
	boolean firstButtonPressed = false;
	double startTime;
	List<Double> times;
	String currentDropoffButtonText;

	LinearLayout deliverySuccessLayout;

	public AnswerUICubeDelivery(Context context) {
		super(context);
		times = new ArrayList<>();
		cubeCount = new HashMap<>();
		this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


		deliverySuccessLayout = new LinearLayout(context);
		deliverySuccessLayout.setOrientation(LinearLayout.HORIZONTAL);
		deliverySuccessLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		Button deliveryFailedButton = new Button(context);
		deliveryFailedButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.failed_cube_delivery_button_color));
		deliveryFailedButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
		deliveryFailedButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deliverySuccessLayout.setVisibility(INVISIBLE);
				String dropoffKey = "Failed Dropoff " + currentDropoffButtonText;
				cubeCount.put(dropoffKey, cubeCount.get(dropoffKey).intValue() + 1);
				resetButtons();
			}
		});
		deliverySuccessLayout.addView(deliveryFailedButton);

		Button deliverySucceededButton = new Button(context);
		deliverySucceededButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.success_cube_delivery_button_color));
		deliverySucceededButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
		deliverySucceededButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deliverySuccessLayout.setVisibility(INVISIBLE);
				String dropoffKey = "Success Dropoff " + currentDropoffButtonText;
				cubeCount.put(dropoffKey, cubeCount.get(dropoffKey).intValue() + 1);
				resetButtons();
			}
		});
		deliverySuccessLayout.addView(deliverySucceededButton);

		deliverySuccessLayout.setVisibility(INVISIBLE);



		LinearLayout cubeLocationLayout = new LinearLayout(context);
		cubeLocationLayout.setOrientation(LinearLayout.VERTICAL);
		cubeLocationLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		exchangeButton = createButton(context, "Exchange", cubeLocationLayout);
		portalButton = createButton(context, "Portal", cubeLocationLayout);
		scaleButton = createButton(context, "Scale", cubeLocationLayout);
		oppositionSwitchInsideButton = createButton(context, "Opposition Switch Inside", cubeLocationLayout);
		allianceSwitchInsideButton = createButton(context, "Alliance Switch Inside", cubeLocationLayout);
		oppositionSwitchOutsideButton = createButton(context, "Opposition Switch Outside", cubeLocationLayout);
		allianceSwitchOutsideButton = createButton(context, "Alliance Switch Outside", cubeLocationLayout);

		this.addView(cubeLocationLayout);
		this.addView(deliverySuccessLayout);
	}

	private void resetButtons() {
		exchangeButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_not_selected));
		portalButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_not_selected));
		scaleButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_not_selected));
		oppositionSwitchInsideButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_not_selected));
		oppositionSwitchOutsideButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_not_selected));
		allianceSwitchInsideButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_not_selected));
		allianceSwitchOutsideButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_not_selected));
		this.portalButton.setVisibility(VISIBLE);
	}

	private void hideDropoffButtons() {
		this.portalButton.setVisibility(INVISIBLE);
	}

	private Button createButton(Context context, final String text, LinearLayout layout) {
		final Button button = new Button(context);
		button.setText(text);
		final String pickupKey = "Pickup " + text;
		String failedDropoffKey = "Failed Dropoff " + text;
		String successDropoffKey = "Success Dropoff " + text;
		cubeCount.put(pickupKey, 0);
		cubeCount.put(failedDropoffKey, 0);
		cubeCount.put(successDropoffKey, 0);
		button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_not_selected));
		button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!firstButtonPressed) {
					firstButtonPressed = true;
					button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_selected));
					startTime = System.currentTimeMillis();
					cubeCount.put(pickupKey, cubeCount.get(pickupKey).intValue() + 1);
					hideDropoffButtons();
				} else {
					firstButtonPressed = false;
					currentDropoffButtonText = text;
					button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_selected));
					times.add((System.currentTimeMillis() - startTime) / 1000);
					deliverySuccessLayout.setVisibility(VISIBLE);
				}
			}
		});

		layout.addView(button);
		return button;
	}

	@Override
	public Map<String, Number> getValue() {
		double averageTime = 0;
		if (times.size() > 0) {
			for (Double time : times) {
				averageTime += time;
			}
			averageTime = averageTime / times.size();
		}
		cubeCount.put("Average Time", averageTime);
		return cubeCount;
	}

	@Override
	public void setValue(Map<String, Number> value) {
		this.cubeCount = value;
	}
}
