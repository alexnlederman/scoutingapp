package com.example.vanguard.bluetooth.server;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.vanguard.bluetooth.BluetoothManager;

/**
 * Created by mbent on 7/20/2017.
 */

public class ServerBroadcastReceiver extends BroadcastReceiver {

	BluetoothAdapter bluetoothAdapter;
	ProgressDialog progressDialog;
	Context context;
	boolean deviceFound = false;


	public ServerBroadcastReceiver(BluetoothAdapter bluetoothAdapter, Context context) {
		this.bluetoothAdapter = bluetoothAdapter;
		this.context = context;
	}

	public void reset() {
		this.deviceFound = false;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
			this.progressDialog = new ProgressDialog(this.context);
			this.progressDialog.setMessage("Searching");
			this.progressDialog.show();
		} else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

			if (BluetoothManager.getBluetoothDeviceName(context).equals(device.getName())) {
				this.deviceFound = true;
				ConnectAsyncTask connectAsyncTask = new ConnectAsyncTask(device, this.bluetoothAdapter, context, this.progressDialog);
				connectAsyncTask.execute();
			}
		}
		if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action) && !this.deviceFound) {
			this.progressDialog.dismiss();
			Toast.makeText(this.context, "Failed To Find Device", Toast.LENGTH_LONG).show();
		}
	}
}
