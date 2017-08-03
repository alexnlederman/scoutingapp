package com.example.vanguard;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.vanguard.Pages.Activities.MainActivity;

import java.util.Locale;

/**
 * Created by mbent on 7/20/2017.
 */

public class TeamNumberManager {

	private static String TEAM_NUMBER_PREF = "TEAM_NUMBER";

	public static int getTeamNumber(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		int teamNumber = preferences.getInt(TEAM_NUMBER_PREF, 0);
		return teamNumber;
	}

	public static void setTeamNumber(int teamNumber, Context context) {
		SharedPreferences.Editor editor = context.getSharedPreferences(MainActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
		editor.putInt(TEAM_NUMBER_PREF, teamNumber);
		editor.apply();
	}

	public static String getFiveCharacterTeamNumber(Context context) {
		int teamNumber = getTeamNumber(context);
		String teamNumberString = String.format(Locale.US, "%05d", teamNumber);
		return teamNumberString;
	}
}
