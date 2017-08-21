package com.example.vanguard.questions.question_types.required_questions;

import android.content.Context;

import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.question_types.IntegerQuestion;
import com.example.vanguard.responses.Response;

/**
 * Created by BertTurtle on 6/11/2017.
 */

public class MatchTeamNumberQuestion extends IntegerQuestion {

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
