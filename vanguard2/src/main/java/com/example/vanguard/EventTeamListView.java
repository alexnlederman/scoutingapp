package com.example.vanguard;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vanguard.custom_ui_elements.TeamListElement;
import com.example.vanguard.pages.activities.MainActivity;

import java.util.List;

/**
 * Created by mbent on 6/29/2017.
 */

public class EventTeamListView extends ListView {

	Activity context;
	TeamListElement.TeamSelectedListener listener;
	List<Integer> teams;

	public EventTeamListView(Activity context, TeamListElement.TeamSelectedListener listener) {
		super(context);
		this.listener = listener;

		this.context = context;
		this.teams = MainActivity.databaseManager.getCurrentEventTeams();
		TeamListAdapter teamListAdapter = new TeamListAdapter(this.teams);
		this.setAdapter(teamListAdapter);
	}

	private class TeamListAdapter extends ArrayAdapter<Integer> {

		List<Integer> values;
		LayoutInflater inflater;

		private TeamListAdapter(List<Integer> values) {
			super(context, -1, values);
			this.values = values;
			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@NonNull
		@Override
		public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			View layout;
			if (convertView != null) {
				layout = convertView;
			} else {
				layout = this.inflater.inflate(R.layout.list_element_graph, parent, false);
			}
			TextView teamNumberTextView = (TextView) layout.findViewById(R.id.graph_teams_number);
			teamNumberTextView.setText(String.valueOf(values.get(position)));
			teamNumberTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.teamSelected(context, values.get(position));
				}
			});
			return layout;
		}
	}
}
