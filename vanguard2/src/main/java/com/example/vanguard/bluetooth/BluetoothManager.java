package com.example.vanguard.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.os.ParcelUuid;
import android.util.Log;

import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.TeamNumberManager;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.example.vanguard.responses.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by mbent on 8/1/2017.
 */

public class BluetoothManager {

	private final static String baseUuid = "633108fc-3602-4f62-98d6-5d1b7425940";
	private final static String STANDARD_DEVICE_NAME_PREF = "STANDARD_DEVICE_NAME_PREF";
	private final static String IS_SERVER_PREF = "IS_SERVER_PREF";
	private final static String ADDRESS_PREF = "ADDRESS_PREF";



	public static UUID getUuid() {
		return UUID.fromString(baseUuid);
	}

	public static String getBluetoothDeviceName(Context context) {
		int teamNumber = TeamNumberManager.getTeamNumber(context);
		return "frc" + teamNumber;
	}

	public static void setBluetoothDeviceName(Context context, BluetoothAdapter adapter) {
		String prevName = adapter.getName();
		if (!prevName.equals(getBluetoothDeviceName(context))) {

			SharedPreferences.Editor editor = context.getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
			editor.putString(STANDARD_DEVICE_NAME_PREF, prevName);
			editor.apply();

			adapter.setName(getBluetoothDeviceName(context));
		}
	}

	public static void resetBluetoothDeviceName(Context context, BluetoothAdapter adapter) {
		SharedPreferences preferences = context.getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		String deviceName = preferences.getString(STANDARD_DEVICE_NAME_PREF, "Default Name");
		adapter.setName(deviceName);
	}

	public static byte[] serializeObject(Serializable obj) throws IOException {
		try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
			try (ObjectOutputStream o = new ObjectOutputStream(b)) {
				o.writeObject(obj);
			}
			return b.toByteArray();
		}
	}

	public static Object deserializeObject(byte[] bytes) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
			try (ObjectInputStream o = new ObjectInputStream(b)) {
				return o.readObject();
			}
		}
	}

	public static boolean isServer(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		return preferences.getBoolean(IS_SERVER_PREF, false);
	}

	public static void setIsServer(Context context, boolean isServer) {
		SharedPreferences.Editor editor = context.getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
		editor.putBoolean(IS_SERVER_PREF, isServer);
		editor.apply();
	}


	public static void saveServerAddress(Context context, String address) {
		Log.d("SERVER ADDRESS", address);
		SharedPreferences.Editor editor = context.getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
		editor.putString(ADDRESS_PREF, address);
		editor.apply();
	}

	public static String getServerAddress(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		return preferences.getString(ADDRESS_PREF, "");
	}

	public static void updateQuestions(Context context, byte[] data) {

		boolean isServer = isServer(context);

		List<Map<String, Object>> questionMaps = null;
		try {
			questionMaps = (List<Map<String, Object>>) deserializeObject(data);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		if (questionMaps != null) {
			Looper.prepare();
			AnswerList<Question> currentQuestions = MainActivity.databaseManager.getAllQuestions();
			List<Integer> teams = MainActivity.databaseManager.getCurrentEventTeams();
			for (Map<String, Object> questionMap : questionMaps) {
				Question transferredQuestion = MainActivity.databaseManager.getQuestionVariableFromMap(questionMap);
				Question correspondingQuestion = currentQuestions.getQuestionById(transferredQuestion.getID());
				currentQuestions.remove(correspondingQuestion);
				if (correspondingQuestion == null && !isServer) {
					// If the question is null and this is not the server.
					MainActivity.databaseManager.createQuestion(transferredQuestion);
				} else if (correspondingQuestion == null) {
					// If the question is null and this is the server.
				} else if (correspondingQuestion.isMatchQuestion()) {
					// If the question is a match question.
					correspondingQuestion.addResponses(transferredQuestion.getResponses());
					MainActivity.databaseManager.saveResponses(transferredQuestion);
				} else if (!isServer) {
					// If the question is not a match question and this is not the server.
					for (int team : teams) {
						AnswerList<Response> correspondingResponses = correspondingQuestion.getTeamResponses(team, false);
						AnswerList<Response> transferredResponses = transferredQuestion.getTeamResponses(team, false);
						if (!correspondingResponses.equals(transferredResponses)) {
							correspondingQuestion.removeResponses(correspondingResponses);
							correspondingQuestion.addResponses(transferredResponses);
						}
					}
					MainActivity.databaseManager.saveResponses(correspondingQuestion);
				} else {
					// If the question is not a match question and this is the server.
					for (int team : teams) {
						AnswerList<Response> correspondingResponses = correspondingQuestion.getTeamResponses(team, false);
						if (correspondingResponses.size() == 0) {
							correspondingQuestion.addResponses(transferredQuestion.getTeamResponses(team, false));
						}
					}
					MainActivity.databaseManager.saveResponses(correspondingQuestion);
				}
			}
			for (Question question : currentQuestions) {
				MainActivity.databaseManager.deleteQuestion(question);
			}
		}
	}
}
