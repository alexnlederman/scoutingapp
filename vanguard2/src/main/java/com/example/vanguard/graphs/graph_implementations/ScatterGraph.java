package com.example.vanguard.graphs.graph_implementations;

import android.app.Activity;

import com.example.vanguard.graphs.Graph;
import com.example.vanguard.graphs.graph_details.GraphDetails;
import com.example.vanguard.graphs.GraphManager;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.github.mikephil.charting.charts.ScatterChart;

import java.util.Map;

/**
 * Created by mbent on 7/25/2017.
 */

public class ScatterGraph extends ScatterChart implements Graph {

	GraphDetails details;

	public ScatterGraph(Activity context, AnswerList<? extends Question> questions, Map<String, Boolean> options) {
		super(context);

		this.details = new GraphDetails(this, GraphTypes.SCATTER_GRAPH, questions, new String[]{PRACTICE_MATCH_OPTION}, options, true);

		GraphManager.setupScatterGraph(this, context);
	}

	@Override
	public GraphDetails getGraphDetails() {
		return this.details;
	}

	@Override
	public String getGraphDescription() {
		return "This displays X vs Y of how teams for a question. The X value is the median value from one question and the Y value is the median value from another question. ";
	}
}
