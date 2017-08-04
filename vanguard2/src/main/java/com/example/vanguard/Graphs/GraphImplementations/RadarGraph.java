package com.example.vanguard.Graphs.GraphImplementations;

import android.content.Context;

import com.example.vanguard.Graphs.Graph;
import com.example.vanguard.Graphs.GraphDetails.GraphDetails;
import com.example.vanguard.Graphs.GraphManager;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.github.mikephil.charting.charts.RadarChart;

import java.util.Map;

/**
 * Created by mbent on 7/29/2017.
 */

public class RadarGraph extends RadarChart implements Graph {

	GraphDetails details;

	public RadarGraph(Context context, AnswerList<? extends Question> questions, int teamNumber, Map<String, Boolean> options) {
		super(context);

//		this.details = new GraphDetails(this, GraphTypes.RADAR_GRAPH, questions, new String[]{PRACTICE_MATCH_OPTION}, options, false);

		GraphManager.setupRadarGraph(this, teamNumber, context);
	}

	@Override
	public GraphDetails getGraphDetails() {
		return this.details;
	}

	@Override
	public String getGraphDescription() {
		return "This doesn't work";
	}
}
