package com.example.vanguard.custom_ui_elements.answer_ui_elements;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.ArrayAdapter;

import com.example.vanguard.AnswerUI;
import com.example.vanguard.R;
import com.example.vanguard.questions.ArrayQuestionUtils;

import java.util.List;

/**
 * Created by mbent on 8/17/2017.
 */

public class AnswerUISpinner extends AppCompatSpinner implements AnswerUI<String> {

	List<String> values;
	Context context;
	private final boolean isMatchQuestion;

	public AnswerUISpinner(Context context, List<String> values, boolean isMatchQuestion) {
		super(context);

		this.context = context;
		this.isMatchQuestion = isMatchQuestion;
		this.setLayoutParams(new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		this.setValues(values);
	}

	@Override
	public String getValue() {
		String value = this.getSelectedItem().toString();
		if (value.equals("") && !this.isMatchQuestion) {
			return null;
		}
		return value;
	}

	@Override
	public void setValue(String value) {
		this.setSelection(this.values.indexOf(value));
	}

	public void setValues(List<String> values) {
		values = ArrayQuestionUtils.trimList(values);
		this.values = values;
		if (!this.isMatchQuestion) {
			values.add(0, "");
		}
		this.setAdapter(new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, values));
	}
}
