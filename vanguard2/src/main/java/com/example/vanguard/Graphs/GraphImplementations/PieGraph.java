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
}
