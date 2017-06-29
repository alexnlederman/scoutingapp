package com.example.vanguard.Pages.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.PitScoutSelector;
import com.example.vanguard.Questions.QuestionViewers.QuestionListViewers.QuestionListFormViewer;
import com.example.vanguard.R;


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

		LinearLayout layout = (LinearLayout) getView().findViewById(R.id.linear_layout);

		PitScoutSelector selector = new PitScoutSelector(getActivity());
		layout.addView(selector);
	}
}
