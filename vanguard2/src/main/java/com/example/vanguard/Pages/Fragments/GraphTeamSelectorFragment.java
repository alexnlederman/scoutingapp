package com.example.vanguard.Pages.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.CustomUIElements.ErrorTextView;
import com.example.vanguard.EventTeamListView;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.R;
import com.example.vanguard.CustomUIElements.TeamListElement;

/**
 * Created by mbent on 7/7/2017.
 */

public class GraphTeamSelectorFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.blank_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.layout);
		if (MainActivity.databaseManager.isCurrentEventSet()) {
			layout.addView(new EventTeamListView(getActivity(), new TeamListElement.TeamSelectedListener() {
				@Override
				public void teamSelected(Context context, int team) {
					MainActivity.setFragment(TeamGraphViewerFragment.newInstance(team), R.id.fragment_fragment_holders, getActivity());
				}
			}));
		}
		else
			layout.addView(new ErrorTextView(getActivity(), R.string.set_event_error));
	}
}
