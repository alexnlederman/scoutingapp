package com.example.vanguard.Pages.Fragments;


import android.app.Fragment;
import android.os.AsyncTask;
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
import android.widget.TextView;

import com.example.vanguard.CustomUIElements.SwitchOption;
import com.example.vanguard.Graphs.Graph;
import com.example.vanguard.Graphs.GraphImplementations.BarGraph;
import com.example.vanguard.Graphs.GraphImplementations.CandleStickGraph;
import com.example.vanguard.Graphs.GraphImplementations.LineGraph;
import com.example.vanguard.Graphs.GraphImplementations.PieGraph;
import com.example.vanguard.Graphs.GraphImplementations.ResponseViewerGraph;
import com.example.vanguard.Graphs.GraphImplementations.ScatterGraph;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class AddGraphFragment extends Fragment {

	View graphView;
	AnswerList<ExampleIntegerQuestion> twoBasicIntegerQuestions;
	AnswerList<ExampleIntegerQuestion> oneBasicIntegerQuestions;
	AnswerList<DetailedExampleIntegerQuestions> oneDetailedIntegerQuestion;
	AnswerList<ExampleStringQuestion> oneStringQuestion;
	AnswerList<ExampleIntegerQuestion> fourIntegerQuestion;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_add_graph, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		final ExampleIntegerQuestion exampleInteger = new ExampleIntegerQuestion(true);
		final ExampleStringQuestion exampleString = new ExampleStringQuestion(false);
		final DetailedExampleIntegerQuestions detailedInteger = new DetailedExampleIntegerQuestions(false);
		final ExampleIntegerQuestion exampleInteger2 = new ExampleIntegerQuestion(false);
		final ExampleIntegerQuestion exampleInteger3 = new ExampleIntegerQuestion(false);
		final ExampleIntegerQuestion exampleInteger4 = new ExampleIntegerQuestion(false);

		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				exampleString.generateResponses();
				detailedInteger.generateResponses();
				exampleInteger3.generateResponses();
				exampleInteger4.generateResponses();
				exampleInteger2.generateResponses();
				System.out.println("RESPONSES Size: " + exampleInteger.getResponses().size());
				return null;
			}
		}.execute();




		this.twoBasicIntegerQuestions = new AnswerList<>();
		this.oneBasicIntegerQuestions = new AnswerList<>();
		this.oneStringQuestion = new AnswerList<>();
		this.oneDetailedIntegerQuestion = new AnswerList<>();
		this.fourIntegerQuestion = new AnswerList<>();


		this.oneStringQuestion.add(exampleString);

		this.oneDetailedIntegerQuestion.add(detailedInteger);

		this.oneBasicIntegerQuestions.add(exampleInteger);
		this.twoBasicIntegerQuestions.add(exampleInteger);
		this.twoBasicIntegerQuestions.add(exampleInteger2);

		this.fourIntegerQuestion.add(exampleInteger);
		this.fourIntegerQuestion.add(exampleInteger2);
		this.fourIntegerQuestion.add(exampleInteger3);
		this.fourIntegerQuestion.add(exampleInteger4);


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
				graph = new LineGraph(getActivity(), this.oneBasicIntegerQuestions, this.oneBasicIntegerQuestions.get(0).getDetailedTeam());
				break;
			case BAR_GRAPH:
				graph = new BarGraph(getActivity(), this.oneBasicIntegerQuestions, new HashMap<String, Boolean>());
				break;
			case SCATTER_GRAPH:
				graph = new ScatterGraph(getActivity(), this.twoBasicIntegerQuestions, new HashMap<String, Boolean>());
				break;
			case CANDLE_STICK_GRAPH:
				graph = new CandleStickGraph(getActivity(), this.oneDetailedIntegerQuestion, new HashMap<String, Boolean>());
				break;
			case PIE_GRAPH:
				graph = new PieGraph(getActivity(), this.oneStringQuestion.get(0), this.oneStringQuestion.get(0).getDetailedTeam(), new HashMap<String, Boolean>());
				break;
			case PLAIN_GRAPH:
				graph = new ResponseViewerGraph(getActivity(), this.oneStringQuestion.get(0), this.oneStringQuestion.get(0).getDetailedTeam());
				break;
//			case RADAR_GRAPH:
//				graph = new RadarGraph(getActivity(), this.fourIntegerQuestion, this.twoBasicIntegerQuestions.get(0).getDetailedTeam(), new HashMap<String, Boolean>());
//				break;
		}
		return graph;
	}

	private void setupGraph(Graph.GraphTypes type) {
		LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.graph_details);
//		if (graphView != null) {
		layout.removeAllViews();
//			for (int i = 0; i < layout.getChildCount(); ) {
//				if (layout.getChildAt(i) instanceof SwitchOption) {
//					layout.removeViewAt(i);
//				} else {
//					i++;
//				}
//			}
//		}
		Graph graph = getGraphByType(type);
		this.graphView = (View) graph;
		if (graph != null) {
			layout.addView(this.graphView);
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
						finalGraph.getGraphDetails().setOptions(optionsMap);
					}
				});
			}

			TextView description = new TextView(this.getActivity());
			description.setText(graph.getGraphDescription());
			layout.addView(description);
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
		Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner);
		spinner.setSelection(0);
	}
}
