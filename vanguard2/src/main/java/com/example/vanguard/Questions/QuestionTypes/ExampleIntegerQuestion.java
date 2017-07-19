package com.example.vanguard.Questions.QuestionTypes;

import android.view.View;

import com.example.vanguard.Questions.Question;
import com.example.vanguard.Responses.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mbent on 7/11/2017.
 */

public class ExampleIntegerQuestion extends Question<Integer> {

	Random random;
	List<Integer> teams;
	int teamCount = 20;

	public ExampleIntegerQuestion() {
		super("Example Question", "", true, ViewStyle.SINGLE_LINE, QuestionType.INTEGER, false, null);
		random = new Random();
		generateResponses();
	}

	private List<Integer> generateTeams() {
		List<Integer> teams = new ArrayList<>();
		for (int i = 0; i < teamCount; i++) {
			int prevValue = (i > 0) ? teams.get(i - 1) : 0;
			teams.add(prevValue + this.random.nextInt(100) + 1);
		}
		return teams;
	}

	private void generateResponses() {
		this.teams = generateTeams();
		int prevMatch = 0;
		for (int i = 0; i < 7; i++) {
			int newMatch = prevMatch + this.random.nextInt(3) + 5;
			System.out.println("New Match: " + newMatch);
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
	public void setValue(Integer newValue) {
	}

	@Override
	public View getAnswerUI() {
		return null;
	}

	@Override
	public float convertResponseToNumber(Response<Integer> response) {
		System.out.println(response.getValue());
		return response.getValue();
	}

	public int getDetailedTeam() {
		return this.teams.get(0);
	}

	@Override
	protected List<Integer> getEventTeams() {
		return this.teams;
	}
}
