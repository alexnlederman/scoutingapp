package com.example.vanguard.pages.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.custom_ui_elements.ErrorTextView;
import com.example.vanguard.MatchScoutSelector;
import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchScoutSelectorFragment extends Fragment {

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
			MatchScoutSelector selector = new MatchScoutSelector((AppCompatActivity) getActivity());
			layout.addView(selector);

		} else
			layout.addView(new ErrorTextView(getActivity(), R.string.set_event_error));
	}
}
