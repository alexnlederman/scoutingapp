package com.example.vanguard.questions.question_types.required_questions;

import android.content.Context;
import android.view.View;

import com.example.vanguard.custom_ui_elements.answer_ui_elements.AnswerUITextView;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.example.vanguard.responses.Response;

import java.util.TreeMap;

/**
 * Created by mbent on 7/9/2017.
 */

public class PitTeamNumberQuestion extends Question<Integer> {

	private AnswerUITextView answerUI;
	private int teamNumber;

	public PitTeamNumberQuestion(Context context, AnswerList<Response> responses, String id) {
		super("Team Number", responses, id, false, ViewStyle.SINGLE_LINE, QuestionType.PIT_TEAM_NUMBER, false, 0, new TreeMap<String, Object>());
		setupUI(context);
	}

	private void setupUI(Context context) {
		this.answerUI = new AnswerUITextView(context);
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

	@Override
	public Integer getGenericValue() {
		return 0;
	}
}
