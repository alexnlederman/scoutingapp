package com.example.vanguard.pages.fragments;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.vanguard.bluetooth.AcceptThread;
import com.example.vanguard.bluetooth.BluetoothManager;
import com.example.vanguard.bluetooth.ConnectThread;
import com.example.vanguard.custom_ui_elements.SwitchOption;
import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.pages.activities.NavDrawerFragment;
import com.example.vanguard.R;

import java.util.Set;

/**
 * Created by mbent on 7/20/2017.
 */

public class BluetoothDataTransferFragment extends Fragment implements NavDrawerFragment {

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



		((MainActivity) getActivity()).setupToolbar(MainActivity.ToolbarStyles.STANDARD);

		getActivity().setTitle(R.string.bluetooth_data_transfer_page_title);

		ImageButton button = (ImageButton) getActivity().findViewById(R.id.sync_button);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
				if (bluetoothAdapter == null) {
					Toast.makeText(getActivity(), "Bluetooth Not Supported", Toast.LENGTH_LONG).show();
				} else {
					Log.d("BPressed", BluetoothManager.getServerAddress(getActivity()));
					if (isServer()) {
						AcceptThread acceptThread = new AcceptThread(getActivity(), bluetoothAdapter);
						acceptThread.start();
					} else {
						Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
						Log.d("Paired Devices", pairedDevices.toString());
						for (BluetoothDevice bluetoothDevice : pairedDevices) {
							Log.d("Device Name", bluetoothDevice.getName());
							Log.d("Device Name", bluetoothDevice.getAddress());
							if (bluetoothDevice.getAddress().equals(BluetoothManager.getServerAddress(getActivity()))) {
								ConnectThread connectThread = new ConnectThread(getActivity(), bluetoothAdapter, bluetoothDevice);
								connectThread.start();
							}
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

	private boolean isServer() {
		return BluetoothManager.isServer(getActivity());
	}
}
