package com.example.vanguard.Responses.FormResponses;

import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Responses.Response;
import com.example.vanguard.Responses.SimpleMatchResponse;

/**
 * Created by BertTurtle on 6/10/2017.
 */

public class FormResponse extends AnswerList<Response> {

	SimpleMatchResponse<Integer> matchNumberResponse;
	SimpleMatchResponse<Integer> teamNumberResponse;

	public FormResponse(AnswerList<Response> responses, SimpleMatchResponse<Integer> matchNumberResponse, SimpleMatchResponse<Integer> teamNumberResponse) {
		super(responses);
		this.teamNumberResponse = teamNumberResponse;
		this.matchNumberResponse = matchNumberResponse;
		// TODO make this set all of the questions match number.
	}

	public Response getQuestionResponse(Question question) {
		for (Response response : this) {
			// TODO they may not be .equal() if question string is changed.
			// TODO this line below will not work. Fix it.
			if (response.getValue().equals(question))
				return response;
		}
		return null;
	}

	public int getMatchNumber() {
		return matchNumberResponse.getValue();
	}

	public Integer getTeamNumber() {
		return teamNumberResponse.getValue();
	}
}
