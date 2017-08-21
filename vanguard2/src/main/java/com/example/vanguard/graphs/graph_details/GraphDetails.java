package com.example.vanguard.graphs.graph_details;

import com.example.vanguard.graphs.Graph;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.github.mikephil.charting.charts.Chart;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mbent on 7/27/2017.
 */

public class GraphDetails {

	private Graph.GraphTypes graphType;
	private AnswerList<? extends Question> questions;
	private Chart chart;
	private String[] optionTitles;
	private Map<String, Boolean> options;
	private boolean isAllTeamGraph;

	public GraphDetails(Chart chart, Graph.GraphTypes graphType, AnswerList<? extends Question> questions, String[] optionTitles, Map<String, Boolean> options, boolean isAllTeamGraph) {
		this.graphType = graphType;
		this.questions = questions;
		this.chart = chart;
		this.optionTitles = optionTitles;
		if (options.size() == 0) {
			this.options = new HashMap<>();
			for (String title : optionTitles) {
				this.options.put(title, false);
			}
		} else {
			this.options = options;
		}
		this.isAllTeamGraph = isAllTeamGraph;
	}

	public Graph.GraphTypes getGraphType() {
		return this.graphType;
	}

	public AnswerList<? extends Question> getGraphQuestions() {
		return this.questions;
	}

	public boolean isShowingMarker() {
		return this.chart.getMarker() != null && this.chart.isDrawMarkersEnabled() && this.chart.valuesToHighlight();
	}

	public String[] getOptionTitles() {
		return this.optionTitles;
	}

	public Map<String, Boolean> getOptions() {
		return this.options;
	}

	public void setOptions(Map<String, Boolean> options) {
		this.options = options;
	}

	public boolean isAllTeamGraph() {
		return this.isAllTeamGraph;
	}

	public void setQuestions(AnswerList<? extends Question> questions) {
		this.questions = questions;
	}
}
