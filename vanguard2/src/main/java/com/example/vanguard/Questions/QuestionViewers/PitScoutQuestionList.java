package com.example.vanguard.Questions.QuestionViewers;

import android.content.Context;

import com.example.vanguard.DatabaseManager;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionTypes.TeamNumberQuestion;

import java.util.HashMap;

/**
 * Created by BertTurtle on 6/15/2017.
 */

public class PitScoutQuestionList extends AnswerList<Question> {

	// Team number question needs to always be first.
	public PitScoutQuestionList(DatabaseManager database, Context context) {
		this(database, 0, context);
	}

	public PitScoutQuestionList(DatabaseManager database, int teamNumberStartingValue, Context context) {
		super();
		this.addAll(database.getPitQuestions());
		if (this.size() == 0) {
			this.add(new TeamNumberQuestion(context, "DFLSFJSKLF"));
		}
	}

	public TeamNumberQuestion getTeamNumberQuestion() {
		return (TeamNumberQuestion) this.get(0);
	}
}
