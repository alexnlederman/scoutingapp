package com.example.vanguard.Pages.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vanguard.CustomUIElements.ErrorTextView;
import com.example.vanguard.Graphs.Graph;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionSelectorViewer;
import com.example.vanguard.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbent on 7/6/2017.
 */

public class SetGraphQuestionsFragment extends Fragment {

	public static SetGraphQuestionsFragment newInstance(Graph graph) {
		Bundle args = new Bundle();
		args.putSerializable("graph", graph);
		SetGraphQuestionsFragment fragment = new SetGraphQuestionsFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.scroll_linearlayout_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		final Graph graph = (Graph) getArguments().getSerializable("graph");
		Graph.GraphTypes graphType = graph.getGraphDetails().getGraphType();

		LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.linear_layout);

		AnswerList<Question> questions = MainActivity.databaseManager.getMatchQuestions();


		List<Class> compatibleClasses = new ArrayList<>();
		List<Class> incompatibleClasses = new ArrayList<>();


		int min = 1;
		int max = -1;
		boolean includesPitQuestions = false;
		switch (graphType) {
			case LINE_GRAPH:
				compatibleClasses.add(Number.class);
				break;
			case BAR_GRAPH:
				compatibleClasses.add(Object.class);
				incompatibleClasses.add(String.class);
				includesPitQuestions = true;
				break;
			case PIE_GRAPH:
				max = 1;
				compatibleClasses.add(Object.class);
				includesPitQuestions = graph.getGraphDetails().isAllTeamGraph();
				break;
			case SCATTER_GRAPH:
				min = 2;
				max = 2;
				compatibleClasses.add(Number.class);
				includesPitQuestions = true;
				break;
			case CANDLE_STICK_GRAPH:
				max = 1;
				compatibleClasses.add(Number.class);
				break;
//			case RADAR_GRAPH:
//				min = 3;
//				compatibleClasses.add(Object.class);
//				incompatibleClasses.add(String.class);
//				includesPitQuestions = true;
//				break;
			case PLAIN_GRAPH:
				max = 1;
				compatibleClasses.add(Object.class);
				includesPitQuestions = true;
				break;
		}

		if (includesPitQuestions) {
			questions.addAll(MainActivity.databaseManager.getPitQuestions());
		}
		System.out.println("Question Size0: " + questions.size());

		for (Class type : compatibleClasses) {
			questions = questions.getAllOfType(type);
			System.out.println("Question Size1: " + questions.size());

		}
		for (Class type : incompatibleClasses) {
			questions = questions.getAllNotOfType(type);
			System.out.println("Question Size2: " + questions.size());

		}
		System.out.println("Question Size: " + questions.size());
		System.out.println("Min: " + min);
		if (questions.size() >= min) {
			final QuestionSelectorViewer selectorViewer = new QuestionSelectorViewer(getActivity(), questions);
			layout.addView(selectorViewer);

			Button submitButton = new Button(getActivity());
			submitButton.setText("Submit");
			final int finalMin = min;
			final int finalMax = max;
			submitButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int selectedAmount = selectorViewer.getAmountSelected();
					if ((selectedAmount <= finalMax || finalMax == -1) && selectedAmount >= finalMin) {
						graph.getGraphDetails().setQuestions(selectorViewer.getSelectedQuestions());
						MainActivity.databaseManager.addGraph(graph);
						getActivity().finish();
					} else {
						String text = "";
						if (selectedAmount > finalMax && finalMax != -1) {
							text = "Too many questions selected";
						} else if (selectedAmount < finalMin) {
							text = "Too few questions selected";
						}
						Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
					}
				}
			});
			layout.addView(submitButton);
		} else {
			layout.addView(new ErrorTextView(this.getActivity(), R.string.compatible_questions_error));
		}
	}
}
