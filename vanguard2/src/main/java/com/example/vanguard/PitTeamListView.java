package com.example.vanguard;

import android.app.Activity;
import android.support.annotation.IntDef;
import android.view.View;
import android.widget.ListAdapter;

import com.example.vanguard.CustomUIElements.TeamListElement;
import com.example.vanguard.CustomUIElements.TeamListProgressElement;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbent on 7/10/2017.
 */

public class PitTeamListView extends EventTeamListView {

	AnswerList<Question> pitQuestions;


	public PitTeamListView(Activity context, TeamListElement.TeamSelectedListener listener) {
		super(context, listener);
		this.pitQuestions = MainActivity.databaseManager.getPitQuestions();
	}

	private class ProgressListAdapter extends TeamListAdapter {

		public ProgressListAdapter(List<Integer> teams) {
			super(teams);
		}

		@Override
		protected View getViewElement(int position) {
			TeamListProgressElement element = new TeamListProgressElement(context, values.get(position), pitQuestions);
			return element;
		}
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		this.pitQuestions = MainActivity.databaseManager.getPitQuestions();
		this.setAdapter(new ProgressListAdapter(this.teams));
	}
}
