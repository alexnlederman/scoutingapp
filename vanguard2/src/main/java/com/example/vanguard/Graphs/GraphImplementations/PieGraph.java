package com.example.vanguard.Graphs.GraphImplementations;

import android.content.Context;

import com.example.vanguard.Graphs.Graph;
import com.example.vanguard.Graphs.GraphDetails.GraphDetails;
import com.example.vanguard.Graphs.GraphManager;
import com.example.vanguard.Graphs.GraphDetails.PieGraphDetails;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.github.mikephil.charting.charts.PieChart;

import java.util.Map;

/**
 * Created by mbent on 7/25/2017.
 */

public class PieGraph extends PieChart implements Graph {

	PieGraphDetails details;

	public PieGraph(Context context, Question question, int teamNumber, Map<String, Boolean> options) {
		super(context);
		setup(question, options);
		GraphManager.setupPieGraph(this, teamNumber, context);
	}

	private void setup(Question question, Map<String, Boolean> options) {
		this.details = new PieGraphDetails(this, question, options);
	}

	@Override
	public GraphDetails getGraphDetails() {
		return this.details;
	}

	@Override
	public String getGraphDescription() {
		return "Displays the percent of responses that are of different values. Displays it for either individual teams or across all teams depending on the " + PieGraphDetails.ACROSS_TEAM_OPTION + " setting. This can also show number values. If this is for a number question and it is being graphed across teams then it uses the median for each team. ";
	}
}
