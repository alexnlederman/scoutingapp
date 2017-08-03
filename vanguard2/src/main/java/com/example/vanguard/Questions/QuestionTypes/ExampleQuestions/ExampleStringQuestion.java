package com.example.vanguard.Questions.QuestionTypes.ExampleQuestions;

import com.example.vanguard.Responses.Response;

import java.util.Random;

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

	@Override
	public int getMatchNumber() {
		return Math.round((float) Math.random() * 50f) + 1;
	}

	@Override
	public boolean isPracticeMatchQuestion() {
		return (Math.random() < 0.2);
	}
}
