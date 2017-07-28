package com.example.vanguard.Questions;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.example.vanguard.CustomUIElements.MaterialLinearLayout;
import com.example.vanguard.Questions.QuestionViewers.CheckBoxQuestionViewer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbent on 7/6/2017.
 */

public class QuestionSelectorViewer extends LinearLayout {

	List<CheckBoxQuestionViewer> questionViewers;

	public QuestionSelectorViewer(Context context, AnswerList<? extends Question> questions) {
		super(context);

		this.setOrientation(VERTICAL);

		questionViewers = new ArrayList<>();

		for (Question question : questions) {
			CheckBoxQuestionViewer questionViewer = new CheckBoxQuestionViewer(context, question);
			questionViewers.add(questionViewer);
			this.addView(questionViewer);
		}
	}

	public int getAmountSelected() {
		int amount = 0;
		for (CheckBoxQuestionViewer questionViewer : questionViewers) {
			if (questionViewer.isChecked())
				amount++;
		}
		return amount;
	}

	public AnswerList<? extends Question> getSelectedQuestions() {
		AnswerList<Question> questions = new AnswerList<>();
		for (CheckBoxQuestionViewer questionViewer : questionViewers) {
			if (questionViewer.isChecked())
				questions.add(questionViewer.getQuestion());
		}
		return questions;
	}
}
