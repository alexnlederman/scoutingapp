package com.example.vanguard.Questions;

import android.view.View;

/**
 * Created by BertTurtle on 6/1/2017.
 */

public abstract class Question<T> implements Label {


	public enum ViewStyle {
		SINGLE_LINE,
		TWO_LINE
	}

	private String label;

	public Question(String label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return this.label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	public abstract Response<T> getResponse();

	public abstract View getAnswerUI();

	public abstract ViewStyle getViewStyle();
}
