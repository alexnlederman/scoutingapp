package com.example.vanguard.Pages.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.vanguard.Bluetooth.Client.AcceptAsyncTask;
import com.example.vanguard.Bluetooth.Server.ConnectAsyncTask;
import com.example.vanguard.Bluetooth.Server.ServerBroadcastReceiver;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Pages.Activities.NavDrawerFragment;
import com.example.vanguard.R;
import com.example.vanguard.TeamNumberManager;

import java.util.Set;
import java.util.UUID;

/**
 * Created by mbent on 7/20/2017.
 */

public class BluetoothDataTransferFragment extends Fragment implements NavDrawerFragment {

	private final static int REQUEST_ENABLE_BT = 1;

	private final static String uuid = "633108fc-3602-4f62-98d6-5d1b742";

	BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

//	ServerBroadcastReceiver receiver;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_bluetooth_data_transfer, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

//		IntentFilter bluetoothDeviceFilter = new IntentFilter();
//		bluetoothDeviceFilter.addAction(BluetoothDevice.ACTION_FOUND);
//		bluetoothDeviceFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
//		bluetoothDeviceFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//		bluetoothDeviceFilter.addAction(BluetoothDevice.ACTION_UUID);
//		this.receiver = new ServerBroadcastReceiver(this.bluetoothAdapter);
//		this.getActivity().registerReceiver(receiver, bluetoothDeviceFilter);

		((MainActivity) getActivity()).setupToolbar(MainActivity.ToolbarStyles.STANDARD);

		getActivity().setTitle(R.string.bluetooth_data_transfer_page_title);

		ImageButton button = (ImageButton) getActivity().findViewById(R.id.sync_button);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (bluetoothAdapter == null) {
					Toast.makeText(getActivity(), "Bluetooth Not Supported On Device", Toast.LENGTH_LONG).show();
				} else {
					if (bluetoothAdapter.isEnabled()) {
						setupPairing();
//						Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//						startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
					} else {
						Toast.makeText(getActivity(), "Connect to Bluetooth Device", Toast.LENGTH_LONG).show();
//						setupPairing();
					}
				}
			}
		});
	}

	@Override
	public int getNavDrawerPosition() {
		return R.id.nav_sync;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("CODE: " + requestCode);
		System.out.println("CODE: " + resultCode);
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				setupPairing();
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Toast.makeText(this.getActivity(), "Bluetooth Not Enabled", Toast.LENGTH_LONG).show();
			}
		} else if (requestCode == DISCOVERABLE_DURATION && resultCode == DISCOVERABLE_DURATION) {
			AcceptAsyncTask acceptAsyncTask = new AcceptAsyncTask(this.bluetoothAdapter, getActivity());
			acceptAsyncTask.execute();
		}
	}

	private final int DISCOVERABLE_DURATION = 60;

	private void setupPairing() {
		if (this.isServer()) {
//			if (this.hasLEBluetooth()) {
//				BluetoothLeScanner scanner = this.bluetoothAdapter.getBluetoothLeScanner();
//				settings = new ScanSettings.Builder()
//						.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
//						.build();
//				scanner.startScan(new ServerBroadcastReceiver.LeScanner());
//
//			}
//			else {
			Set<BluetoothDevice> pairedDevices = this.bluetoothAdapter.getBondedDevices();
//
//			if (pairedDevices.size() > 0) {
//				// There are paired devices. Get the name and address of each paired device.
//				for (BluetoothDevice device : pairedDevices) {
//					this.bluetoothAdapter.removeBond
//				}
//			}
//			this.bluetoothAdapter.startDiscovery();
//			}
			BluetoothDevice device = pairedDevices.iterator().next();
			System.out.println("DEVICE: " + device.getName());
			ConnectAsyncTask connectAsyncTask = new ConnectAsyncTask(device, this.bluetoothAdapter, getActivity());
			connectAsyncTask.execute();
		} else {
//			System.out.println("");
//			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
//			startActivityForResult(discoverableIntent, DISCOVERABLE_DURATION);
			AcceptAsyncTask acceptAsyncTask = new AcceptAsyncTask(this.bluetoothAdapter, getActivity());
			acceptAsyncTask.execute();
		}
	}

	private boolean isServer() {
		Switch isServer = (Switch) this.getActivity().findViewById(R.id.is_server_switch);
		return isServer.isChecked();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

//		this.getActivity().unregisterReceiver(this.receiver);
	}

	public static UUID getUuid(Context context) {
		String completeUUID = uuid + TeamNumberManager.getFiveCharacterTeamNumber(context);
		System.out.println("Complete UUID: " + completeUUID);
		return UUID.fromString(completeUUID);
	}
}
