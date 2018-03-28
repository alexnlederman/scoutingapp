package com.example.vanguard.questions.question_types;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanguard.custom_ui_elements.answer_ui_elements.AnswerUICubeDelivery;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.example.vanguard.responses.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by mbent on 3/13/2018.
 */

public class CubeDeliveryQuestion extends Question<Map<String, Number>> {

	private AnswerUICubeDelivery answerUI;

	public CubeDeliveryQuestion(Context context, String label, AnswerList<Response> responses, String id, boolean isMatchQuestion) {
		super(label, responses, id, isMatchQuestion, ViewStyle.TWO_LINE, QuestionType.CUBE_DELIVERY, true, new HashMap<String, Number>(), new TreeMap<QuestionPropertyDescription, Object>());

		this.answerUI = new AnswerUICubeDelivery(context);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		this.answerUI.setLayoutParams(params);
	}

	@Override
	public Map<String, Number> getGenericValue() {
		return new HashMap<>();
	}

	@Override
	public View getAnswerUI() {
		return this.answerUI;
	}

	@Override
	public float convertResponseToNumber(Response<Map<String, Number>> response) {
		return 1;
	}
}
