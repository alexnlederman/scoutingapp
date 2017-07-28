package com.example.vanguard.Bluetooth.Server;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.ParcelUuid;
import android.os.Parcelable;

import com.example.vanguard.Pages.Fragments.BluetoothDataTransferFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by mbent on 7/20/2017.
 */

public class ServerBroadcastReceiver extends BroadcastReceiver {

	BluetoothAdapter bluetoothAdapter;
	UUID deviceUUID;

	private List<BluetoothDevice> devices;

	public ServerBroadcastReceiver(BluetoothAdapter bluetoothAdapter, Context context) {
		this.bluetoothAdapter = bluetoothAdapter;
		this.devices = new ArrayList<>();
		this.deviceUUID = BluetoothDataTransferFragment.getUuid(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		System.out.println("ACTION: " + action);
		if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			ParcelUuid uuidExtra = intent.getParcelableExtra(BluetoothDevice.EXTRA_UUID);

			System.out.println("NAME: " + device.getName());

			System.out.println("EXTRA: " + uuidExtra);
			if (uuidExtra != null)
				System.out.println("EXTRA UUID: " + uuidExtra.getUuid());
			ParcelUuid[] uuids = device.getUuids();
			System.out.println("UUID: " + uuids);
			this.devices.add(device);

//			if ("TEST".equals(device.getName())) {
//				ConnectAsyncTask connectAsyncTask = new ConnectAsyncTask(device, this.bluetoothAdapter);
//				connectAsyncTask.execute();
//			}

			if (uuids != null) {
				for (ParcelUuid uuid : uuids) {
					System.out.println("UUID: " + uuid.getUuid());
					if (uuid.getUuid().equals(this.deviceUUID)) {
						// TODO connect.
						System.out.println("CONTAINS UUID");
					}
				}
			}
		}

		else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
			for (BluetoothDevice device : devices) {
				System.out.println("DEVICE: " + device.getName());
				if (device.getName() != null) {
					device.fetchUuidsWithSdp();
				}
			}
		}

		else if (BluetoothDevice.ACTION_UUID.equals(action)) {
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			System.out.println("NAME: " + device.getName());
			for (ParcelUuid uuid : device.getUuids()) {
				System.out.println("ACTION UUID: " + uuid.getUuid());
			}
		}
	}
}
