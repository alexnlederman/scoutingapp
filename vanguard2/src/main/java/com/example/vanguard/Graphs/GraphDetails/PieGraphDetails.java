package com.example.vanguard.Graphs.GraphDetails;

import com.example.vanguard.Graphs.Graph;
import com.example.vanguard.Graphs.GraphDetails.GraphDetails;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.github.mikephil.charting.charts.Chart;

import java.util.Collections;
import java.util.Map;

import static com.example.vanguard.Graphs.Graph.PRACTICE_MATCH_OPTION;

/**
 * Created by mbent on 7/27/2017.
 */

public class PieGraphDetails extends GraphDetails {

	private static String ACROSS_TEAM_OPTION = "Graph Across Teams";

	public PieGraphDetails(Chart chart, Question question, Map<String, Boolean> options) {
		super(chart, Graph.GraphTypes.PIE_GRAPH, new AnswerList<Question>(Collections.singletonList(question)), new String[]{PRACTICE_MATCH_OPTION, ACROSS_TEAM_OPTION}, options, true);
	}

	@Override
	public boolean isAllTeamGraph() {
		return this.getOptions().get(ACROSS_TEAM_OPTION);
	}
}
