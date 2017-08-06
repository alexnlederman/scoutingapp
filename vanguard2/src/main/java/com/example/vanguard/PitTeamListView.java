package com.example.vanguard;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vanguard.CustomUIElements.TeamListElement;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;

import java.util.List;

/**
 * Created by mbent on 7/10/2017.
 */

public class PitTeamListView extends ListView {

	AnswerList<Question> pitQuestions;
	Context context;
	TeamListElement.TeamSelectedListener listener;
	List<Integer> teams;


	public PitTeamListView(Activity context, TeamListElement.TeamSelectedListener listener) {
		super(context);
		this.context = context;
		this.listener = listener;
		this.teams = MainActivity.databaseManager.getCurrentEventTeams();

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

		this.setLayoutParams(layoutParams);
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		this.pitQuestions = MainActivity.databaseManager.getPitQuestions();
		this.setAdapter(new ProgressListAdapter(this.teams));
	}

	private class ProgressListAdapter extends ArrayAdapter<Integer> {

		LayoutInflater inflater;
		List<Integer> teams;

		public ProgressListAdapter(List<Integer> teams) {
			super(context, R.layout.list_element_pit_team, teams);
			this.teams = teams;
			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@NonNull
		@Override
		public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			View layout;
			if (convertView != null) {
				layout = convertView;
			} else {
				layout = this.inflater.inflate(R.layout.list_element_pit_team, parent, false);
			}
			final int team = this.teams.get(position);
			TextView teamNumberTextView = (TextView) layout.findViewById(R.id.pit_scout_team_number);
			teamNumberTextView.setText(String.valueOf(team));
			teamNumberTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.teamSelected(context, teams.get(position));
				}
			});

			final ProgressBar bar = (ProgressBar) layout.findViewById(R.id.pit_scout_progress);

			AsyncTask<Void, Void, Integer> asyncTask = new AsyncTask<Void, Void, Integer>() {
				@Override
				protected Integer doInBackground(Void... params) {
					float i = 0;

					for (Question question : pitQuestions) {
						i += (question.getTeamResponses(team, false).size() > 0) ? 1 : 0;
					}

					return Math.round((i / pitQuestions.size()) * 100);
				}

				@Override
				protected void onPostExecute(Integer integer) {
					bar.setProgress(integer);
				}
			};
			asyncTask.execute();

			return layout;
		}
	}
}
