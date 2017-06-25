package com.example.vanguard.Questions.QuestionViewers;

import android.content.Context;

import com.example.vanguard.DatabaseManager;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionTypes.MatchNumberQuestion;
import com.example.vanguard.Questions.QuestionTypes.TeamNumberQuestion;

import java.util.HashMap;

/**
 * Created by BertTurtle on 6/11/2017.
 */

public class MatchScoutQuestionList extends AnswerList<Question> {

	public MatchScoutQuestionList(DatabaseManager database, Context context) {
		this(database, context, 0, 0);
	}

	public MatchScoutQuestionList(DatabaseManager database, Context context, int teamNumberStartingValue, int matchNumberStartingValue) {
		super();
		this.addAll(database.getMatchQuestions());
		if (this.size() == 0) {
			database.createQuestion("This is the label", DatabaseManager.QuestionTypes.MATCH_NUMBER.toString(), true);
			database.createQuestion("This is the label", DatabaseManager.QuestionTypes.TEAM_NUMBER.toString(), true);
		}
	}

	// Match number question must always be second.
	public MatchNumberQuestion getMatchNumberQuestion() {
		return (MatchNumberQuestion) this.get(1);
	}

	// Team number question must always be first
	public TeamNumberQuestion getTeamNumberQuestion() {
		return (TeamNumberQuestion) this.get(0);
	}
}
