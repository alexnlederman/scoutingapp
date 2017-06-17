package com.example.vanguard.Questions.QuestionViewers;

import android.content.Context;

import com.example.vanguard.Questions.QuestionList;
import com.example.vanguard.Questions.QuestionTypes.MatchNumberQuestion;
import com.example.vanguard.Questions.QuestionTypes.TeamNumberQuestion;

import java.util.HashMap;

/**
 * Created by BertTurtle on 6/15/2017.
 */

public class PitScoutQuestionList extends QuestionList {

	// Team number question needs to always be first.
	public PitScoutQuestionList(Context context, HashMap<String, Object>[] hashMap) {
		this(context, hashMap, 0);
	}

	public PitScoutQuestionList(Context context, HashMap<String, Object>[] hashMap, int teamNumberStartingValue) {
		super(context, hashMap);
		if (this.size() == 0) {
			this.add(new TeamNumberQuestion(new HashMap<String, Object>(), context, teamNumberStartingValue));
		}
	}

	public TeamNumberQuestion getTeamNumberQuestion() {
		return (TeamNumberQuestion) this.get(0);
	}
}
