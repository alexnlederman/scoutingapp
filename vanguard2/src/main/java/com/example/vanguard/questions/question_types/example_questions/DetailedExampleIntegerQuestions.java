package com.example.vanguard.questions.question_types.example_questions;

/**
 * Created by mbent on 7/25/2017.
 */

public class DetailedExampleIntegerQuestions extends ExampleIntegerQuestion {

	public DetailedExampleIntegerQuestions(boolean generateResponses) {
		super(generateResponses);
	}

	@Override
	public void generateResponses() {
		for (int i = 0; i < this.teamCount; i++) {
			int prevMatch = 0;
			for (int x = 0; x < 7; x++) {
				prevMatch += 7;
				this.setTeamNumber(this.teams.get(i));
				this.setMatchNumber(prevMatch);
				saveResponse();
			}
		}
	}
}
