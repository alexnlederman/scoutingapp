package com.example.vanguard.graphs.graph_details;

import com.example.vanguard.graphs.Graph;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.github.mikephil.charting.charts.Chart;

import java.util.Collections;
import java.util.Map;

import static com.example.vanguard.graphs.Graph.PRACTICE_MATCH_OPTION;

/**
 * Created by mbent on 7/27/2017.
 */

public class PieGraphDetails extends GraphDetails {

	public static String ACROSS_TEAM_OPTION = "Graph Across Teams";

	public PieGraphDetails(Chart chart, Question question, Map<String, Boolean> options) {
		super(chart, Graph.GraphTypes.PIE_GRAPH, new AnswerList<Question>(Collections.singletonList(question)), new String[]{PRACTICE_MATCH_OPTION, ACROSS_TEAM_OPTION}, options, true);
	}

	@Override
	public boolean isAllTeamGraph() {
		return this.getOptions().get(ACROSS_TEAM_OPTION);
	}
}
