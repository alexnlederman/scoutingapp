package com.example.vanguard.graphs.graph_implementations;

import android.app.Activity;

import com.example.vanguard.graphs.Graph;
import com.example.vanguard.graphs.graph_details.GraphDetails;
import com.example.vanguard.graphs.GraphManager;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.github.mikephil.charting.charts.CandleStickChart;

import java.util.Map;

/**
 * Created by mbent on 7/25/2017.
 */

public class CandleStickGraph extends CandleStickChart implements Graph {

	GraphDetails details;

	public CandleStickGraph(Activity context, AnswerList<? extends Question> questions, Map<String, Boolean> options) {
		super(context);

		this.details = new GraphDetails(this, GraphTypes.CANDLE_STICK_GRAPH, questions, new String[]{PRACTICE_MATCH_OPTION}, options, true);

		GraphManager.setupCandleStickGraph(this, context);
	}

	@Override
	public GraphDetails getGraphDetails() {
		return this.details;
	}

	@Override
	public String getGraphDescription() {
		return "Top of line shows max value. Bottom of line shows min value. Top of box is 25th percentile and bottom is 75th percentile. Click on data point for team number. ";
	}
}
