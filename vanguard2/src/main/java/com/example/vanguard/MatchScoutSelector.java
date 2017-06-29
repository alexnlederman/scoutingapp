package com.example.vanguard;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Pages.Activities.MatchFormActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by mbent on 6/27/2017.
 */

public class MatchScoutSelector extends ListView {

	Context context;

	public MatchScoutSelector(Context context) {
		super(context);
//		for (String[] array : MainActivity.databaseManager.getEvents()) {
//			System.out.println("Event");
//			System.out.println(array[0]);
//			System.out.println(array[1]);
//		}
		this.setDividerHeight(0);
		this.context = context;
		List<Map<String, Object>> eventMatches = MainActivity.databaseManager.getEventMatches("2016nytr");
		ListAdapter listAdapter = new ListAdapter(eventMatches);
		this.setAdapter(listAdapter);
	}

	private class ListAdapter extends ArrayAdapter<Map<String, Object>> {

		List<Map<String, Object>> values;

		public ListAdapter(List<Map<String, Object>> values) {
			super(context, -1, values);
			this.values = values;
		}

		@NonNull
		@Override
		public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			System.out.println("Pos: " + position);
			int qualNumber = (int) this.values.get(position).get(DatabaseManager.event_match_number_key);
			List<String> blueTeamsList = (List<String>) this.values.get(position).get(DatabaseManager.event_match_blue_team);
			List<String> redTeamsList = (List<String>) this.values.get(position).get(DatabaseManager.event_match_red_team);
			return new MatchScoutQualUI(context, qualNumber, blueTeamsList, redTeamsList, new OnClick(qualNumber));
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
			intent.putExtra(MatchFormActivity.QUAL_NUMBER, qualNumber);
			intent.putExtra(MatchFormActivity.TEAM_NUMBER, teamNumber);
			context.startActivity(intent);
		}
	}
}
