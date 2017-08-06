package com.example.vanguard.CustomUIElements;

import com.example.vanguard.AnswerUI;

/**
 * Created by mbent on 8/5/2017.
 */

public interface AnswerUIInteger extends AnswerUI<Integer> {
	void setMinValue(int minValue);

	void setMaxValue(int maxValue);

	void setIncrementation(int incrementation);
}
