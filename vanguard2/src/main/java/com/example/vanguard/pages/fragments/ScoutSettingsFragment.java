package com.example.vanguard.pages.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.R;
import com.example.vanguard.questions.question_viewers.question_list_viewers.QuestionListEditViewer;

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
		this.selector = new QuestionListEditViewer(getActivity(), getArguments().getBoolean("match_form"));
		layout.addView(selector);
	}

	public boolean isMatchForm() {
		return getArguments().getBoolean("match_form");
	}
}