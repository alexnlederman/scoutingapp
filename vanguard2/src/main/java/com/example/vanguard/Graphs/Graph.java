package com.example.vanguard.Graphs;

import com.example.vanguard.Pages.Fragments.SetGraphQuestionsFragment;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Responses.Response;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbent on 7/3/2017.
 */

public interface Graph {

	enum GraphTypes {
		LINE_GRAPH("Line Graph", false),
		BAR_GRAPH("Bar Graph", true),
		GROUPED_BAR_GRAPH("Grouped Bar Graph", true),
		PIE_GRAPH("Pie Chart", false),
		SCATTER_GRAPH("Scatter Graph", true),
		CANDLE_STICK_GRAPH("Candle Stick Graph", true),
		RADAR_GRAPH("Radar Chart", false);

		private final String text;
		private final boolean isAllTeamGraph;

		GraphTypes(final String text, final boolean isAllTeamGraph) {
			this.text = text;
			this.isAllTeamGraph = isAllTeamGraph;
		}

		public boolean isAllTeamGraph() {
			return isAllTeamGraph;
		}

		public String getName() {
			return this.text;
		}
	}

	GraphTypes getGraphType();

	boolean isAllTeamGraph();

	AnswerList<Question> getGraphQuestions();
}
