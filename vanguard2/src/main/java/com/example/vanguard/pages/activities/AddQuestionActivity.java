package com.example.vanguard.pages.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.vanguard.R;
import com.example.vanguard.bluetooth.BluetoothManager;
import com.example.vanguard.custom_ui_elements.HintEditText;
import com.example.vanguard.custom_ui_elements.HintSpinner;
import com.example.vanguard.pages.fragments.dialog_fragments.ConfirmationDialogFragment;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.example.vanguard.questions.question_viewers.SimpleFormQuestionViewer;
import com.example.vanguard.responses.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mbent on 7/13/2017.
 */

public class AddQuestionActivity extends AbstractActivity {

	public static final String is_match_question = "is_match_question";
	boolean isMatchQuestion;
	LinearLayout optionsLayout;
	LinearLayout previewLayout;
	Map<String, Object> properties;
	Question question;

	//	List<EditText> optionValues;
	SimpleFormQuestionViewer questionPreview;

	AddQuestionActivity that;

	public AddQuestionActivity() {
		super(R.layout.activity_add_question, R.string.add_question_page_title);
		this.that = this;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.isMatchQuestion = getIntent().getExtras().getBoolean(is_match_question);

		this.optionsLayout = (LinearLayout) findViewById(R.id.options_layout);
		this.previewLayout = (LinearLayout) findViewById(R.id.preview_layout);

		if (!BluetoothManager.isServer(this)) {
			ConfirmationDialogFragment warning = ConfirmationDialogFragment.newInstance(R.string.confirm_not_server_add_question_title, R.string.confirm_not_server_add_question_text, new ConfirmationDialogFragment.ConfirmDialogListener() {
				@Override
				public void decline() {
					finish();
				}
			});
			warning.show(getFragmentManager(), "Confirm Add Question");
		}

		Spinner spinner = (Spinner) this.findViewById(R.id.spinner);
		spinner.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, getQuestionTypeNames()));
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String text = ((TextView) view).getText().toString();
				Question.QuestionType question = Question.QuestionType.valueOfName(text);

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
				View focus = getCurrentFocus();
				if (focus != null) {
					InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					manager.hideSoftInputFromWindow(focus.getWindowToken(), 0);
				}
				finish();
			}
		});
	}

	private List<String> getQuestionTypeNames() {
		Question.QuestionType[] types = Question.QuestionType.values();
		List<String> names = new ArrayList<>();
		for (Question.QuestionType type : types) {
			switch (type) {
				case MATCH_NUMBER:
				case MATCH_TEAM_NUMBER:
				case PIT_TEAM_NUMBER:
					break;
				default:
					names.add(type.getName());
			}
		}
		return names;
	}

	private void addOptions() {
		this.optionsLayout.removeAllViews();
		this.properties = this.question.getQuestionProperties();
		for (final String propertyName : this.properties.keySet()) {
			Object value = this.properties.get(propertyName);
			if (value instanceof Enum<?>) {
				HintSpinner spinner = new HintSpinner(this, ((Enum) value).getDeclaringClass().getEnumConstants(), propertyName);
				spinner.setSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
						properties.put(propertyName, parent.getItemAtPosition(position));
						if (questionPreview.getViewStyle() != question.getViewStyle()) {
							addQuestionPreview(question);
						}
						questionPreview.refreshView();
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
				this.optionsLayout.addView(spinner);
			} else if (value instanceof Object[]) {
				HintEditText propertyEdit = new HintEditText(this, propertyName);
				propertyEdit.getEditText().setText(TextUtils.join(",", (Object[]) value));
				propertyEdit.getEditText().setInputType(getPropertyInputType(value));

				propertyEdit.getEditText().addTextChangedListener(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {

					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						String[] values = s.toString().split(",");
						properties.put(propertyName, values);
						questionPreview.refreshView();
					}

					@Override
					public void afterTextChanged(Editable s) {

					}
				});
				this.optionsLayout.addView(propertyEdit);
			} else {
				HintEditText propertyEdit = new HintEditText(this, propertyName);
				propertyEdit.getEditText().setText(value.toString());
				propertyEdit.getEditText().setInputType(getPropertyInputType(value));

				propertyEdit.getEditText().addTextChangedListener(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {

					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						properties.put(propertyName, convertValueToType(s, properties.get(propertyName)));
						questionPreview.refreshView();
					}

					@Override
					public void afterTextChanged(Editable s) {

					}
				});
				this.optionsLayout.addView(propertyEdit);
			}
		}
	}

	private Object convertValueToType(CharSequence s, Object propertyValue) {
		if (propertyValue instanceof Integer) {
			return (s.length() > 0 && !s.toString().equals("-")) ? Integer.valueOf(s.toString()) : 0;
		}
		if (propertyValue instanceof String) {
			return s.toString();
		}
		return null;
	}

	private int getPropertyInputType(Object propertyValue) {
		if (propertyValue instanceof Integer || propertyValue instanceof Integer[]) {
			return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED;
		}
		if (propertyValue instanceof String || propertyValue instanceof String[]) {
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

	private void addQuestionPreview(Question question) {
		this.previewLayout.removeAllViews();
		this.question = question;
		this.questionPreview = question.getQuestionViewer(this);
		this.previewLayout.addView(this.questionPreview);
	}
}
