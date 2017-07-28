package com.example.vanguard.Pages.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.vanguard.CustomUIElements.SwitchOption;
import com.example.vanguard.Graphs.BarGraph;
import com.example.vanguard.Graphs.CandleStickGraph;
import com.example.vanguard.Graphs.Graph;
import com.example.vanguard.Graphs.LineGraph;
import com.example.vanguard.Graphs.PieGraph;
import com.example.vanguard.Graphs.ScatterGraph;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.QuestionTypes.ExampleQuestions.DetailedExampleIntegerQuestions;
import com.example.vanguard.Questions.QuestionTypes.ExampleQuestions.ExampleIntegerQuestion;
import com.example.vanguard.Questions.QuestionTypes.ExampleQuestions.ExampleStringQuestion;
import com.example.vanguard.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddGraphFragment extends Fragment {

	View graphView;
	AnswerList<ExampleIntegerQuestion> detailedIntegerQuestions;
	AnswerList<ExampleIntegerQuestion> basicIntegerQuestions;
	AnswerList<DetailedExampleIntegerQuestions> oneDetailedIntegerQuestion;
	AnswerList<ExampleStringQuestion> oneStringQuestion;
	boolean firstRun = true;


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

		this.oneStringQuestion = new AnswerList<>();
		this.oneStringQuestion.add(new ExampleStringQuestion());

		this.oneDetailedIntegerQuestion = new AnswerList<>();
		this.oneDetailedIntegerQuestion.add(new DetailedExampleIntegerQuestions());

		this.detailedIntegerQuestions = new AnswerList<>();
		this.basicIntegerQuestions = new AnswerList<>();
		ExampleIntegerQuestion exampleIntegerQuestion = new ExampleIntegerQuestion();
		this.basicIntegerQuestions.add(exampleIntegerQuestion);
		this.detailedIntegerQuestions.add(exampleIntegerQuestion);
		this.detailedIntegerQuestions.add(new ExampleIntegerQuestion());

		final Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner);
		if (spinner.getAdapter() == null) {
			ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, getGraphTypeNames());

			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					setupGraph(Graph.GraphTypes.valueOfName(parent.getSelectedItem().toString()));
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});
		}

		Button submitButton = (Button) getActivity().findViewById(R.id.submit);
		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = SetGraphQuestionsFragment.newInstance((Graph) graphView);
				MainActivity.setFragment(fragment, R.id.fragment_holder_layout, getActivity());
			}
		});
	}

	public Graph getGraphByType(Graph.GraphTypes type) {
		Graph graph = null;
		switch (type) {
			case LINE_GRAPH:
				graph = new LineGraph(getActivity(), this.basicIntegerQuestions, this.basicIntegerQuestions.get(0).getDetailedTeam());
				break;
			case BAR_GRAPH:
				graph = new BarGraph(getActivity(), this.basicIntegerQuestions, new HashMap<String, Boolean>());
				break;
			case SCATTER_GRAPH:
				graph = new ScatterGraph(getActivity(), this.detailedIntegerQuestions, new HashMap<String, Boolean>());
				break;
			case CANDLE_STICK_GRAPH:
				graph = new CandleStickGraph(getActivity(), this.oneDetailedIntegerQuestion, new HashMap<String, Boolean>());
				break;
			case PIE_GRAPH:
				graph = new PieGraph(getActivity(), this.oneStringQuestion, this.oneStringQuestion.get(0).getDetailedTeam(), new HashMap<String, Boolean>());
				break;
		}
		return graph;
	}

	private void setupGraph(Graph.GraphTypes type) {
		LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.graph_layout);
		if (graphView != null) {
			layout.removeView(graphView);
			for (int i = 0; i < layout.getChildCount();) {
				if (layout.getChildAt(i) instanceof SwitchOption) {
					layout.removeViewAt(i);
				}
				else {
					i++;
				}
			}
		}
		Graph graph = getGraphByType(type);
		this.graphView = (View) graph;
		if (graph != null) {
			layout.addView(graphView);
			String[] options = graph.getGraphDetails().getOptionTitles();
			final List<SwitchOption> toggles = new ArrayList<>();
			for (String option : options) {
				SwitchOption toggle = new SwitchOption(getActivity(), option);
				toggles.add(toggle);
				layout.addView(toggle);
				final Graph finalGraph = graph;
				toggle.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						Map<String, Boolean> optionsMap = new HashMap<>();
						for (SwitchOption toggle : toggles) {
							optionsMap.put(toggle.getDescription(), toggle.isChecked());
						}
						System.out.println("SAVE: " + optionsMap);
						finalGraph.getGraphDetails().setOptions(optionsMap);
					}
				});
			}
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

	@Override
	public void onResume() {
		super.onResume();
		final Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner);
//		Graph.GraphTypes[] types = Graph.GraphTypes.values();
//		Random random = new Random();
		if (!this.firstRun) {
//			int rng = random.nextInt(types.length - 1) + 1;
			spinner.setSelection(1);
		}
		this.firstRun = false;
	}
}
