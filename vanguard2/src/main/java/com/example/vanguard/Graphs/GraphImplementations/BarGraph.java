package com.example.vanguard.Graphs.GraphImplementations;

import android.app.Activity;
import android.content.Context;

import com.example.vanguard.Graphs.Graph;
import com.example.vanguard.Graphs.GraphDetails.GraphDetails;
import com.example.vanguard.Graphs.GraphManager;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.github.mikephil.charting.charts.BarChart;

import java.util.Map;

/**
 * Created by mbent on 7/7/2017.
 */

public class BarGraph extends BarChart implements Graph {

	GraphDetails details;

	public BarGraph(Activity context, AnswerList<? extends Question> questions, Map<String, Boolean> options) {
		super(context);

		this.details = new GraphDetails(this, GraphTypes.BAR_GRAPH, questions, new String[]{PRACTICE_MATCH_OPTION}, options, true);

		GraphManager.setupBarGraph(this, context);
	}

	@Override
	public GraphDetails getGraphDetails() {
		return this.details;
	}

	@Override
	public String getGraphDescription() {
		return "The median value for the question. Click on bar for team number. ";
	}
}
