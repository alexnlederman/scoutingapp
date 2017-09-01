package com.example.vanguard.questions.question_types;

import android.content.Context;
import android.view.View;

import com.example.vanguard.custom_ui_elements.answer_ui_elements.AnswerUIBooleanRadioButtons;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.example.vanguard.responses.Response;

import java.util.TreeMap;

/**
 * Created by mbent on 8/5/2017.
 */

public class BooleanQuestion extends Question<Boolean> {

	private AnswerUIBooleanRadioButtons answerUI;

	public BooleanQuestion(Context context, String label, AnswerList<Response> responses, String id, boolean isMatchQuestion) {
		super(label, responses, id, isMatchQuestion, ViewStyle.SINGLE_LINE, QuestionType.BOOLEAN, true, null, new TreeMap<String, Object>());
		this.answerUI = new AnswerUIBooleanRadioButtons(context);
		if (isMatchQuestion) {
			this.setValue(false);
		}
	}

	@Override
	public View getAnswerUI() {
		return this.answerUI;
	}

	@Override
	public float convertResponseToNumber(Response<Boolean> response) {
		if (response.getValue()) {
			return 1;
		}
		return 0;
	}

	@Override
	public Boolean getGenericValue() {
		return true;
	}
}
