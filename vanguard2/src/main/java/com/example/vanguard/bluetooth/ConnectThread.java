package com.example.vanguard.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by mbent on 3/12/2018.
 */

public class ConnectThread extends Thread {

	private final BluetoothDevice device;

	private final BluetoothSocket socket;

	private final BluetoothAdapter bluetoothAdapter;

	Activity context;

	public ConnectThread(Activity context, BluetoothAdapter bluetoothAdapter, BluetoothDevice device) {
		this.device = device;
		this.bluetoothAdapter = bluetoothAdapter;
		BluetoothSocket tmp = null;
		this.context = context;

		try {
			tmp = device.createInsecureRfcommSocketToServiceRecord(BluetoothManager.getUuid());
		} catch (IOException e) {
			e.printStackTrace();
		}
		socket = tmp;
	}

	@Override
	public void run() {
		Looper.prepare();
//		bluetoothAdapter.cancelDiscovery();

		try {
			socket.connect();
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			return;
		}

		BluetoothManager.saveServerAddress(this.context, device.getAddress());
		Toast.makeText(this.context, "Device Found", Toast.LENGTH_LONG).show();

		Log.d("Connect Socket", socket.getRemoteDevice().getName());
		sendData(socket);
	}

	@Override
	public void interrupt() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.interrupt();
	}

	private void sendData(BluetoothSocket socket) {
		new BluetoothTransfer(socket, context, bluetoothAdapter);
	}

}
