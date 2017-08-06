package com.example.vanguard.Pages.Activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.view.View;
import android.widget.Toast;

import com.example.vanguard.CustomUIElements.SettingsView;
import com.example.vanguard.DatabaseManager;
import com.example.vanguard.Pages.Fragments.DialogFragments.AddEventDialogFragment;
import com.example.vanguard.Pages.Fragments.DialogFragments.ConfirmationDialogFragment;
import com.example.vanguard.Pages.Fragments.DialogFragments.SetTeamNumberDialogFragment;
import com.example.vanguard.Pages.Fragments.GraphSettingsFragment;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.R;
import com.example.vanguard.Responses.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class SettingsActivity extends AbstractActivity {

	Activity that;

	public SettingsActivity() {
		super(R.layout.activity_settings, R.string.setting_page_title);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		that = this;

		SettingsView addEvent = (SettingsView) findViewById(R.id.add_event);
		addEvent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				DialogFragment fragment = AddEventDialogFragment.newInstance(new AddEventDialogFragment.DialogOpener() {
					@Override
					public void openDialog(DialogFragment dialog) {
						dialog.show(getFragmentManager(), "Fragment");
					}

					@Override
					public int describeContents() {
						return 0;
					}

					@Override
					public void writeToParcel(Parcel dest, int flags) {

					}
				});
				fragment.show(getFragmentManager(), "Event Adder");
			}


		});

		SettingsView scoutSettings = (SettingsView) findViewById(R.id.scout_settings);
		scoutSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(that, ScoutSettingsActivity.class);
				startActivity(intent);
			}
		});

		SettingsView deleteResponsesSettings = (SettingsView) findViewById(R.id.delete_responses);
		deleteResponsesSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment fragment = ConfirmationDialogFragment.newInstance(R.string.confirm_delete_responses_dialog_title, R.string.confirm_delete_responses_dialog_text, new ConfirmationDialogFragment.ConfirmDialogListener() {
					@Override
					public void confirm() {
						AnswerList<Question> questions = MainActivity.databaseManager.getPitQuestions();
						questions.addAll(MainActivity.databaseManager.getMatchQuestions());
						for (Question question : questions) {
							question.resetResponses();
						}
						MainActivity.databaseManager.saveResponses(questions);
						Toast.makeText(that, "Responses Successfully Deleted", Toast.LENGTH_LONG).show();
					}
				});
				fragment.show(getFragmentManager(), "Question Responses Delete");
			}
		});

		SettingsView setTeamNumber = (SettingsView) findViewById(R.id.set_team_number);
		setTeamNumber.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SetTeamNumberDialogFragment teamNumberDialogFragment = SetTeamNumberDialogFragment.newInstance(null);
				teamNumberDialogFragment.show(getFragmentManager(), "Set Team Number");
			}
		});


		SettingsView saveDataSetting = (SettingsView) findViewById(R.id.export_responses);
		saveDataSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				exportResponses();
			}
		});
	}

	private void exportResponses() {
		String fileName = "responses.csv";

		String csv = generateMatchResponsesCSV();
		File file = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), fileName);
		try {
			FileOutputStream outputStream = new FileOutputStream(file);
			outputStream.write(csv.getBytes());
			outputStream.close();
			Toast.makeText(this, "CSV in downloads folder on phone", Toast.LENGTH_LONG).show();
		} catch (java.io.IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Failed to save CSV", Toast.LENGTH_LONG).show();
		}
	}

	private String generateMatchResponsesCSV() {
		AnswerList<Question> matchQuestions = MainActivity.databaseManager.getMatchQuestions();

		List<Map<String, Object>> matches = MainActivity.databaseManager.getCurrentEventMatches();
//		int responseSize = 0;
		String csv = "";
		for (Question question : matchQuestions) {
			csv += "\"" + question.getLabel() + "\",";
		}
		csv = csv.substring(0, csv.length() - 1);
		csv += "\n";

		int matchCount = matches.size();
		for (int i = 0; i < matchCount; i++) {
			int matchNumber = i + 1;
			Map<String, Object> map = matches.get(i);
			List<String> teams = (List<String>) map.get(DatabaseManager.event_match_blue_team);
			teams.addAll((List<String>) map.get(DatabaseManager.event_match_red_team));
			for (String team : teams) {
				int teamNumber = Integer.valueOf(team.substring(3));
				for (Question question : matchQuestions) {
					AnswerList<Response> responses = question.getResponses();
					AnswerList<Response> teamMatchResponse = responses.getTeamAnswers(teamNumber).getMatchAnswers(matchNumber);
					if (teamMatchResponse.size() > 0) {
						csv += "\"" + teamMatchResponse.get(0).getValue() + "\",";
					} else
						csv += "\"\",";
				}
				csv = csv.substring(0, csv.length() - 1);
				csv += "\n";
			}
		}
		return csv;
	}
}
