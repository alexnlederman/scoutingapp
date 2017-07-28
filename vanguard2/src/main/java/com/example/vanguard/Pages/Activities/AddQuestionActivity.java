package com.example.vanguard.Pages.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Properties.QuestionProperty;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionViewers.SimpleFormQuestionViewer;
import com.example.vanguard.R;
import com.example.vanguard.Responses.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbent on 7/13/2017.
 */

public class AddQuestionActivity extends AbstractActivity {

	boolean isMatchQuestion;
	LinearLayout optionsLayout;
	LinearLayout previewLayout;
	List<QuestionProperty> properties;
	Question question;
	SimpleFormQuestionViewer questionPreview;

//	List<EditText> optionValues;

	public static final String is_match_question = "is_match_question";

	public AddQuestionActivity() {
		super(R.layout.activity_add_question, R.string.add_question_page_title);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.isMatchQuestion = getIntent().getExtras().getBoolean(is_match_question);

		this.optionsLayout = (LinearLayout) findViewById(R.id.options_layout);
		this.previewLayout = (LinearLayout) findViewById(R.id.preview_layout);

		Spinner spinner = (Spinner) this.findViewById(R.id.spinner);
		spinner.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, getQuestionTypeNames()));
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String text = ((TextView) view).getText().toString();
				Question.QuestionType question = Question.QuestionType.valueOfName(text);


//				List<Map<String, String>> questionOptions = question.getOptionTitles();

//				addOptions(questionOptions);

				addQuestionPreview(question, text);

				addOptions();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		Button submit = (Button) this.findViewById(R.id.submit_button);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.databaseManager.createQuestion(question.getLabel(), question.getQuestionType().toString(), isMatchQuestion, question.getQuestionProperties());
				finish();
			}
		});
	}

	private List<String> getQuestionTypeNames() {
		Question.QuestionType[] types = Question.QuestionType.values();
		List<String> names = new ArrayList<>();
		for (int i = 0; i < types.length; i++) {
			switch (types[i]) {
				case MATCH_NUMBER:
				case MATCH_TEAM_NUMBER:
				case PIT_TEAM_NUMBER:
					break;
				default:
					names.add(types[i].getName());
			}
		}
		return names;
	}

	private void addOptions() {
		this.optionsLayout.removeAllViews();
		this.properties = this.question.getQuestionProperties();
		for (final QuestionProperty property : this.properties) {
			EditText propertyEdit = new EditText(this);
			propertyEdit.setHint(property.getName());
			propertyEdit.setText(property.getValue().toString());
			propertyEdit.setInputType(getPropertyInputType(property));

			propertyEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

					property.setValue(convertValueToType(s, property));
					List<QuestionProperty> properties = question.getQuestionProperties();
					questionPreview.refreshView();
				}

				@Override
				public void afterTextChanged(Editable s) {

				}
			});

			this.optionsLayout.addView(propertyEdit);
		}
	}

	private Object convertValueToType(CharSequence s, QuestionProperty property) {
		Class valueClass = property.getValue().getClass();
		if (Integer.class.isAssignableFrom(valueClass)) {
			return (s.length() > 0) ? Integer.valueOf(s.toString()) : 0;
		}
		if (String.class.isAssignableFrom(valueClass)) {
			return s.toString();
		}
		return null;
	}

	private int getPropertyInputType(QuestionProperty property) {
		Class valueClass = property.getValue().getClass();
		if (Integer.class.isAssignableFrom(valueClass)) {
			return InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED;
		}
		if (String.class.isAssignableFrom(valueClass)) {
			return InputType.TYPE_CLASS_TEXT;
		}
		return 0;
	}

	private void addQuestionPreview(Question.QuestionType questionType, String spinnerValue) {
		this.previewLayout.removeAllViews();
		this.question = Question.createQuestionByType(questionType, spinnerValue, new AnswerList<Response>(), this.isMatchQuestion, "", this, null);
		this.questionPreview = question.getQuestionViewer(this);
		this.previewLayout.addView(this.questionPreview);
	}
}
