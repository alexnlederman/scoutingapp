package com.example.vanguard.Pages.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.DatabaseManager;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Questions.QuestionViewers.QuestionListViewers.QuestionListEditViewer;
import com.example.vanguard.R;

/**
 * Created by mbent on 7/3/2017.
 */

public class ScoutSettingsFragment extends Fragment {

	QuestionListEditViewer selector;

	public ScoutSettingsFragment() {
		setHasOptionsMenu(true);
	}

	public static ScoutSettingsFragment newInstance(boolean isMatchForm) {

		Bundle args = new Bundle();

		args.putBoolean("match_form", isMatchForm);

		ScoutSettingsFragment fragment = new ScoutSettingsFragment();
		fragment.setArguments(args);
		return fragment;
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
		LinearLayout layout = (LinearLayout) getView().findViewById(R.id.linear_layout);
		selector = new QuestionListEditViewer(getActivity(), MainActivity.databaseManager, getArguments().getBoolean("match_form"));
		System.out.println("View Added");
		layout.addView(selector);
	}

	public void addStringQuestion() {
		selector.addStringQuestion();
	}

	public void addIntegerQuestion() {
		selector.addIntegerQuestion();
	}
}