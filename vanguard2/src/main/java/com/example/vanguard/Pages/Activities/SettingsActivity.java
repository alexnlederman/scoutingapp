package com.example.vanguard.Pages.Activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.vanguard.AddEventDialogFragment;
import com.example.vanguard.CustomUIElements.SettingsView;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.R;
import com.example.vanguard.SetEventDialogFragment;

public class SettingsActivity extends AppCompatActivity {

	Activity that;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		that = this;


		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		SettingsView addEvent = (SettingsView) findViewById(R.id.add_event);
		addEvent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("Clicked");

				DialogFragment fragment = AddEventDialogFragment.newInstance(new AddEventDialogFragment.DialogOpener() {
					@Override
					public void openDialog(DialogFragment dialog) {
						dialog.show(getFragmentManager(), "Fragment");
					}

					@Override
					public int describeContents() {
						return 0;
					}

					@Override
					public void writeToParcel(Parcel dest, int flags) {

					}
				});
				fragment.show(getFragmentManager(), "Event Adder");
			}


		});

		SettingsView setEvent = (SettingsView) findViewById(R.id.set_event);
		setEvent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (MainActivity.databaseManager.getEventNames().size() == 0) {
					Toast.makeText(that, "Add events first", Toast.LENGTH_LONG).show();
				}
				else {
					DialogFragment fragment = SetEventDialogFragment.newInstance();
					fragment.show(getFragmentManager(), "Event Selector");
				}
			}
		});

		SettingsView scoutSettings = (SettingsView) findViewById(R.id.scout_settings);
		scoutSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(that, ScoutSettingsActivity.class);
				startActivity(intent);
			}
		});

		SettingsView graphSettings = (SettingsView) findViewById(R.id.graph_settings);
		graphSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(that, GraphSettingsActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onSupportNavigateUp() {
		System.out.println("Back");
		onBackPressed();
		return true;
	}
}
