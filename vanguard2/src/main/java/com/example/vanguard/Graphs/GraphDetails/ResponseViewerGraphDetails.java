package com.example.vanguard.Graphs.GraphDetails;

import com.example.vanguard.Graphs.Graph;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.github.mikephil.charting.charts.Chart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mbent on 7/28/2017.
 */

public class ResponseViewerGraphDetails extends GraphDetails {
	public ResponseViewerGraphDetails(Question question) {
		super(null, Graph.GraphTypes.PLAIN_GRAPH, new AnswerList<>(Collections.singletonList(question)), new String[0], new HashMap<String, Boolean>(), false);
	}

	@Override
	public boolean isShowingMarker() {
		return false;
	}
}
