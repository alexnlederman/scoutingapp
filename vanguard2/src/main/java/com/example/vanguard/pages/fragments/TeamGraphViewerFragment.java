package com.example.vanguard.pages.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.custom_ui_elements.ErrorTextView;
import com.example.vanguard.graphs.GraphViewer;
import com.example.vanguard.R;

/**
 * Created by mbent on 7/7/2017.
 */

public class TeamGraphViewerFragment extends Fragment {

	public static TeamGraphViewerFragment newInstance(int teamNumber) {

		Bundle args = new Bundle();
		args.putInt("team_number", teamNumber);
		TeamGraphViewerFragment fragment = new TeamGraphViewerFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_team_graph_viewer, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.fragment_graph_layout);
		GraphViewer graphViewer = new GraphViewer(getActivity(), getArguments().getInt("team_number"));
		if (graphViewer.getGraphCount() == 0)
			layout.addView(new ErrorTextView(getActivity(), R.string.add_graph_error));
		else
			layout.addView(graphViewer);
	}
}
