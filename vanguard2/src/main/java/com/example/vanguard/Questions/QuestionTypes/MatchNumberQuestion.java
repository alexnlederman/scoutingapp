package com.example.vanguard.Questions.QuestionTypes;

import android.content.Context;

import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Responses.Response;

/**
 * Created by BertTurtle on 6/11/2017.
 */

public class MatchNumberQuestion extends IntegerQuestion {
	public MatchNumberQuestion(Context context, String id) {
		super(context, "Match Number?", id, true);
	}

	public MatchNumberQuestion(Context context, int startingValue, String id) {
		this(context, id);
		this.answerUI.setValue(startingValue);
	}

	public MatchNumberQuestion(Context context, AnswerList<Response> responses, String id) {
		super(context, "Match Number?", responses, id, true);
	}

	public MatchNumberQuestion(Context context, int startingValue, AnswerList<Response> responses, String id) {
		this(context, responses, id);
		this.answerUI.setValue(startingValue);
	}

	@Override
	public Boolean isEditable() {
		return false;
	}

	@Override
	public Question.QuestionTypes getQuestionType() {
		return Question.QuestionTypes.MATCH_NUMBER;
	}
}
