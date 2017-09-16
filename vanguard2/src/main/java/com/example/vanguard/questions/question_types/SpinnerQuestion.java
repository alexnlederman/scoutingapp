package com.example.vanguard.questions.question_types;

import android.content.Context;
import android.view.View;

import com.example.vanguard.custom_ui_elements.answer_ui_elements.AnswerUISpinner;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.ArrayQuestionUtils;
import com.example.vanguard.questions.Question;
import com.example.vanguard.responses.Response;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by mbent on 8/17/2017.
 */

public class SpinnerQuestion extends Question<String> {

	private AnswerUISpinner answerUI;

	public SpinnerQuestion(Context context, String label, AnswerList<Response> responses, String id, boolean isMatchQuestion, Map<QuestionPropertyDescription, Object> properties) {
		super(label, responses, id, isMatchQuestion, ViewStyle.TWO_LINE, QuestionType.SPINNER, true, null, properties);
		this.answerUI = new AnswerUISpinner(context, Arrays.asList(this.getPossibleValues()), this.isMatchQuestion());
	}

	@Override
	public View getAnswerUI() {
		this.answerUI.setValues(Arrays.asList(this.getPossibleValues()));
		return this.answerUI;
	}

	@Override
	public String getValue() {
		return this.answerUI.getValue();
	}

	@Override
	public float convertResponseToNumber(Response<String> response) {
		return 1;
	}

	private String[] getPossibleValues() {
		Object values = this.getPropertyValue(QuestionPropertyDescription.CSV);
		return ArrayQuestionUtils.getStringArrayFromProperty(values);
	}

	private void setPossibleValues(String[] options) {
		this.setPropertyValue(QuestionPropertyDescription.CSV, options);
	}

	@Override
	public LinkedHashMap<QuestionPropertyDescription, Object> getDefaultProperties() {
		LinkedHashMap<QuestionPropertyDescription, Object> map = super.getDefaultProperties();
		map.put(QuestionPropertyDescription.CSV, new String[0]);
		return map;
	}

	@Override
	public String getGenericValue() {
		return "HI";
	}
}
