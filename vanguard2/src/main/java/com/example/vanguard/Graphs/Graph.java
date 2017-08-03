package com.example.vanguard.Graphs;

import com.example.vanguard.EnumName;
import com.example.vanguard.Graphs.GraphDetails.GraphDetails;

import java.io.Serializable;

/**
 * Created by mbent on 7/3/2017.
 */

public interface Graph extends Serializable {

	enum GraphTypes implements EnumName {
		LINE_GRAPH("Line Graph"),
		BAR_GRAPH("Bar Graph"),
		PIE_GRAPH("Pie Chart"),
		SCATTER_GRAPH("Scatter Graph"),
		CANDLE_STICK_GRAPH("Candle Stick Graph"),
//		RADAR_GRAPH("Radar Chart"),
		PLAIN_GRAPH("Plain Response Viewer");

		private final String text;

		GraphTypes(final String text) {
			this.text = text;
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

	String PRACTICE_MATCH_OPTION = "Include Practice Matches";

	GraphDetails getGraphDetails();
}
