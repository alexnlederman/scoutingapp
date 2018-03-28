package com.example.vanguard.custom_ui_elements.answer_ui_elements;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.example.vanguard.AnswerUI;
import com.example.vanguard.questions.ArrayQuestionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mbent on 8/18/2017.
 */

public class AnswerUICheckboxes extends LinearLayout implements AnswerUI<List<String>> {

	private final static String SEPARATOR = ", ";
	private final boolean isMatchQuestion;
	private CheckBox[] checkBoxes;
	private Context context;
	private boolean changed = true;
	private String[] currentCheckBoxes;

	public AnswerUICheckboxes(Context context, String[] values, boolean isMatchQuestion) {
		super(context);
		this.isMatchQuestion = isMatchQuestion;
		this.setOrientation(VERTICAL);
		this.context = context;

		setCheckboxes(values);
	}

	public void setCheckboxes(String[] values) {
		if (values != null) {
			if (!Arrays.equals(values, this.currentCheckBoxes)) {
				this.currentCheckBoxes = values;
				List<String> strings = ArrayQuestionUtils.trimList(Arrays.asList(values));
				this.checkBoxes = new CheckBox[strings.size()];
				this.removeAllViews();
				for (int i = 0; i < strings.size(); i++) {
					CheckBox checkBox = new CheckBox(context);
					checkBox.setText(strings.get(i));
					checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							changed = true;
						}
					});
					this.checkBoxes[i] = checkBox;
					this.addView(checkBox);
				}
			}
		}
	}

	@Override
	public List<String> getValue() {
		if (!changed && !this.isMatchQuestion) {
			return null;
		}
		List<String> values = this.getValues();
		return values;
	}

	@Override
	public void setValue(List<String> value) {
		if (value == null) {
			this.changed = false;
		} else {
			for (String name : value) {
				for (CheckBox checkBox : this.checkBoxes) {
					if (name.equals(checkBox.getText())) {
						checkBox.setChecked(true);
					}
				}
			}
		}
	}

	private List<String> getValues() {
		List<String> checkedNames = new ArrayList<>();
		for (CheckBox checkBox : this.checkBoxes) {
			if (checkBox.isChecked()) {
				checkedNames.add(checkBox.getText().toString());
			}
		}
		return checkedNames;
	}
}
