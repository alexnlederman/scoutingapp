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
import com.example.vanguard.pages.activities.AbstractActivity;

/**
 * Created by mbent on 7/7/2017.
 */

public class TeamGraphViewerFragment extends AbstractActivity {

	public final static String TEAM_NUMBER = "team_number";

	public TeamGraphViewerFragment() {
		super(R.layout.fragment_phony_single_team_graphs, R.string.graph_page_title);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int teamNumber = getIntent().getExtras().getInt(TEAM_NUMBER);
		this.setTitle("Team " + teamNumber);
	}
}
