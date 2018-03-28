package com.example.vanguard.pages.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.vanguard.bluetooth.AcceptThread;
import com.example.vanguard.bluetooth.BluetoothManager;
import com.example.vanguard.bluetooth.ConnectThread;
import com.example.vanguard.bluetooth.server.ServerBroadcastReceiver;
import com.example.vanguard.custom_ui_elements.SettingsView;
import com.example.vanguard.pages.fragments.dialog_fragments.AddEventDialogFragment;
import com.example.vanguard.pages.fragments.dialog_fragments.ConfirmationDialogFragment;
import com.example.vanguard.pages.fragments.dialog_fragments.SetTeamNumberDialogFragment;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.example.vanguard.R;
import com.example.vanguard.questions.question_types.CubeDeliveryQuestion;
import com.example.vanguard.responses.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class SettingsActivity extends AbstractActivity {

	Activity that;

	private final int DISCOVERABLE_DURATION = 60;
	BluetoothAdapter bluetoothAdapter;
	ServerBroadcastReceiver receiver;


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


		SettingsView saveMatchDataSetting = (SettingsView) findViewById(R.id.export_match_responses);
		saveMatchDataSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				exportMatchResponses();
			}
		});

		SettingsView savePitDataSetting = (SettingsView) findViewById(R.id.export_pit_responses);
		savePitDataSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				exportPitResponses();
			}
		});

		SettingsView setupBluetoothSetting = (SettingsView) findViewById(R.id.setup_bluetooth);

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		IntentFilter bluetoothDeviceFilter = new IntentFilter();
		bluetoothDeviceFilter.addAction(BluetoothDevice.ACTION_FOUND);
		bluetoothDeviceFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		bluetoothDeviceFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		receiver = new ServerBroadcastReceiver(bluetoothAdapter, this);
		this.registerReceiver(receiver, bluetoothDeviceFilter);

		setupBluetoothSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (bluetoothAdapter == null) {
					Toast.makeText(that, "Bluetooth Not Supported On Device", Toast.LENGTH_LONG).show();
				} else {
					if (bluetoothAdapter.isEnabled()) {
						setupPairing();
					} else {
						Toast.makeText(that, "Bluetooth Not Enabled", Toast.LENGTH_LONG).show();
					}
				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DISCOVERABLE_DURATION && resultCode == DISCOVERABLE_DURATION) {
			BluetoothManager.setBluetoothDeviceName(this, this.bluetoothAdapter);
		}
	}


	private void setupPairing() {
		if (!BluetoothManager.isServer(this)) {
			this.receiver.reset();
			this.bluetoothAdapter.startDiscovery();
		} else {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
			startActivityForResult(discoverableIntent, DISCOVERABLE_DURATION);
			AcceptThread acceptThread = new AcceptThread(this, bluetoothAdapter);
			acceptThread.start();
		}
	}

	private void exportMatchResponses() {
		String fileName = "match_responses.csv";

		String csv = generateMatchResponsesCSV();

		saveCSV(fileName, csv);
	}

	private void exportPitResponses() {
		String fileName = "pit_responses.csv";

		String csv = generatePitResponsesCSV();

		saveCSV(fileName, csv);
	}

	private void saveCSV(String fileName, String csv) {
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

		String csv = "Event,Match Type";
		for (Question question : matchQuestions) {
			if (question instanceof CubeDeliveryQuestion) {
				if (question.getResponses().size() > 0) {
					Set<String> keys = ((Map<String, Number>)((CubeDeliveryQuestion) question).getResponses().get(0).getValue()).keySet();
					for (String key : keys) {
						csv += "," + key;
					}
				} else {
					csv += "," + question.getLabel();
				}
			} else {
				csv += "," + question.getLabel();
			}
		}

		List<Integer> eventTeams = MainActivity.databaseManager.getCurrentEventTeams();
		for (Integer team : eventTeams) {
			HashMap<Integer, String> matchToCSV = new HashMap<>();
			for (Question question : matchQuestions) {
				AnswerList<Response<?>> responses = question.getResponses().getTeamAnswers(team);
				for (Response<?> response : responses) {
					String currentCSV = matchToCSV.get(response.getMatchNumber());
					String responseCSVValue = "";
					if (question instanceof CubeDeliveryQuestion) {
						Collection<Number> values = ((Map<String, Number>) response.getValue()).values();
						Log.d("Values", String.valueOf(values));
						for (Object value : values) {
							responseCSVValue += "," + value;
						}
						Log.d("Values", responseCSVValue);
					} else {
						responseCSVValue = "," + response.getValue();
					}
					if (currentCSV != null) {
						matchToCSV.put(response.getMatchNumber(), currentCSV + responseCSVValue);
					} else {
						if (response.isPracticeMatchResponse()) {
							matchToCSV.put(response.getMatchNumber(), response.getEventKey() + "," + "Playoff Match" + responseCSVValue);
						} else {
							matchToCSV.put(response.getMatchNumber(), response.getEventKey() + "," + "Qual Match" + responseCSVValue);
						}
					}
				}
			}
			for (String matchCSV : matchToCSV.values()) {
				csv += "\n" + matchCSV;
			}
		}

		Log.d("CSV", csv);
		return csv;
	}

	private String generatePitResponsesCSV() {
		AnswerList<Question> matchQuestions = MainActivity.databaseManager.getPitQuestions();

		String csv = "";
		for (Question question : matchQuestions) {
			csv += "," + question.getLabel();
		}
		csv = csv.substring(1) + "\n";

		List<Integer> eventTeams = MainActivity.databaseManager.getCurrentEventTeams();

		for (Integer team : eventTeams) {
			for (Question question : matchQuestions) {
				AnswerList<Response> teamResponse = question.getResponses().getTeamAnswers(team);
				if (teamResponse.size() == 0) {
					csv += ",";
				} else {
					csv +=  teamResponse.get(0).getValue() + ",";
				}
			}
			csv = csv.substring(0, csv.length() - 1) + "\n";
		}
		Log.d("Pit csv", csv);
		return csv;
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(receiver);
		super.onDestroy();
	}
}
