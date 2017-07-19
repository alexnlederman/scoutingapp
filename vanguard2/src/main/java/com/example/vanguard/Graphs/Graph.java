package com.example.vanguard.Graphs;

import com.example.vanguard.EnumName;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;

/**
 * Created by mbent on 7/3/2017.
 */

public interface Graph {

	enum GraphTypes implements EnumName {
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

		public static GraphTypes valueOfName(String graphName) {
			for (GraphTypes type : GraphTypes.values())
				if (type.getName().equals(graphName))
					return type;
			return null;
		}
	}

	GraphTypes getGraphType();

	boolean isAllTeamGraph();

	AnswerList<? extends Question> getGraphQuestions();

	boolean isShowingMarker();
}
