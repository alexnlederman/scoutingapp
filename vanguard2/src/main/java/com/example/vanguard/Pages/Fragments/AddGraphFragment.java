package com.example.vanguard.Pages.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.vanguard.Graphs.BarGraph;
import com.example.vanguard.Graphs.Graph;
import com.example.vanguard.Graphs.LineGraph;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.QuestionTypes.ExampleIntegerQuestion;
import com.example.vanguard.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddGraphFragment extends Fragment {

	View graphView;
	AnswerList<ExampleIntegerQuestion> questions;

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

		System.out.println("CREATED!");

		questions = new AnswerList<>();
		questions.add(new ExampleIntegerQuestion());

		final Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner);
		if (spinner.getAdapter() == null) {
			spinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, getGraphTypeNames()));
			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					System.out.println("Graph Name: " + parent.getSelectedItem().toString());
					setupGraph(Graph.GraphTypes.valueOfName(parent.getSelectedItem().toString()));
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});
		}
		spinner.setSelection(0);
		System.out.println("Selected: " + spinner.getSelectedItem());

		Button submitButton = (Button) getActivity().findViewById(R.id.submit);
		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = SetGraphQuestionsFragment.newInstance(Graph.GraphTypes.valueOfName(spinner.getSelectedItem().toString()));
				MainActivity.setFragment(fragment, R.id.fragment_holder_layout, getActivity());
			}
		});
	}

	private void setupGraph(Graph.GraphTypes type) {
		LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.graph_layout);
		if (graphView != null) {
			layout.removeView(graphView);
		}
		Graph graph = null;
		switch (type) {
			case LINE_GRAPH:
				graph = new LineGraph(getActivity(), questions, questions.get(0).getDetailedTeam());
				break;
			case BAR_GRAPH:
				graph = new BarGraph(getActivity(), questions);
				break;
		}
		this.graphView = (View) graph;
		if (graph != null) {
			layout.addView(graphView);
		}
	}

	private String[] getGraphTypeNames() {
		Graph.GraphTypes[] types = Graph.GraphTypes.values();
		String[] names = new String[types.length];
		for (int i = 0; i < types.length; i++) {
			names[i] = types[i].getName();
		}
		return names;
	}
}
