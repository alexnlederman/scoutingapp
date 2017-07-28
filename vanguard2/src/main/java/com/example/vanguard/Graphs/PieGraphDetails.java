package com.example.vanguard.Graphs;

import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.github.mikephil.charting.charts.Chart;

import java.util.Map;

import static com.example.vanguard.Graphs.Graph.PRACTICE_MATCH_OPTION;

/**
 * Created by mbent on 7/27/2017.
 */

public class PieGraphDetails extends GraphDetails {

	private static String ACROSS_TEAM_OPTION = "Graph Across Teams";

	public PieGraphDetails(Chart chart, AnswerList<? extends Question> questions, Map<String, Boolean> options) {
		super(chart, Graph.GraphTypes.PIE_GRAPH, questions, new String[]{PRACTICE_MATCH_OPTION, ACROSS_TEAM_OPTION}, options, true);
	}

	@Override
	public boolean isAllTeamGraph() {
		return this.getOptions().get(ACROSS_TEAM_OPTION);
	}
}
