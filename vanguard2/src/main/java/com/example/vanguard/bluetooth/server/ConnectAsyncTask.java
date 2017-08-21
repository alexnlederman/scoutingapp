package com.example.vanguard.bluetooth.server;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;

import com.example.vanguard.bluetooth.BluetoothManager;
import com.example.vanguard.bluetooth.BluetoothTransfer;

import java.io.IOException;

/**
 * Created by mbent on 7/21/2017.
 */

public class ConnectAsyncTask extends AsyncTask<Void, Boolean, Void> {

	private final BluetoothDevice device;
	private BluetoothSocket socket;
	private final BluetoothAdapter bluetoothAdapter;
	private final Context context;
	private ProgressDialog progressDialog;

	public ConnectAsyncTask(BluetoothDevice device, BluetoothAdapter bluetoothAdapter, Context context, ProgressDialog progressDialog) {
		this.device = device;
		this.bluetoothAdapter = bluetoothAdapter;
		this.context = context;
		this.progressDialog = progressDialog;

		try {
			this.socket = device.createRfcommSocketToServiceRecord(BluetoothManager.getUuid(context));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPreExecute() {
		this.progressDialog.setMessage("Connecting");
	}

	@Override
	protected Void doInBackground(Void[] params) {
		this.bluetoothAdapter.cancelDiscovery();
		try {
			this.socket.connect();
		} catch (IOException e) {
			try {
				this.socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return null;
		}

		publishProgress(true);

		new BluetoothTransfer(socket, this.context, this.bluetoothAdapter, this.progressDialog);

		return null;
	}

	@Override
	protected void onProgressUpdate(Boolean... values) {
		this.progressDialog.setMessage("Transferring Data");
	}
}
