package com.example.vanguard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.vanguard.Pages.Activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mbent on 6/29/2017.
 */
public class AddEventDialogFragment extends DialogFragment {

	Map<String, String> eventMaps;
	Activity context;

	public static AddEventDialogFragment newInstance(DialogOpener dialogOpener) {

		Bundle args = new Bundle();
		args.putParcelable("dialog", dialogOpener);
		AddEventDialogFragment fragment = new AddEventDialogFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		View v = inflater.inflate(R.layout.number_picker_dialog, null);
		final NumberPicker numberPicker = (NumberPicker) v.findViewById(R.id.number_picker);

		builder.setView(v).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("CONFIRMED");
				int year = numberPicker.getValue();
				System.out.println("Year: " + year);
				try {
					URL url = new URL("https://www.thebluealliance.com/api/v3/events/" + year + "/simple?X-TBA-Auth-Key=vVc9R5KHLDG2zkDgqFzRQRAFWBIPSSdyesezDG0m44p5yAiUBAz7qMasclG4Ua7a");
					GetEventsAsync async = new GetEventsAsync();
					async.execute(url);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		});

		numberPicker.setMaxValue(3000);
		numberPicker.setMinValue(1992);
		numberPicker.setValue(Calendar.getInstance().get(Calendar.YEAR));

		return builder.create();
	}

	@Override
	public void onAttach(Activity activity) {
		this.context = activity;
		super.onAttach(activity);
	}

	public class GetEventsAsync extends AsyncTask<URL, Boolean, String> {

		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(getContext());
			progressDialog.setMessage("Loading");
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(URL... params) {
			try {
				return DatabaseManager.getUrlValue(params[0]);
			} catch (SocketTimeoutException | UnknownHostException e) {
				e.printStackTrace();
				System.out.println("Socket Time Out");
				return "NO_INTERNET";
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Invalid Year");
				return "INVALID_YEAR";
			}
		}

		@Override
		protected void onPostExecute(String s) {
			progressDialog.dismiss();
			if (s.equals("INVALID_YEAR")) {
				Toast.makeText(context, "Please Enter A Valid Year", Toast.LENGTH_LONG).show();
				return;
			}
			else if (s.equals("NO_INTERNET")) {
				Toast.makeText(context, "Please Connect To The Internet", Toast.LENGTH_LONG).show();
				return;
			}
			System.out.println(s);
			Map<String, String> eventMapList = new HashMap<>();
			try {
				JSONArray events = new JSONArray(s);
				for (int i = 0; i < events.length(); i++) {
					JSONObject event = events.getJSONObject(i);
					eventMapList.put(event.getString("name"), event.getString("key"));
				}
				eventMaps = eventMapList;

				DialogOpener opener = getArguments().getParcelable("dialog");
				opener.openDialog(new ChooseEventDialogFragment());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			super.onPostExecute(s);
		}
	}

	@SuppressLint("ValidFragment")
	public class ChooseEventDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			LayoutInflater inflater = getActivity().getLayoutInflater();
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			View v = inflater.inflate(R.layout.event_picker_dialog, null);
			final AutoCompleteTextView textView = (AutoCompleteTextView) v.findViewById(R.id.event_name);

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, 	eventMaps.keySet().toArray(new String[eventMaps.keySet().size()]));
			textView.setAdapter(adapter);

			builder.setView(v).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (eventMaps.containsKey(textView.getText().toString())) {
						System.out.println("Success");
						MainActivity.databaseManager.addEvent(eventMaps.get(textView.getText().toString()), textView.getText().toString(), context);
					}
					else Toast.makeText(context, "Please Select An Event From The Dropdown", Toast.LENGTH_LONG).show();
				}
			});


			return builder.create();
		}

		// TODO have this confirm a proper year. And have it add the event. And that they have internet.
	}

	public interface DialogOpener extends Parcelable {
		void openDialog(DialogFragment dialog);
	}
}
