package com.example.vanguard.pages.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.vanguard.bluetooth.BluetoothManager;
import com.example.vanguard.bluetooth.client.AcceptAsyncTask;
import com.example.vanguard.bluetooth.server.ServerBroadcastReceiver;
import com.example.vanguard.custom_ui_elements.SwitchOption;
import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.pages.activities.NavDrawerFragment;
import com.example.vanguard.R;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by mbent on 7/20/2017.
 */

public class BluetoothDataTransferFragment extends Fragment implements NavDrawerFragment {

	private final static int REQUEST_ENABLE_BT = 1;

	private final int DISCOVERABLE_DURATION = 60;
	BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	ServerBroadcastReceiver receiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_bluetooth_data_transfer, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		SwitchOption isServerSwitch = (SwitchOption) this.getActivity().findViewById(R.id.is_server_switch);
		isServerSwitch.setChecked(BluetoothManager.isServer(getActivity()));
		isServerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				BluetoothManager.setIsServer(getActivity(), isChecked);
			}
		});

		IntentFilter bluetoothDeviceFilter = new IntentFilter();
		bluetoothDeviceFilter.addAction(BluetoothDevice.ACTION_FOUND);
		bluetoothDeviceFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		bluetoothDeviceFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.receiver = new ServerBroadcastReceiver(this.bluetoothAdapter, getActivity());
		this.getActivity().registerReceiver(receiver, bluetoothDeviceFilter);

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
						if (isServer()) {
							Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
							startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
						} else {
							setupPairing();
						}
					}
				}
			}
		});
	}

	@Override
	public int getNavDrawerIconId() {
		return R.id.nav_sync;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				setupPairing();
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Toast.makeText(this.getActivity(), "Bluetooth Not Enabled", Toast.LENGTH_LONG).show();
			}
		} else if (requestCode == DISCOVERABLE_DURATION && resultCode == DISCOVERABLE_DURATION) {
			BluetoothManager.setBluetoothDeviceName(getActivity(), this.bluetoothAdapter);
			AcceptAsyncTask acceptAsyncTask = new AcceptAsyncTask(this.bluetoothAdapter, getActivity());
			acceptAsyncTask.execute();
		}
	}

	private void setupPairing() {
		if (this.isServer()) {
			Set<BluetoothDevice> pairedDevices = this.bluetoothAdapter.getBondedDevices();

//			 There are paired devices. Get the name and address of each paired device.
			for (BluetoothDevice device : pairedDevices) {
				this.unpairDevice(device);
			}
			this.receiver.reset();
			this.bluetoothAdapter.startDiscovery();
		} else {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
			startActivityForResult(discoverableIntent, DISCOVERABLE_DURATION);
		}
	}

	private boolean isServer() {
		return BluetoothManager.isServer(getActivity());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		this.getActivity().unregisterReceiver(this.receiver);
	}

	private void unpairDevice(BluetoothDevice device) {
		try {
			Method m = device.getClass().getMethod("removeBond", (Class[]) null);
			m.invoke(device, (Object[]) null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
