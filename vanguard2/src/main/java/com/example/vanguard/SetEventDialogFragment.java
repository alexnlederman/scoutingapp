package com.example.vanguard;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.vanguard.Pages.Activities.MainActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mbent on 6/30/2017.
 */

public class SetEventDialogFragment extends DialogFragment {

	Activity context;

	public static SetEventDialogFragment newInstance() {

		Bundle args = new Bundle();

		SetEventDialogFragment fragment = new SetEventDialogFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		View v = inflater.inflate(R.layout.set_event_dialog, null);
		final RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radio_group);
		final List<RadioButton> buttons = new ArrayList<>();

		List<String> eventNames = MainActivity.databaseManager.getEventNames();
		for (String eventName : eventNames) {
			RadioButton button = createRadioButton(eventName);
			System.out.println("Button Id1: " + button.getId());
			radioGroup.addView(button);
			buttons.add(button);
			System.out.println("Button Id2: " + button.getId());
		}

		builder.setView(v).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				for (RadioButton button : buttons) {
					if (button.isChecked()) {
						String eventName = button.getText().toString();
						MainActivity.databaseManager.setCurrentEvent(eventName);
						Toast.makeText(context, "Restart App For Settings to Take into Effect", Toast.LENGTH_LONG).show();
					}
				}
			}
		});

		return builder.create();
	}

	private RadioButton createRadioButton(String text) {
		System.out.println(context);
		RadioButton button = new RadioButton(context);
		button.setText(text);
		int padding = Math.round(MainActivity.dpToPixels * 16);
		button.setPadding(0, padding, 0, padding);
		button.setTextSize(16);
		return button;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = activity;
	}
}
