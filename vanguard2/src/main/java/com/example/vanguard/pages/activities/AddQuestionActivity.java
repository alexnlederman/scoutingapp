package com.example.vanguard.pages.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
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

import static com.example.vanguard.pages.activities.ScoutSettingsActivity.QUESTION_ADDED;

/**
 * Created by mbent on 7/13/2017.
 */

public class AddQuestionActivity extends AbstractActivity {

	public static final String IS_MATCH_QUESTION = "IS_MATCH_QUESTION";
	public static final String EDIT_QUESTION = "EDIT_QUESTION";
	boolean isMatchQuestion;
	LinearLayout optionsLayout;
	LinearLayout previewLayout;
	Map<Question.QuestionPropertyDescription, Object> properties;
	Question question;
	boolean editQuestion;


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

		this.isMatchQuestion = getIntent().getExtras().getBoolean(IS_MATCH_QUESTION);
		final String questionID = getIntent().getExtras().getString(EDIT_QUESTION);
		Spinner spinner = (Spinner) this.findViewById(R.id.spinner);

		this.optionsLayout = (LinearLayout) findViewById(R.id.options_layout);
		this.previewLayout = (LinearLayout) findViewById(R.id.preview_layout);

		if (questionID != null) {
			this.question = MainActivity.databaseManager.getQuestionsFromIds(questionID).get(0);
			spinner.setVisibility(View.GONE);
			this.editQuestion = true;
			addQuestionPreview(this.question);
			addOptions();
		} else {
			this.editQuestion = false;
			spinner.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, getQuestionTypeNames()));
			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					String text = ((TextView) view).getText().toString();
					Question.QuestionType questionType = Question.QuestionType.valueOfName(text);

					addQuestionPreview(questionType, text);

					// TODO this is missing.
					addOptions();
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});
		}

		if (!BluetoothManager.isServer(this)) {
			// TODO change warning texts.
			ConfirmationDialogFragment warning = ConfirmationDialogFragment.newInstance(R.string.confirm_not_server_add_question_title, R.string.confirm_not_server_add_question_text, new ConfirmationDialogFragment.ConfirmDialogListener() {
				@Override
				public void decline() {
					finish();
				}
			});
			warning.show(getFragmentManager(), "Confirm Add Question");
		}

		Button submit = (Button) this.findViewById(R.id.submit_button);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("EDIT: " + editQuestion);
				if (editQuestion) {
					MainActivity.databaseManager.setQuestionProperties(question, question.getQuestionProperties());
				} else
					MainActivity.databaseManager.createQuestion(question.getLabel(), question.getQuestionType().toString(), isMatchQuestion, question.getQuestionProperties());
				setResult(QUESTION_ADDED);
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
		// TODO this may crash when question edited.
		this.properties = this.question.getQuestionProperties();
		for (final Question.QuestionPropertyDescription propertyDescription : this.properties.keySet()) {
			Object value = this.properties.get(propertyDescription);
			switch (propertyDescription.type) {
				case ARRAY:
					HintEditText propertyEdit = new HintEditText(this, propertyDescription.title);
					if (value instanceof List) {
						value = ((List) value).toArray();
					}
					propertyEdit.getEditText().setText(TextUtils.join(",", (Object[]) value));
//					propertyEdit.getEditText().setInputType(getPropertyInputType(value));

					propertyEdit.getEditText().addTextChangedListener(new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {

						}

						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							String[] values = s.toString().split(",");
							properties.put(propertyDescription, values);
							questionPreview.refreshView();
						}

						@Override
						public void afterTextChanged(Editable s) {

						}
					});
					this.optionsLayout.addView(propertyEdit);
					break;
				case NUMBER:
					HintEditText propertyEditText = new HintEditText(this, propertyDescription.title);
					propertyEditText.getEditText().setText(String.valueOf(value));
					propertyEditText.getEditText().setInputType(getNumberInputType());

					propertyEditText.getEditText().addTextChangedListener(new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {

						}

						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							properties.put(propertyDescription, convertStringToInt(s.toString()));
							questionPreview.refreshView();
						}

						@Override
						public void afterTextChanged(Editable s) {

						}
					});
					this.optionsLayout.addView(propertyEditText);
					break;
				case STRING:
					HintEditText propertyTextEdit = new HintEditText(this, propertyDescription.title);
					propertyTextEdit.getEditText().setText(String.valueOf(value));

					propertyTextEdit.getEditText().addTextChangedListener(new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {

						}

						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							properties.put(propertyDescription, s.toString());
							questionPreview.refreshView();
						}

						@Override
						public void afterTextChanged(Editable s) {

						}
					});
					this.optionsLayout.addView(propertyTextEdit);
					break;
				case ENUM:
					HintSpinner spinner = new HintSpinner(this, ((Enum) value).getDeclaringClass().getEnumConstants(), propertyDescription.title);
					spinner.setSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
							properties.put(propertyDescription, parent.getItemAtPosition(position));
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
					break;
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

	public Integer convertStringToInt(String string) {
		return (string.length() > 0 && !string.equals("-")) ? Integer.valueOf(string) : 0;
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

	private int getNumberInputType() {
		return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED;
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
