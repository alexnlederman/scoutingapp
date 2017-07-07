package com.example.vanguard.Questions.QuestionTypes;

import android.content.Context;

import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Responses.Response;

/**
 * Created by BertTurtle on 6/11/2017.
 */

public class TeamNumberQuestion extends IntegerQuestion {

	public TeamNumberQuestion(Context context, String id, boolean isMatchQuestion) {
		super(context, "Team Number?", id, isMatchQuestion);
	}

	public TeamNumberQuestion(Context context, int startingValue, String id, boolean isMatchQuestion) {
		this(context, id, isMatchQuestion);
		this.answerUI.setValue(startingValue);
	}

	public TeamNumberQuestion(Context context, AnswerList<Response> responses, String id, boolean isMatchQuestion) {
		super(context, "Team Number?", responses, id, isMatchQuestion);
	}

	public TeamNumberQuestion(Context context, int startingValue, AnswerList<Response> responses, String id, boolean isMatchQuestion) {
		this(context, responses, id, isMatchQuestion);
		this.answerUI.setValue(startingValue);
	}

	@Override
	public Boolean isEditable() {
		return false;
	}

	@Override
	public Question.QuestionTypes getQuestionType() {
		return Question.QuestionTypes.TEAM_NUMBER;
	}
}
