package com.example.vanguard.Graphs.GraphImplementations;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.vanguard.CustomUIElements.DividerTextView;
import com.example.vanguard.CustomUIElements.GenericTextView;
import com.example.vanguard.CustomUIElements.MaterialLinearLayout;
import com.example.vanguard.CustomUIElements.MaxHeightScrollView;
import com.example.vanguard.Graphs.Graph;
import com.example.vanguard.Graphs.GraphDetails.GraphDetails;
import com.example.vanguard.Graphs.GraphDetails.ResponseViewerGraphDetails;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.R;
import com.example.vanguard.Responses.Response;

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

	public ResponseViewerGraph(Context context, Question question, int teamNumber) {
		super(context);
		this.context = context;
		this.details = new ResponseViewerGraphDetails(question);
		this.question = question;

		AnswerList<Response> responses = question.getTeamResponses(teamNumber, true);

		Collections.sort(responses, new ResponseSorter());

		this.addView(new DividerTextView(context, question.getQualifiedLabel()));

		this.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		this.setOrientation(VERTICAL);

		MaxHeightScrollView responsesScrollView = new MaxHeightScrollView(context, Math.round(context.getResources().getDimension(R.dimen.graph_height)));
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