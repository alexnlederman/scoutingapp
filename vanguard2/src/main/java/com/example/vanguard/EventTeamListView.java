package com.example.vanguard;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.vanguard.Pages.Activities.MainActivity;

import java.util.List;

/**
 * Created by mbent on 6/29/2017.
 */

public class EventTeamListView extends ListView {

	Activity context;
	TeamListElement.TeamSelectedListener listener;

	public EventTeamListView(Activity context, TeamListElement.TeamSelectedListener listener) {
		super(context);
		this.listener = listener;
		List<String> eventInfo = MainActivity.databaseManager.getCurrentEventInfo();
		if (eventInfo != null) {
			List<Integer> teams = MainActivity.databaseManager.getEventTeams(eventInfo.get(0));


			this.setDividerHeight(0);
			this.context = context;
			ListAdapter listAdapter = new ListAdapter(teams);
			this.setAdapter(listAdapter);
		}
	}

	private class ListAdapter extends ArrayAdapter<Integer> {

		List<Integer> values;

		public ListAdapter(List<Integer> values) {
			super(context, -1, values);
			this.values = values;
		}

		@NonNull
		@Override
		public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			return new TeamListElement(context, values.get(position), listener);
		}
	}
}
