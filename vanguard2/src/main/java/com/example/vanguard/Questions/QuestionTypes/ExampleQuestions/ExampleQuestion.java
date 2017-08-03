package com.example.vanguard.Questions.QuestionTypes.ExampleQuestions;

import android.view.View;

import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Properties.QuestionProperty;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Responses.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by mbent on 7/25/2017.
 */

public abstract class ExampleQuestion<T extends Object> extends Question<T> {
	Random random;
	List<Integer> teams;
	int teamCount = 20;

	public ExampleQuestion() {
		super("Example Question", "", true, ViewStyle.SINGLE_LINE, QuestionType.INTEGER, false, null, new TreeMap<String, Object>());
		this.random = new Random();
		this.teams = this.generateTeams();
		this.generateResponses();
	}

	private List<Integer> generateTeams() {
		List<Integer> teams = new ArrayList<>();
		teams.add(50);
		for (int i = 1; i < teamCount; i++) {
			int prevValue = teams.get(i - 1);
			teams.add(prevValue + this.random.nextInt(100) + 1);
		}
		return teams;
	}

	protected abstract void generateResponses();

	@Override
	public void setValue(T newValue) {
	}

	@Override
	public View getAnswerUI() {
		return null;
	}

	public int getDetailedTeam() {
		return this.teams.get(0);
	}

	@Override
	public List<Integer> getEventTeams() {
		return this.teams;
	}
}
