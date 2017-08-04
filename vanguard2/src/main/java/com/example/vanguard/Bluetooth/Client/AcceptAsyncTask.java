package com.example.vanguard.Bluetooth.Client;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.example.vanguard.Bluetooth.BluetoothManager;
import com.example.vanguard.Bluetooth.BluetoothTransfer;
import com.example.vanguard.Pages.Fragments.BluetoothDataTransferFragment;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by mbent on 7/21/2017.
 */

public class AcceptAsyncTask extends AsyncTask<Void, Boolean, Void> {

	private BluetoothServerSocket serverSocket;
	private final BluetoothAdapter bluetoothAdapter;
	private final Context context;
	private ProgressDialog progressDialog;

	public AcceptAsyncTask(BluetoothAdapter bluetoothAdapter, Context context) {
		this.bluetoothAdapter = bluetoothAdapter;
		this.context = context;
		try {
			this.serverSocket = this.bluetoothAdapter.listenUsingRfcommWithServiceRecord("Bluetooth", BluetoothManager.getUuid(context));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR ERROR ERROR ERROR");
		}
	}

	@Override
	protected void onPreExecute() {
		this.progressDialog = new ProgressDialog(this.context);
		this.progressDialog.setMessage("Searching");
		this.progressDialog.show();
	}

	@Override
	protected Void doInBackground(Void[] params) {
		BluetoothSocket socket;
		while (true) {
			try {
				socket = this.serverSocket.accept();
				BluetoothManager.resetBluetoothDeviceName(this.context, this.bluetoothAdapter);
			} catch (IOException e) {
				e.printStackTrace();
				BluetoothManager.resetBluetoothDeviceName(this.context, this.bluetoothAdapter);
				break;
			}


			if (socket != null) {
				publishProgress(true);
				new BluetoothTransfer(socket, this.context, this.bluetoothAdapter, this.progressDialog);
				try {
					this.serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Boolean... values) {
		this.progressDialog.setMessage("Transferring Data");
	}
}
