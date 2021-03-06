package com.example.vanguard.graphs.graph_implementations;

import android.app.Activity;

import com.example.vanguard.graphs.Graph;
import com.example.vanguard.graphs.graph_details.GraphDetails;
import com.example.vanguard.graphs.GraphManager;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.github.mikephil.charting.charts.LineChart;

import java.util.HashMap;

/**
 * Created by mbent on 7/3/2017.
 */

public class LineGraph extends LineChart implements Graph {

	GraphDetails details;


	public LineGraph(Activity context, AnswerList<? extends Question> questions, int teamNumber) {
		super(context);

		this.details = new GraphDetails(this, GraphTypes.LINE_GRAPH, questions, new String[0], new HashMap<String, Boolean>(), false);

		GraphManager.setupLineGraph(this, teamNumber, context);
	}

	@Override
	public GraphDetails getGraphDetails() {
		return this.details;
	}

	@Override
	public String getGraphDescription() {
		return "Displays the qual number vs response value. Click on plot point to delete the entry completely. 25th percentile, median, and 75th percentile is across all teams. Data points before the practice match divider line are for practice matches. Their x value is in the proper order but it is not the actual practice match. ";
	}
}
