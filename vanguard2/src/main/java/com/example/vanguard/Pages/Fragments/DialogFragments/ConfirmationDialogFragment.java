package com.example.vanguard.Pages.Fragments.DialogFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.vanguard.R;

/**
 * Created by mbent on 7/18/2017.
 */

public class ConfirmationDialogFragment extends DialogFragment {

	public static ConfirmationDialogFragment newInstance(int title, int text, ConfirmDialogListener confirmListener) {

		Bundle args = new Bundle();

		args.putInt("title", title);
		args.putInt("text", text);
		args.putParcelable("listener", confirmListener);

		ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		View v = inflater.inflate(R.layout.dialog_confirmation, null);

		((TextView) v.findViewById(R.id.dialog_text)).setText(getArguments().getInt("text"));
		((TextView) v.findViewById(R.id.dialog_title)).setText(getArguments().getInt("title"));

		builder.setView(v).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((ConfirmDialogListener) getArguments().getParcelable("listener")).confirm();
			}
		}).setNegativeButton("Decline", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((ConfirmDialogListener) getArguments().getParcelable("listener")).decline();
			}
		});

		return builder.create();
	}

	public static abstract class ConfirmDialogListener implements Parcelable {
		public void confirm() {
		}

		public void decline() {
		}

		@Override
		public final int describeContents() {
			return 0;
		}

		@Override
		public final void writeToParcel(Parcel dest, int flags) {
		}
	}
}
