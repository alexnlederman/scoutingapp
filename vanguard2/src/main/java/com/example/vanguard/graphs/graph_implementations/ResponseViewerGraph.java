package com.example.vanguard.graphs.graph_implementations;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.custom_ui_elements.DividerTextView;
import com.example.vanguard.custom_ui_elements.GenericTextView;
import com.example.vanguard.custom_ui_elements.MaterialLinearLayout;
import com.example.vanguard.custom_ui_elements.MaxHeightScrollView;
import com.example.vanguard.graphs.Graph;
import com.example.vanguard.graphs.graph_details.GraphDetails;
import com.example.vanguard.graphs.graph_details.ResponseViewerGraphDetails;
import com.example.vanguard.graphs.GraphManager;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.example.vanguard.responses.Response;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by mbent on 7/28/2017.
 */

public class ResponseViewerGraph extends MaterialLinearLayout implements Graph {

	GraphDetails details;
	LinearLayout responsesView;
	Context context;
	Question question;
	boolean practiceMatchDividerAdded = false;
	boolean qualMatchDividerAdded = false;

	public ResponseViewerGraph(Activity context, Question question, int teamNumber) {
		super(context);
		this.context = context;
		this.details = new ResponseViewerGraphDetails(question);
		this.question = question;

		AnswerList<Response> responses = question.getTeamResponses(teamNumber, true);

		Collections.sort(responses, new ResponseSorter());

		this.addView(new DividerTextView(context, question.getQualifiedLabel()));

		this.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		this.setOrientation(VERTICAL);

		MaxHeightScrollView responsesScrollView = new MaxHeightScrollView(context, GraphManager.getGraphHeight(context));
		this.responsesView = new LinearLayout(context);
		this.responsesView.setOrientation(VERTICAL);


		responsesScrollView.addView(this.responsesView);

		this.addView(responsesScrollView);

		if (responses.size() == 0) {
			this.addView(new GenericTextView(this.context, "No Responses Recorded"));
		}

		for (Response response : responses) {
			this.addResponse(response);
		}
	}

	private void addResponse(Response response) {
		if (response.isPracticeMatchResponse() && !this.practiceMatchDividerAdded) {
			this.responsesView.addView(new DividerTextView(this.context, "Practice Match Responses"));
			this.practiceMatchDividerAdded = true;
		}
		if (!response.isPracticeMatchResponse() && this.practiceMatchDividerAdded && !this.qualMatchDividerAdded) {
			this.responsesView.addView(new DividerTextView(this.context, "Qual Match Responses"));
			this.qualMatchDividerAdded = true;
		}
		this.responsesView.addView(new GenericTextView(this.context, getResponseDescription(response)));
	}

	private String getResponseDescription(Response response) {
		if (response.isPracticeMatchResponse()) {
			return "Practice Match " + response.getMatchNumber() + ": " + response.getValue().toString();
		}
		if (this.question.isMatchQuestion()) {
			return "Match " + response.getMatchNumber() + ": " + response.getValue().toString();
		} else {
			return response.getValue().toString();
		}
	}

	@Override
	public GraphDetails getGraphDetails() {
		return this.details;
	}

	@Override
	public String getGraphDescription() {
		return "This views all the responses from a question in plain text for an individual team. ";
	}

	private class ResponseSorter implements Comparator<Response> {

		@Override
		public int compare(Response lhs, Response rhs) {
			if (lhs.isPracticeMatchResponse() != rhs.isPracticeMatchResponse()) {
				if (lhs.isPracticeMatchResponse()) {
					return -1;
				} else {
					return 1;
				}
			}
			return lhs.getMatchNumber() - rhs.getMatchNumber();
		}
	}
}
