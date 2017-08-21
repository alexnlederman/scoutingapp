package com.example.vanguard.questions.question_types.example_questions;

import com.example.vanguard.responses.Response;

/**
 * Created by mbent on 7/11/2017.
 */

public class ExampleIntegerQuestion extends ExampleQuestion<Integer> {

	public ExampleIntegerQuestion(boolean generateResponses) {
		super(generateResponses);
	}

	public void generateResponses() {
		int prevMatch = 0;
		for (int i = 0; i < 7; i++) {
			int newMatch = prevMatch + 7;
			this.setTeamNumber(getDetailedTeam());
			this.setMatchNumber(newMatch);
			saveResponse();
			prevMatch = newMatch;
		}
		for (int i = 1; i < teamCount; i++) {
			this.setTeamNumber(this.teams.get(i));
			this.setMatchNumber(this.random.nextInt(50));
			saveResponse();
		}
	}

	@Override
	public Integer getValue() {
		return (5 + this.random.nextInt(10));
	}

	@Override
	public float convertResponseToNumber(Response<Integer> response) {
		return response.getValue();
	}
}
