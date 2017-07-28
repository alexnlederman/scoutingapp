package com.example.vanguard.Questions.QuestionTypes.ExampleQuestions;

import com.example.vanguard.Responses.Response;

/**
 * Created by mbent on 7/25/2017.
 */

public class ExampleStringQuestion extends ExampleQuestion<String> {

	public ExampleStringQuestion() {
		super();
	}

	@Override
	public String getValue() {
		String[] values = {"Example 1", "Example 2", "Example 3"};
		return values[this.random.nextInt(values.length)];
	}

	@Override
	protected void generateResponses() {
		for (int i = 0; i < 8; i++) {
			this.setTeamNumber(this.getDetailedTeam());
			this.saveResponse();
		}
	}

	@Override
	public float convertResponseToNumber(Response<String> response) {
		return 1;
	}
}
