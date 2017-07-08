package com.example.vanguard.Pages.Fragments;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.vanguard.Graphs.Graph;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddGraphFragment extends Fragment {


	public AddGraphFragment() {
		// Required empty public constructor
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_add_graph, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		final Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner);
		spinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, Graph.GraphTypes.values()));

		Button submitButton = (Button) getActivity().findViewById(R.id.submit);
		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("Class: " + spinner.getSelectedItem().getClass());
				Fragment fragment = SetGraphQuestionsFragment.newInstance((Graph.GraphTypes) spinner.getSelectedItem());
				MainActivity.setFragment(fragment, R.id.fragment_holder_layout, getActivity());
			}
		});
	}
}
