package com.example.vanguard;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.vanguard.Pages.Activities.MainActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by mbent on 6/29/2017.
 */

public class PitScoutSelector extends ListView {

	Activity context;

	public PitScoutSelector(Activity context) {
		super(context);
		List<Integer> teams = MainActivity.databaseManager.getEventTeams("2016nytr");


		this.setDividerHeight(0);
		this.context = context;
		ListAdapter listAdapter = new ListAdapter(teams);
		this.setAdapter(listAdapter);
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
			return new PitScoutTeamUI(context, values.get(position));
		}
	}
}
