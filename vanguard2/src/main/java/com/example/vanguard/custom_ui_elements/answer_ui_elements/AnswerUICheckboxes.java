package com.example.vanguard.custom_ui_elements.answer_ui_elements;

import android.content.Context;
import android.text.TextUtils;
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

public class AnswerUICheckboxes extends LinearLayout implements AnswerUI<String> {

	private final static String SEPARATOR = ", ";
	private CheckBox[] checkBoxes;
	private Context context;
	private boolean changed = true;
	private final boolean isMatchQuestion;

	public AnswerUICheckboxes(Context context, String[] values, boolean isMatchQuestion) {
		super(context);
		this.isMatchQuestion = isMatchQuestion;
		this.setOrientation(VERTICAL);
		this.context = context;

		setCheckboxes(values);
	}

	public void setCheckboxes(String[] values) {
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

	@Override
	public String getValue() {
		if (!changed && !this.isMatchQuestion) {
			return null;
		}
		return TextUtils.join(SEPARATOR, getValues());
	}

	@Override
	public void setValue(String value) {
		if (value == null) {
			this.changed = false;
		} else {
			String[] values = TextUtils.split(value, SEPARATOR);
			for (String name : values) {
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
			checkedNames.add(checkBox.getText().toString());
		}
		return checkedNames;
	}
}
