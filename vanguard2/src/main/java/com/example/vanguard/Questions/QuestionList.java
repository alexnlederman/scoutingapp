package com.example.vanguard.Questions;

import android.content.Context;

import com.example.vanguard.Questions.QuestionTypes.IntegerQuestion;
import com.example.vanguard.Questions.QuestionTypes.StringQuestion;

import java.util.HashMap;

/**
 * Created by BertTurtle on 6/15/2017.
 */

public class QuestionList extends AnswerList<Question> {

	public enum QuestionTypes {
		INTEGER("INTEGER"),
		STRING("STRING");

		private final String text;

		private QuestionTypes(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return this.text;
		}
	}

	public QuestionList(Context context) {
		this(context, new HashMap[0]);
	}

	public QuestionList(Context context, HashMap<String, Object>[] questions) {
		setHashMap(questions, context);
	}

	public void setHashMap(HashMap<String, Object>[] hashMap, Context context) {
		this.clear();
		for (HashMap<String, Object> questionHashMap : hashMap) {
			Question<? extends Object> question = getQuestionFromHashMap(questionHashMap, context);
			this.add(question);
		}
	}

	private Question<? extends Object> getQuestionFromHashMap(HashMap<String, Object> hashMap, Context context) {
		Question<? extends Object> question = null;
		switch (QuestionTypes.valueOf((String) hashMap.get(Question.hashmap_type))) {
			case INTEGER:
				question = new IntegerQuestion(hashMap, context);
				break;
			case STRING:
				question = new StringQuestion(hashMap, context);
				break;
		}
		return question;
	}

	public HashMap<String, Object>[] getQuestionHashMaps() {
		HashMap<String, Object>[] questionHashMaps = new HashMap[this.size()];
		for (int i = 0; i < this.size(); i++) {
			questionHashMaps[i] = this.get(i).getHashMap();
		}
		return questionHashMaps;
	}
}
