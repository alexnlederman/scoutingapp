package com.example.vanguard.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by mbent on 3/12/2018.
 */

public class AcceptThread extends Thread {

	private final BluetoothServerSocket serverSocket;
	private final BluetoothAdapter bluetoothAdapter;
	private final Activity context;

	public AcceptThread(Activity context, BluetoothAdapter bluetoothAdapter) {
		Log.d("Accept Thread", "Accept");

		BluetoothServerSocket tmp = null;
		this.context = context;
		this.bluetoothAdapter = bluetoothAdapter;
		try {
			tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("Application", BluetoothManager.getUuid());
		} catch (IOException e) {
			e.printStackTrace();
		}
		serverSocket = tmp;
	}

	@Override
	public void run() {
		BluetoothSocket socket = null;

		while (true) {
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d("Socket", socket.getRemoteDevice().getName());
			if (socket != null) {
				new BluetoothTransfer(socket, context, bluetoothAdapter);
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	@Override
	public void interrupt() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.interrupt();
	}
}
