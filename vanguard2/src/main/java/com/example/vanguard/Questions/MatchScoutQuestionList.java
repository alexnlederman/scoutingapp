package com.example.vanguard.Questions;

import android.content.Context;

import com.couchbase.lite.Document;
import com.example.vanguard.Questions.QuestionTypes.MatchNumberQuestion;
import com.example.vanguard.Questions.QuestionTypes.TeamNumberQuestion;

import java.util.HashMap;

/**
 * Created by BertTurtle on 6/11/2017.
 */

public class MatchScoutQuestionList extends ScoutQuestionList {

	public MatchScoutQuestionList(Context context, Document document, String key) {
		this(context, document, 0, 0, key);
	}

	public MatchScoutQuestionList(Context context, Document document, int teamNumberStartingValue, int matchNumberStartingValue, String key) {
		super(context, document, key);
		if (this.size() == 0) {
			this.add(new TeamNumberQuestion(new HashMap<String, Object>(), context, teamNumberStartingValue));
			this.add(new MatchNumberQuestion(new HashMap<String, Object>(), context, matchNumberStartingValue));
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
