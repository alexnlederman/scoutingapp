package com.example.vanguard.pages.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanguard.R;
import com.example.vanguard.questions.question_viewers.question_list_viewers.edit_list_viewer.QuestionDecoration;
import com.example.vanguard.questions.question_viewers.question_list_viewers.edit_list_viewer.QuestionListViewAdapter;
import com.example.vanguard.questions.question_viewers.question_list_viewers.edit_list_viewer.QuestionTouchCallback;

/**
 * Created by mbent on 7/3/2017.
 */

public class ScoutSettingsFragment extends Fragment {

	RecyclerView selector;
	QuestionListViewAdapter adapter;

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

	private void setupRecyclerView() {
		this.adapter = new QuestionListViewAdapter(getActivity(), isMatchForm());

//		this.selector.setHasFixedSize(true);
		this.selector.setAdapter(this.adapter);
		this.selector.setLayoutManager(new LinearLayoutManager(this.getActivity()));


		QuestionTouchCallback callback = new QuestionTouchCallback(getActivity(), this.adapter);
		ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
		touchHelper.attachToRecyclerView(this.selector);


		this.selector.addItemDecoration(new QuestionDecoration());
		this.selector.setHasFixedSize(true);
	}

	// TODO editing label doesn't work.
	public QuestionListViewAdapter getAdapter() {
		return this.adapter;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_question_edit_view, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.selector = (RecyclerView) getView().findViewById(R.id.question_edit_list_view);
		setupRecyclerView();
	}

	public boolean isMatchForm() {
		return getArguments().getBoolean("match_form");
	}


}
