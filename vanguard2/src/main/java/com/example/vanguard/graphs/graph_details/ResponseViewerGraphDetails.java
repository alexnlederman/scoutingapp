package com.example.vanguard.graphs.graph_details;

import com.example.vanguard.graphs.Graph;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;

import java.util.Collections;
import java.util.HashMap;

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
