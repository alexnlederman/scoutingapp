package com.example.vanguard.questions.question_types.required_questions;

import android.content.Context;
import android.view.View;

import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.question_types.IntegerQuestion;
import com.example.vanguard.responses.Response;

/**
 * Created by BertTurtle on 6/11/2017.
 */

public class MatchNumberQuestion extends IntegerQuestion {

	public MatchNumberQuestion(Context context, AnswerList<Response> responses, String id) {
		super(context, "Match Number", responses, id, null, true);
		setMinValue(0);
		setMaxValue(200);
		setIncrementation(1);
	}

	@Override
	public Boolean isEditable() {
		return false;
	}

	@Override
	public QuestionType getQuestionType() {
		return QuestionType.MATCH_NUMBER;
	}

	@Override
	public View getAnswerUI() {
		return super.getAnswerUI();
	}
}
