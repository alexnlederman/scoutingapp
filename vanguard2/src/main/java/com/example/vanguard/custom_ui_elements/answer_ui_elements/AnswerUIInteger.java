package com.example.vanguard.custom_ui_elements.answer_ui_elements;

import com.example.vanguard.AnswerUI;

/**
 * Created by mbent on 8/5/2017.
 */

public interface AnswerUIInteger extends AnswerUI<Integer> {
	void setMinValue(int minValue);

	void setMaxValue(int maxValue);

	void setIncrementation(int incrementation);
}
