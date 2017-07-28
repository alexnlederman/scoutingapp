package com.example.vanguard.Questions.QuestionTypes.ExampleQuestions;

/**
 * Created by mbent on 7/25/2017.
 */

public class DetailedExampleIntegerQuestions extends ExampleIntegerQuestion {

	public DetailedExampleIntegerQuestions() {
		super();
	}

	@Override
	protected void generateResponses() {
		for (int i = 0; i < this.teamCount; i++) {
			int prevMatch = 0;
			for (int x = 0; x < 7; x++) {
				prevMatch += 7;
				this.setTeamNumber(this.teams.get(i));
				this.setMatchNumber(prevMatch);
				System.out.println("SAVING");
				saveResponse();
			}
		}
	}
}
