package com.example.vanguard.Questions.QuestionTypes;

import android.content.Context;
import android.view.View;

import com.example.vanguard.CustomUIElements.AnswerUITextView;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Responses.Response;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by mbent on 7/9/2017.
 */

public class PitTeamNumberQuestion extends Question<Integer> {

	AnswerUITextView answerUI;
	int teamNumber;

	public PitTeamNumberQuestion(Context context, AnswerList<Response> responses, String id) {
		super("Team Number", responses, id, false, ViewStyle.SINGLE_LINE, QuestionType.PIT_TEAM_NUMBER, false, 0, new TreeMap<String, Object>());
		setupUI(context);
	}

	private void setupUI(Context context) {
		this.answerUI = new AnswerUITextView(context);
	}

	@Override
	public Integer getValue() {
		return this.teamNumber;
	}

	@Override
	public void setValue(Integer newValue) {

	}

	@Override
	public View getAnswerUI() {
		return this.answerUI;
	}

	public void setTeam(int teamNumber) {
		this.answerUI.setText(String.valueOf(teamNumber));
		this.teamNumber = teamNumber;
	}

	@Override
	public float convertResponseToNumber(Response<Integer> response) {
		return response.getValue();
	}
}
