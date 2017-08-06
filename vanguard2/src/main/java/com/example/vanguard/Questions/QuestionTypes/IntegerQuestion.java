package com.example.vanguard.Questions.QuestionTypes;


import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.vanguard.CustomUIElements.AnswerUICustomNumberPicker;
import com.example.vanguard.CustomUIElements.AnswerUIInteger;
import com.example.vanguard.CustomUIElements.AnswerUISeekbar;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Responses.Response;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by BertTurtle on 6/5/2017.
 * A question that uses the {@link AnswerUICustomNumberPicker} for its answer.
 */
public class IntegerQuestion extends Question<Integer> {

	private final static String MAX_VALUE = "Max Value";
	private final static String MIN_VALUE = "Min Value";
	private final static String INCREMENTATION = "Incrementation";
	private final static String ANSWER_UI_TYPE = "Input Type";
	AnswerUIInteger answerUI;
	IntegerInputType prevInputType;
	private Context context;

	public IntegerQuestion(Context context, String label, AnswerList<Response> responses, String id, @Nullable Map<String, Object> questionProperties, boolean isMatchQuestion) {
		super(label, responses, id, isMatchQuestion, ViewStyle.SINGLE_LINE, QuestionType.INTEGER, true, null, (questionProperties == null || questionProperties.size() == 0) ? new LinkedHashMap<String, Object>() {{
			put(QUESTION_TITLE_PROPERTY_NAME, "Integer");
			put(ANSWER_UI_TYPE, IntegerInputType.COUNTER);
			put(MIN_VALUE, 0);
			put(MAX_VALUE, 10);
			put(INCREMENTATION, 1);
		}} : questionProperties);
		setup(context);
	}

	private void setup(Context context) {
		this.context = context;
		setInputType(getInputType());
	}

	private IntegerInputType getInputType() {
		System.out.println("INPUT TYPE: " + this.getQuestionProperties().get(ANSWER_UI_TYPE).getClass());
		return (IntegerInputType) IntegerInputType.valueOf(String.valueOf(this.getQuestionProperties().get(ANSWER_UI_TYPE)));
	}

	public void setInputType(IntegerInputType inputType) {
		if (prevInputType != inputType) {
			switch (inputType) {
				case COUNTER:
					this.answerUI = new AnswerUICustomNumberPicker(this.context, this.getMinValue(), this.getMaxValue(), this.getIncrementation());
					break;
				case SEEKBAR:
					this.answerUI = new AnswerUISeekbar(this.context, this.getMinValue(), this.getMaxValue(), this.getIncrementation());
					break;
			}
		}
		this.prevInputType = inputType;
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
		this.answerUI.setIncrementation(getIncrementation());
		setInputType(getInputType());
		return (View) this.answerUI;
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
	public ViewStyle getViewStyle() {

		switch (getInputType()) {
			case COUNTER:
				return ViewStyle.SINGLE_LINE;
			case SEEKBAR:
				return ViewStyle.TWO_LINE;
		}
		return null;
	}

	@Override
	public float convertResponseToNumber(Response<Integer> response) {
		return response.getValue().floatValue();
	}

	public enum IntegerInputType {
		COUNTER,
		SEEKBAR
	}
}
