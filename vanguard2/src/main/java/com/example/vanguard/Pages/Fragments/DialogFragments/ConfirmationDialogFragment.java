package com.example.vanguard.Pages.Fragments.DialogFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.example.vanguard.R;

/**
 * Created by mbent on 7/18/2017.
 */

public class ConfirmationDialogFragment extends DialogFragment {

	public static ConfirmationDialogFragment newInstance(int layout, ConfirmDialogListener confirmListener) {

		Bundle args = new Bundle();

		args.putInt("layout", layout);
		args.putParcelable("listener", confirmListener);

		ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		View v = inflater.inflate(getArguments().getInt("layout"), null);


		builder.setView(v).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((ConfirmDialogListener) getArguments().getParcelable("listener")).confirm();
			}
		}).setNegativeButton("Decline", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		return builder.create();
	}

	public interface ConfirmDialogListener extends Parcelable {
		void confirm();
	}
}
