package com.example.vanguard.Bluetooth.Server;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;

import com.example.vanguard.Bluetooth.Client.AcceptAsyncTask;
import com.example.vanguard.Pages.Fragments.BluetoothDataTransferFragment;

import java.io.IOException;

/**
 * Created by mbent on 7/21/2017.
 */

public class ConnectAsyncTask extends AsyncTask<Void, Void, Void> {

	private final BluetoothDevice device;
	private BluetoothSocket socket;
	private final BluetoothAdapter bluetoothAdapter;

	public ConnectAsyncTask(BluetoothDevice device, BluetoothAdapter bluetoothAdapter, Context context) {
		this.device = device;
		this.bluetoothAdapter = bluetoothAdapter;

		try {
			this.socket = device.createRfcommSocketToServiceRecord(BluetoothDataTransferFragment.getUuid(context));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO show progress dialog.

	}

	@Override
	protected Void doInBackground(Void[] params) {
		this.bluetoothAdapter.cancelDiscovery();
		try {
			socket.connect();
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return null;
		}

		System.out.println("IS CONNECTED: " + socket.isConnected());
		// TODO do stuff with connected device.

		return null;
	}

	@Override
	protected void onPostExecute(Void aVoid) {
		// TODO hide progress dialog.
	}
}
