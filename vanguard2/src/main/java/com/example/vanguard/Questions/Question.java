package com.example.vanguard.Questions;

import android.view.View;

import com.example.vanguard.DatabaseManager;
import com.example.vanguard.ResponseList;
import com.example.vanguard.Responses.Response;
import com.example.vanguard.Responses.SimpleResponse;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by BertTurtle on 6/1/2017.
 */

public abstract class Question<T extends Object> implements Label, Answer<T> {

	protected int matchNumber;
	protected int teamNumber;
	protected String label;
	protected ResponseList<T> responses;
	protected String id;



	public enum ViewStyle {
		SINGLE_LINE,
		TWO_LINE
	}

	public Question(String label, ResponseList responses, String id) {
		this.matchNumber = 0;
		this.label = label;
		this.id = id;
		this.responses = responses;
	}

	public Question(String label, String id) {
		this(label, new ResponseList(), id);
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

	public ResponseList<T> getResponses() {
		return responses;
	}

	public abstract View getAnswerUI();

	public abstract ViewStyle getViewStyle();

	public abstract DatabaseManager.QuestionTypes getQuestionType();

	public abstract Boolean isEditable();
}
