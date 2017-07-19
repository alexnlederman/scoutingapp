package com.example.vanguard;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.vanguard.CustomUIElements.TeamListElement;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;

import java.util.ArrayList;
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

		this.setDividerHeight(0);
		this.context = context;
		this.teams = getTeams();
		TeamListAdapter teamListAdapter = new TeamListAdapter(this.teams);
		this.setAdapter(teamListAdapter);
	}

	protected List<Integer> getTeams() {
		return MainActivity.databaseManager.getCurrentEventTeams();
	}

	protected class TeamListAdapter extends ArrayAdapter<Integer> {

		List<Integer> values;

		public TeamListAdapter(List<Integer> values) {
			super(context, -1, values);
			this.values = values;
		}

		@NonNull
		@Override
		public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			View element = getViewElement(position);
			element.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.teamSelected(context, values.get(position));
				}
			});
			return element;
		}

		protected View getViewElement(int position) {
			return new TeamListElement(context, values.get(position));
		}
	}
}
