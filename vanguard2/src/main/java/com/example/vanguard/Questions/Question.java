package com.example.vanguard.Questions;

import android.view.View;

import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Responses.Response;
import com.example.vanguard.Responses.SimpleResponse;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BertTurtle on 6/1/2017.
 */

public abstract class Question<T extends Object> implements Label, Answer<T> {

	private final boolean isMatchQuestion;
	protected int matchNumber;
	protected int teamNumber;
	protected String label;
	protected AnswerList<Response> responses;
	protected String id;

	/**
	 * Possible types of questions.
	 * TODO add more question types.
	 */
	public enum QuestionTypes {
		INTEGER("INTEGER"),
		STRING("STRING"),
		MATCH_NUMBER("MATCH_NUMBER"),
		TEAM_NUMBER("TEAM_NUMBER");

		private final String text;

		QuestionTypes(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return this.text;
		}
	}

	public enum ViewStyle {
		SINGLE_LINE,
		TWO_LINE
	}

	public Question(String label, AnswerList<Response> responses, String id, boolean isMatchQuestion) {
		this.matchNumber = 0;
		this.label = label;
		this.id = id;
		this.responses = responses;
		this.isMatchQuestion = isMatchQuestion;
	}

	public Question(String label, String id, boolean isMatchQuestion) {
		this(label, new AnswerList<Response>(), id, isMatchQuestion);
	}

	public void setMatchNumber(int matchNumber) {
		this.matchNumber = matchNumber;
	}

	public void setTeamNumber(int teamNumber) { this.teamNumber = teamNumber; }

	public String getID() { return this.id; }

	@Override
	public String getLabel() {
		return this.label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}


	public void saveResponse() {
		SimpleResponse<T> response = new SimpleResponse<T>(this.getValue(), this.matchNumber, this.teamNumber);
		responses.add(response);
	}

	public AnswerList<Response<T>> getTeamResponses(int teamNumber) {
//		return MainActivity.databaseManager.getTeamMatchQuestionResponses(this.getID(), teamNumber);
		AnswerList<Response> responses = getResponses();
		AnswerList<Response<T>> teamResponses = new AnswerList<>();
		for (Response<T> response : responses) {
			if (response.getTeamNumber() == teamNumber) {
				teamResponses.add(response);
			}
		}
		return teamResponses;
		// TODO sort the responses.
	}

	public AnswerList<Response> getResponses() {
		return responses;
	}

	public abstract View getAnswerUI();

	public List<Entry> getTeamEntryResponseValues(int teamNumber) {
		List<Entry> entries = new ArrayList<>();
		AnswerList<Response<T>> responses = this.getTeamResponses(teamNumber);
		for (Response response : responses) {
			entries.add(new Entry(response.getMatchNumber(), convertResponseToNumber(response)));
		}
		return entries;
	}

	public List<Entry> getAllTeamEntryResponseValues() {
		List<Entry> entries = new ArrayList<>();
		List<Integer> teams = MainActivity.databaseManager.getEventTeams((String) MainActivity.databaseManager.getCurrentEventInfo().get(0));
		System.out.println("Team Size: " + teams.size());
		int i = 1;
		for (int teamNumber : teams) {
			AnswerList<Response<T>> teamResponses = this.getTeamResponses(teamNumber);
			System.out.println("Team Number: " + teamNumber);
			System.out.println("Should be zero for most but one or two for some: " + teamResponses.size());
			entries.add(new Entry(i, convertResponsesToNumber(teamResponses), teamNumber));
			i++;
		}
		System.out.println("X0: " + entries.get(0).getX());
		System.out.println("X1: " + entries.get(1).getX());
		System.out.println("X2: " + entries.get(2).getX());
		return entries;
	}

	public abstract float convertResponseToNumber(Response<T> response);

	public float convertResponsesToNumber(AnswerList<Response<T>> responses) {
		if (responses.size() == 0)
			return 0;
		float total = 0;
		for (Response response : responses) {
			total += this.convertResponseToNumber(response);
		}
		return total / responses.size();
	}

	public abstract ViewStyle getViewStyle();
	
	public abstract QuestionTypes getQuestionType();

	public abstract Boolean isEditable();
}
