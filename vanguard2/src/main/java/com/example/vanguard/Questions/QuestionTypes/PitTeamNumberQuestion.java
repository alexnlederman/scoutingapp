package com.example.vanguard.Questions.QuestionTypes;

import android.content.Context;
import android.view.View;

import com.example.vanguard.CustomUIElements.AnswerUITextView;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Responses.Response;

/**
 * Created by mbent on 7/9/2017.
 */

public class PitTeamNumberQuestion extends Question<Integer> {

	AnswerUITextView answerUI;
	int teamNumber;

	public PitTeamNumberQuestion(Context context, AnswerList<Response> responses, String id, int teamNumber) {
		this(context, responses, id);
		this.teamNumber = teamNumber;
	}

	public PitTeamNumberQuestion(Context context, AnswerList<Response> responses, String id) {
		super("Team Number", responses, id, false, ViewStyle.SINGLE_LINE, QuestionType.PIT_TEAM_NUMBER, false, 0);
		setupUI(context);
	}

	public PitTeamNumberQuestion(Context context, String id, int teamNumber) {
		this(context, id);
		this.teamNumber = teamNumber;
	}

	public PitTeamNumberQuestion(Context context, String id) {
		super("Team Number", id, false, ViewStyle.SINGLE_LINE, QuestionType.PIT_TEAM_NUMBER, false, 0);
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
