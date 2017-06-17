package com.example.vanguard.Questions;

import android.view.View;

import com.example.vanguard.Responses.Response;
import com.example.vanguard.Responses.SimpleMatchResponse;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by BertTurtle on 6/1/2017.
 */

public abstract class Question<T> implements Label, Answer<T> {

	protected HashMap<String, Object> values;
	protected int matchNumber;

	public static final String hashmap_label = "label";
	public static final String hashmap_type = "type";
	public static final String hashmap_responses = "responses";
	public static final String hashmap_response_value = "value";
	public static final String hashmap_response_match_number = "value";


	public enum ViewStyle {
		SINGLE_LINE,
		TWO_LINE
	}

	public Question(HashMap<String, Object> values) {
		this.matchNumber = 0;
		this.values = values;
		if (this.values.equals(new HashMap<String, Object>())) {
			this.values.put(hashmap_label, "Insert Title Here");
			this.values.put(hashmap_type, this.getQuestionType().toString());
			this.values.put(hashmap_responses, new ArrayList<HashMap<String, Object>>());
		}
	}

	public void setMatchNumber(int matchNumber) {
		this.matchNumber = matchNumber;
	}

	@Override
	public String getLabel() {
		return (String) this.values.get(hashmap_label);
	}

	@Override
	public void setLabel(String label) {
		this.values.put(hashmap_label, label);
	}


	public void saveResponse() {
		HashMap<String, Object> response = new HashMap<>();
		response.put(hashmap_response_match_number, matchNumber);
		response.put(hashmap_response_value, this.getValue());
		((ArrayList<HashMap<String, Object>>) this.values.get(hashmap_responses)).add(response);
	}

	public ArrayList<HashMap<String,Object>> getResponses() {
		return (ArrayList<HashMap<String, Object>>) this.values.get(hashmap_responses);
	}

	public HashMap<String, Object> getHashMap() {
		return this.values;
	}

	public abstract View getAnswerUI();

	public abstract ViewStyle getViewStyle();

	public abstract QuestionList.QuestionTypes getQuestionType();

	public abstract Boolean isEditable();
}
