package com.example.vanguard.Pages.Activities;

import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.vanguard.Bluetooth.BluetoothManager;
import com.example.vanguard.CustomUIElements.HintEditText;
import com.example.vanguard.Pages.Fragments.DialogFragments.ConfirmationDialogFragment;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionViewers.SimpleFormQuestionViewer;
import com.example.vanguard.R;
import com.example.vanguard.Responses.Response;

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

	public AddQuestionActivity() {
		super(R.layout.activity_add_question, R.string.add_question_page_title);
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
		for (final String propertyName : this.properties.keySet()) {
			HintEditText propertyEdit = new HintEditText(this, propertyName);
			propertyEdit.getEditText().setText(this.properties.get(propertyName).toString());
			propertyEdit.getEditText().setInputType(getPropertyInputType(this.properties.get(propertyName)));

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
		if (propertyValue instanceof Integer) {
			return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED;
		}
		if (propertyValue instanceof String) {
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
