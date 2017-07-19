package com.example.vanguard.Questions.QuestionTypes;


import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.vanguard.CustomUIElements.AnswerUICustomNumberPicker;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Properties.QuestionProperty;
import com.example.vanguard.Questions.Properties.SimpleQuestionProperty;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Responses.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BertTurtle on 6/5/2017.
 * A question that uses the {@link AnswerUICustomNumberPicker} for its answer.
 */
public class IntegerQuestion extends Question<Integer> {

	Context context;

	public final int min_value_index = 1;
	public final int max_value_index = 2;
	public final int incrementation_index = 3;

	protected AnswerUICustomNumberPicker answerUI;

	private static final List<QuestionProperty> defaultProperties = new ArrayList<QuestionProperty>() {{
		add(new SimpleQuestionProperty<>(0, "Min Value"));
		add(new SimpleQuestionProperty<>(10, "Max Value"));
		add(new SimpleQuestionProperty<>(1, "Incrementation"));
	}};

	public IntegerQuestion(Context context, String label, AnswerList<Response> responses, String id, @Nullable List<QuestionProperty> questionProperties, boolean isMatchQuestion) {
		super(label, responses, id, isMatchQuestion, ViewStyle.SINGLE_LINE, QuestionType.INTEGER, true, null, (QuestionProperty[]) ((questionProperties == null || questionProperties.size() == 0) ? defaultProperties.toArray(new QuestionProperty[defaultProperties.size()]) : questionProperties.toArray(new QuestionProperty[defaultProperties.size()])));
		setup(context);
	}

	public IntegerQuestion(Context context, String label, String id, @Nullable List<QuestionProperty> properties, boolean isMatchQuestion) {
		this(context, label, new AnswerList<Response>(), id, properties, isMatchQuestion);
		setup(context);
	}

	private void setup(Context context) {
		this.context = context;
	}

	@Override
	public Integer getValue() {
		return (this.answerUI == null) ? 0 : this.answerUI.getValue();
	}

	@Override
	public void setValue(Integer newValue) {
		this.answerUI.setValue(newValue);
	}

	@Override
	public View getAnswerUI() {
		return new AnswerUICustomNumberPicker(this.context, getMinValue(), getMaxValue(), getIncrementation());
	}

	private Integer getMinValue() {
		return (Integer) this.getQuestionProperties().get(min_value_index).getValue();
	}

	private Integer getMaxValue() {
		return (Integer) this.getQuestionProperties().get(max_value_index).getValue();
	}

	private Integer getIncrementation() {
		return (Integer) this.getQuestionProperties().get(incrementation_index).getValue();
	}

	protected void setMinValue(int newValue) {
		this.getQuestionProperties().get(min_value_index).setValue(newValue);
	}

	protected void setMaxValue(int newValue) {
		this.getQuestionProperties().get(max_value_index).setValue(newValue);
	}

	protected void getIncrementation(int newValue) {
		this.getQuestionProperties().get(incrementation_index).setValue(newValue);
	}

	@Override
	public float convertResponseToNumber(Response<Integer> response) {
		return response.getValue().floatValue();
	}
}
