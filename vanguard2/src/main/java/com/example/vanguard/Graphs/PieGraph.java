package com.example.vanguard.Graphs;

import android.content.Context;

import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.github.mikephil.charting.charts.PieChart;

import java.util.Map;

/**
 * Created by mbent on 7/25/2017.
 */

public class PieGraph extends PieChart implements Graph {

	PieGraphDetails details;

	public PieGraph(Context context, AnswerList<? extends Question> questions, int teamNumber, Map<String, Boolean> options) {
		super(context);
		setup(questions, options);
		GraphManager.setupPieGraph(this, teamNumber);
	}

	private void setup(AnswerList<? extends Question> questions, Map<String, Boolean> options) {
		this.details = new PieGraphDetails(this, questions, options);
	}

	@Override
	public GraphDetails getGraphDetails() {
		return this.details;
	}
}
