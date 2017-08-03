package com.example.vanguard.Pages.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.CustomUIElements.ErrorTextView;
import com.example.vanguard.CustomUIElements.TeamListElement;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Pages.Activities.PitFormActivity;
import com.example.vanguard.PitTeamListView;
import com.example.vanguard.R;

import static com.example.vanguard.Pages.Activities.MatchFormActivity.TEAM_NUMBER;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class PitScoutFragment extends Fragment {

	public PitScoutFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.scroll_linearlayout_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		final LinearLayout layout = (LinearLayout) getView().findViewById(R.id.linear_layout);

		if (MainActivity.databaseManager.isCurrentEventSet()) {
			PitTeamListView selector = new PitTeamListView(getActivity(), new TeamListElement.TeamSelectedListener() {
				@Override
				public void teamSelected(Context context, int team) {
					Intent intent = new Intent(context, PitFormActivity.class);
					intent.putExtra(TEAM_NUMBER, Integer.valueOf(team));
					context.startActivity(intent);
				}
			});

			layout.addView(selector);
		} else
			layout.addView(new ErrorTextView(getActivity(), R.string.set_event_error));
	}
}
