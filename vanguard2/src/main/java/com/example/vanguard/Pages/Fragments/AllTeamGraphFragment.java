package com.example.vanguard.Pages.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.Graphs.BarGraph;
import com.example.vanguard.Graphs.GraphViewer;
import com.example.vanguard.Graphs.LineGraph;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.R;

/**
 * Created by mbent on 7/5/2017.
 */

public class AllTeamGraphFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.scroll_linearlayout_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		LinearLayout layout = (LinearLayout) getView().findViewById(R.id.linear_layout);

//		AnswerList<Question> questions = new AnswerList<>();
//		questions.add(MainActivity.databaseManager.getMatchQuestions().get(0));
//		questions.add(MainActivity.databaseManager.getMatchQuestions().get(1));
//		LineGraph graph = new LineGraph(getActivity(), questions, 5940);
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//		graph.setLayoutParams(params);

		GraphViewer viewer = new GraphViewer(getActivity());
		layout.addView(viewer);
	}
}
