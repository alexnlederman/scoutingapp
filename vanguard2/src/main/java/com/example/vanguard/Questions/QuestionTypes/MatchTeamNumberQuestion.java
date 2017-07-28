package com.example.vanguard.Questions.QuestionTypes;

import android.content.Context;

import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Responses.Response;

/**
 * Created by BertTurtle on 6/11/2017.
 */

public class MatchTeamNumberQuestion extends IntegerQuestion {

	public MatchTeamNumberQuestion(Context context, String id) {
		this(context, new AnswerList<Response>(), id);
	}

	public MatchTeamNumberQuestion(Context context, int startingValue, String id) {
		this(context, id);
		this.answerUI.setValue(startingValue);
	}

	public MatchTeamNumberQuestion(Context context, AnswerList<Response> responses, String id) {
		super(context, "Team Number", responses, id, null, true);
		setMinValue(0);
		setMaxValue(100000);
	}

	public MatchTeamNumberQuestion(Context context, int startingValue, AnswerList<Response> responses, String id) {
		this(context, responses, id);
		this.answerUI.setValue(startingValue);
	}

	@Override
	public Boolean isEditable() {
		return false;
	}

	@Override
	public QuestionType getQuestionType() {
		return QuestionType.MATCH_TEAM_NUMBER;
	}
}
