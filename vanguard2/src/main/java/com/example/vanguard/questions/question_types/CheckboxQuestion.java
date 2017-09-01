package com.example.vanguard.questions.question_types;

import android.content.Context;
import android.view.View;

import com.example.vanguard.custom_ui_elements.answer_ui_elements.AnswerUICheckboxes;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.ArrayQuestionUtils;
import com.example.vanguard.questions.Question;
import com.example.vanguard.responses.Response;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by mbent on 8/18/2017.
 */

public class CheckboxQuestion extends Question<String> {

	private static final String CHECKBOX_VALUE_PROPERTY_NAME = "Comma Separated Values";

	private AnswerUICheckboxes answerUI;

	public CheckboxQuestion(Context context, String label, AnswerList<Response> responses, String id, boolean isMatchQuestion, Map<String, Object> properties) {
		super(label, responses, id, isMatchQuestion, ViewStyle.SINGLE_LINE, QuestionType.CHECKBOXES, true, null, properties);

		this.answerUI = new AnswerUICheckboxes(context, getCheckboxValues(), this.isMatchQuestion());
	}

	private String[] getCheckboxValues() {
		Object value = this.getQuestionProperties().get(CHECKBOX_VALUE_PROPERTY_NAME);
		return ArrayQuestionUtils.getStringArrayFromProperty(value);
	}

	public void setCheckboxValues(String[] values) {
		this.getQuestionProperties().put(CHECKBOX_VALUE_PROPERTY_NAME, values);
	}

	@Override
	public View getAnswerUI() {
		this.answerUI.setCheckboxes(this.getCheckboxValues());
		return this.answerUI;
	}

	@Override
	public float convertResponseToNumber(Response<String> response) {
		return 1;
	}

	@Override
	public LinkedHashMap<String, Object> getDefaultProperties() {
		LinkedHashMap<String, Object> props = super.getDefaultProperties();
		props.put(CHECKBOX_VALUE_PROPERTY_NAME, new String[0]);
		return props;
	}

	@Override
	public String getGenericValue() {
		return "HI";
	}
}
