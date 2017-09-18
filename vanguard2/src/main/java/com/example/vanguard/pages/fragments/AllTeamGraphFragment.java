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
 * Created by mbent on 7/5/2017.
 */

public class AllTeamGraphFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_phony_all_team_graphs, container, false);
	}
}
