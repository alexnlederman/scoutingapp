package com.example.vanguard;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.pages.activities.MatchFormActivity;

import java.io.Console;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by mbent on 6/27/2017.
 */

public class MatchScoutSelector extends ListView {

	AppCompatActivity context;

	public MatchScoutSelector(AppCompatActivity context) {
		super(context);
		this.context = context;
		List<Map<String, Object>> eventMatches = MainActivity.databaseManager.getCurrentEventMatches();
		ListAdapter listAdapter = new ListAdapter(eventMatches);
		this.setAdapter(listAdapter);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		this.setLayoutParams(layoutParams);
	}

	private class ListAdapter extends ArrayAdapter<Map<String, Object>> {

		List<Map<String, Object>> values;
		LayoutInflater inflater;

		private ListAdapter(List<Map<String, Object>> values) {
			super(context, R.layout.scout_selector_ui, values);
			this.values = values;
			Log.d("VALUES", this.values.toString());
			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@NonNull
		@Override
		public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			final View layout;
			if (convertView == null) {
				if (position == 0) {
					return setupButton(this.inflater.inflate(R.layout.scout_practice_match_button, parent, false));
				} else {
					layout = this.inflater.inflate(R.layout.scout_selector_ui, parent, false);
				}
			} else {
				if (convertView.findViewById(R.id.qual_indicator) == null) {
					if (position == 0) {
						return setupButton(convertView);
					} else {
						layout = this.inflater.inflate(R.layout.scout_selector_ui, parent, false);
					}
				} else {
					if (position == 0) {
						return setupButton(this.inflater.inflate(R.layout.scout_practice_match_button, parent, false));
					} else {
						layout = convertView;
					}
				}
			}
			position--;

			Map<String, Object> map = this.values.get(position);
			int qualNumber = (int) map.get(DatabaseManager.event_match_number_key);
			List<String> blueTeamsList = (List<String>) map.get(DatabaseManager.event_match_blue_team);
			List<String> redTeamsList = (List<String>) map.get(DatabaseManager.event_match_red_team);

			((TextView) layout.findViewById(R.id.qual_indicator)).setText(String.format(Locale.US, "Qual\n%d", qualNumber));
			Button red1 = ((Button) layout.findViewById(R.id.red1));
			Button red2 = ((Button) layout.findViewById(R.id.red2));
			Button red3 = ((Button) layout.findViewById(R.id.red3));
			red1.setText(redTeamsList.get(0).substring(3));
			red2.setText(redTeamsList.get(1).substring(3));
			red3.setText(redTeamsList.get(2).substring(3));

			Button blue1 = ((Button) layout.findViewById(R.id.blue1));
			Button blue2 = ((Button) layout.findViewById(R.id.blue2));
			Button blue3 = ((Button) layout.findViewById(R.id.blue3));
			blue1.setText(blueTeamsList.get(0).substring(3));
			blue2.setText(blueTeamsList.get(1).substring(3));
			blue3.setText(blueTeamsList.get(2).substring(3));


			setupButtons(qualNumber, blue1, blue2, blue3, red1, red2, red3);

			return layout;
		}

		private View setupButton(View view) {
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, MatchFormActivity.class);
					context.startActivity(intent);
				}
			});
			return view;
		}

		private void setupButtons(int qualNumber, Button... buttons) {
			for (Button button : buttons) {
				button.setOnClickListener(new OnClick(qualNumber));
				button.setContentDescription(button.getText());
			}
		}
	}

	private class OnClick implements View.OnClickListener {

		int qualNumber;

		public OnClick(int qualNumber) {
			this.qualNumber = qualNumber;
		}

		@Override
		public void onClick(View v) {
			int teamNumber = Integer.valueOf(String.valueOf(v.getContentDescription()));
			Intent intent = new Intent(context, MatchFormActivity.class);
			intent.putExtra(MatchFormActivity.QUAL_NUMBER, this.qualNumber);
			intent.putExtra(MatchFormActivity.TEAM_NUMBER, teamNumber);
			context.startActivity(intent);
		}
	}
}
