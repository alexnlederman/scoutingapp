package com.example.vanguard.Questions.QuestionTypes;


import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.vanguard.CustomUIElements.AnswerUICustomNumberPicker;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Responses.Response;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by BertTurtle on 6/5/2017.
 * A question that uses the {@link AnswerUICustomNumberPicker} for its answer.
 */
public class IntegerQuestion extends Question<Integer> {

//	private final Map<String, Object> defaultProperties = new HashMap<String, Object>() {{
//		put(QUESTION_TITLE_PROPERTY_NAME, "Integer");
//		put(MIN_VALUE, 0);
//		put(MAX_VALUE, 10);
//		put(INCREMENTATION, 1);
//	}};
	public final static String MAX_VALUE = "Max Value";
	private final static String MIN_VALUE = "Min Value";
	private final static String INCREMENTATION = "Incrementation";

	protected AnswerUICustomNumberPicker answerUI;
	Context context;

	public IntegerQuestion(Context context, String label, AnswerList<Response> responses, String id, @Nullable Map<String, Object> questionProperties, boolean isMatchQuestion) {
		super(label, responses, id, isMatchQuestion, ViewStyle.SINGLE_LINE, QuestionType.INTEGER, true, null, (questionProperties == null || questionProperties.size() == 0) ? new LinkedHashMap<String, Object>() {{
			put(QUESTION_TITLE_PROPERTY_NAME, "Integer");
			put(MIN_VALUE, 0);
			put(MAX_VALUE, 10);
			put(INCREMENTATION, 1);
		}} : questionProperties);
		setup(context);
	}

	private void setup(Context context) {
		this.context = context;
		this.answerUI = new AnswerUICustomNumberPicker(this.context, getMinValue(), getMaxValue(), getIncrementation());
	}

	@Override
	public Integer getValue() {
		Integer value = 0;
		if (this.answerUI != null)
			value = this.answerUI.getValue();

		return value;
	}

	@Override
	public void setValue(Integer newValue) {
		this.answerUI.setValue(newValue);
	}

	@Override
	public View getAnswerUI() {
		this.answerUI.setMinValue(getMinValue());
		this.answerUI.setMaxValue(getMaxValue());
		this.answerUI.setIncremenation(getIncrementation());
		return this.answerUI;
	}

	private Integer getMinValue() {
		return (Integer) this.getQuestionProperties().get(MIN_VALUE);
	}

	protected void setMinValue(int newValue) {
		this.getQuestionProperties().put(MIN_VALUE, newValue);
	}

	private Integer getMaxValue() {
		return (Integer) this.getQuestionProperties().get(MAX_VALUE);
	}

	protected void setMaxValue(int newValue) {
		this.getQuestionProperties().put(MAX_VALUE, newValue);
	}

	private Integer getIncrementation() {
		return (Integer) this.getQuestionProperties().get(INCREMENTATION);
	}

	protected void setIncrementation(int newValue) {
		this.getQuestionProperties().put(INCREMENTATION, newValue);
	}

	@Override
	public float convertResponseToNumber(Response<Integer> response) {
		return response.getValue().floatValue();
	}
}
