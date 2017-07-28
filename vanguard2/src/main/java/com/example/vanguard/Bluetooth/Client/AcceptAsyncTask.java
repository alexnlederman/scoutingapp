package com.example.vanguard.Bluetooth.Client;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.os.ParcelUuid;
import android.util.Log;

import com.example.vanguard.Pages.Fragments.BluetoothDataTransferFragment;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by mbent on 7/21/2017.
 */

public class AcceptAsyncTask extends AsyncTask<Void, Void, Void> {

	private BluetoothServerSocket serverSocket;
	private final BluetoothAdapter bluetoothAdapter;

	public AcceptAsyncTask(BluetoothAdapter bluetoothAdapter, Context context) {
		this.bluetoothAdapter = bluetoothAdapter;
		try {
			System.out.println("LISTEN");
			this.serverSocket = this.bluetoothAdapter.listenUsingRfcommWithServiceRecord("Bluetooth", BluetoothDataTransferFragment.getUuid(context));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR ERROR ERROR");
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO show progress dialog.
	}

	@Override
	protected Void doInBackground(Void[] params) {
		BluetoothSocket socket;
		while (true) {
			try {
				socket = this.serverSocket.accept();

			} catch (IOException e) {
				e.printStackTrace();
				break;
			}

			if (socket != null) {
				try {
					this.serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				System.out.println("DONE");

				// TODO test if device is connecting properly.
				break;
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void aVoid) {
		//TODO hide progress dialog.
	}
}
