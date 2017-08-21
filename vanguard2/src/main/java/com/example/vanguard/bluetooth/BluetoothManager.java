package com.example.vanguard.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.TeamNumberManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by mbent on 8/1/2017.
 */

public class BluetoothManager {

	private final static String baseUuid = "633108fc-3602-4f62-98d6-5d1b742";
	private final static String STANDARD_DEVICE_NAME_PREF = "STANDARD_DEVICE_NAME_PREF";
	private final static String IS_SERVER_PREF = "IS_SERVER_PREF";


	public static UUID getUuid(Context context) {
		String completeUUID = baseUuid + TeamNumberManager.getFiveCharacterTeamNumber(context);
		return UUID.fromString(completeUUID);
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
}
