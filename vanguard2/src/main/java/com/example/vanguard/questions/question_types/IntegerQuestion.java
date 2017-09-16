package com.example.vanguard.questions.question_types;


import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.vanguard.EnumName;
import com.example.vanguard.custom_ui_elements.answer_ui_elements.AnswerUICustomNumberPicker;
import com.example.vanguard.custom_ui_elements.answer_ui_elements.AnswerUIInteger;
import com.example.vanguard.custom_ui_elements.answer_ui_elements.AnswerUISeekbar;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.example.vanguard.responses.Response;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by BertTurtle on 6/5/2017.
 * A question that uses the {@link AnswerUICustomNumberPicker} for its answer.
 */
public class IntegerQuestion extends Question<Integer> {

	protected AnswerUIInteger answerUI;
	IntegerInputType prevInputType;
	private Context context;

	public IntegerQuestion(Context context, String label, AnswerList<Response> responses, String id, @Nullable Map<QuestionPropertyDescription, Object> questionProperties, boolean isMatchQuestion) {
		super(label, responses, id, isMatchQuestion, ViewStyle.SINGLE_LINE, QuestionType.INTEGER, true, null, questionProperties);
		setup(context);
	}

	private void setup(Context context) {
		this.context = context;
		if (this.getQuestionProperties().get(QuestionPropertyDescription.UI_TYPE) instanceof String) {
			this.getQuestionProperties().put(QuestionPropertyDescription.UI_TYPE, IntegerInputType.value(this.getQuestionProperties().get(QuestionPropertyDescription.UI_TYPE)));
		}
		setInputType(getInputType());
	}

	private IntegerInputType getInputType() {
		return IntegerInputType.value(this.getPropertyValue(QuestionPropertyDescription.UI_TYPE));
	}

	private void setInputType(IntegerInputType inputType) {
		if (prevInputType != inputType) {
			switch (inputType) {
				case COUNTER:
					this.answerUI = new AnswerUICustomNumberPicker(this.context, this.getMinValue(), this.getMaxValue(), this.getIncrementation(), this.isMatchQuestion());
					break;
				case SEEKBAR:
					this.answerUI = new AnswerUISeekbar(this.context, this.getMinValue(), this.getMaxValue(), this.getIncrementation(), this.isMatchQuestion());
					break;
			}
		}
		this.prevInputType = inputType;
	}

	@Override
	public LinkedHashMap<QuestionPropertyDescription, Object> getDefaultProperties() {
		LinkedHashMap<QuestionPropertyDescription, Object> map = super.getDefaultProperties();
		map.put(QuestionPropertyDescription.MIN_VALUE, 0);
		map.put(QuestionPropertyDescription.MAX_VALUE, 10);
		map.put(QuestionPropertyDescription.INCREMENTATION, 1);
		map.put(QuestionPropertyDescription.UI_TYPE, IntegerInputType.COUNTER);
		return map;
	}

	@Override
	public Integer getValue() {
		Integer value = 0;
		if (this.answerUI != null)
			value = this.answerUI.getValue();

		return value;
	}

	@Override
	public Integer getGenericValue() {
		return 0;
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
		return (Integer) this.getPropertyValue(QuestionPropertyDescription.MIN_VALUE);
	}

	protected void setMinValue(int newValue) {
		this.setPropertyValue(QuestionPropertyDescription.MIN_VALUE, newValue);
	}

	private Integer getMaxValue() {
		return (Integer) this.getPropertyValue(QuestionPropertyDescription.MAX_VALUE);
	}

	protected void setMaxValue(int newValue) {
		this.setPropertyValue(QuestionPropertyDescription.MAX_VALUE, newValue);
	}

	private Integer getIncrementation() {
		return (Integer) this.getPropertyValue(QuestionPropertyDescription.INCREMENTATION);
	}

	protected void setIncrementation(int newValue) {
		this.setPropertyValue(QuestionPropertyDescription.INCREMENTATION, newValue);
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

	public enum IntegerInputType implements EnumName {
		COUNTER("Counter"),
		SEEKBAR("Seekbar");

		String name;

		IntegerInputType(String name) {
			this.name = name;
		}

		public static IntegerInputType value(Object value) {
			if (value instanceof IntegerInputType) {
				return (IntegerInputType) value;
			} else {
				for (IntegerInputType ord : IntegerInputType.values()) {
					if (ord.getName().equals(value) || ord.name().equals(value)) {
						return ord;
					}
				}
			}
			return null;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}
}
