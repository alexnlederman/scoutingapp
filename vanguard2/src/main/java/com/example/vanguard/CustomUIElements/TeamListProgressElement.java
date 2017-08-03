package com.example.vanguard.CustomUIElements;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.R;

/**
 * Created by mbent on 7/10/2017.
 */

public class TeamListProgressElement extends TeamListElement {

	ProgressBar bar;
	int team;

	public TeamListProgressElement(Activity context, Integer team, AnswerList<Question> pitQuestions) {
		super(context, team);

		this.team = team;

		this.bar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
		this.bar.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.team_progress_bar));


		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		this.bar.setLayoutParams(params);
		this.addView(this.bar);
		this.setGravity(Gravity.CENTER_VERTICAL);

		updateQuestions(pitQuestions);
	}

	public void updateQuestions(final AnswerList<Question> pitQuestions) {

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
	}

	public int getTeam() {
		return team;
	}
}
