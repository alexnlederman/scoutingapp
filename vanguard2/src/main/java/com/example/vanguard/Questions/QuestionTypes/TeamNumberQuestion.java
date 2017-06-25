package com.example.vanguard.Questions.QuestionTypes;

import android.content.Context;

import com.example.vanguard.DatabaseManager;
import com.example.vanguard.ResponseList;
import com.example.vanguard.Responses.Response;

import java.util.HashMap;

/**
 * Created by BertTurtle on 6/11/2017.
 */

public class TeamNumberQuestion extends IntegerQuestion {

	public TeamNumberQuestion(Context context, String id) {
		super(context, "Team Number?", id);
	}

	public TeamNumberQuestion(Context context, int startingValue, String id) {
		this(context, id);
		this.answerUI.setValue(startingValue);
	}

	public TeamNumberQuestion(Context context, ResponseList responses, String id) {
		super(context, "Team Number?", responses, id);
	}

	public TeamNumberQuestion(Context context, int startingValue, ResponseList responses, String id) {
		this(context, responses, id);
		this.answerUI.setValue(startingValue);
	}

	@Override
	public Boolean isEditable() {
		return false;
	}

	@Override
	public DatabaseManager.QuestionTypes getQuestionType() {
		return DatabaseManager.QuestionTypes.TEAM_NUMBER;
	}
}
