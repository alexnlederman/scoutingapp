package com.example.vanguard.questions.question_types;

import android.content.Context;
import android.view.View;

import com.example.vanguard.custom_ui_elements.answer_ui_elements.AnswerUITimer;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.example.vanguard.responses.Response;

import java.util.TreeMap;

/**
 * Created by mbent on 8/18/2017.
 */

public class TimerQuestion extends Question<Double> {

	private AnswerUITimer answerUI;

	public TimerQuestion(Context context, String label, AnswerList<Response> responses, String id, boolean isMatchQuestion) {
		super(label, responses, id, isMatchQuestion, ViewStyle.SINGLE_LINE, QuestionType.TIMER, true, null, new TreeMap<String, Object>());
		this.answerUI = new AnswerUITimer(context, this.isMatchQuestion());
	}

	@Override
	public View getAnswerUI() {
		return this.answerUI;
	}

	@Override
	public float convertResponseToNumber(Response<Double> response) {
		return response.getValue().floatValue();
	}

	@Override
	public Double getGenericValue() {
		return 4d;
	}
}
