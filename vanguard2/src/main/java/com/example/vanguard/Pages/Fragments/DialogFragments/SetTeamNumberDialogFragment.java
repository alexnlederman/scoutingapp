package com.example.vanguard.Pages.Fragments.DialogFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.vanguard.R;
import com.example.vanguard.TeamNumberManager;

/**
 * Created by mbent on 7/20/2017.
 */

public class SetTeamNumberDialogFragment extends DialogFragment {

	private SetTeamNumberDialogFragment() {

	}

	public static SetTeamNumberDialogFragment newInstance(@Nullable ConfirmationDialogFragment.ConfirmDialogListener confirmDialogListener) {

		Bundle args = new Bundle();

		args.putParcelable("listener", confirmDialogListener);

		SetTeamNumberDialogFragment fragment = new SetTeamNumberDialogFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		View v = inflater.inflate(R.layout.dialog_set_team_number, null);
		final EditText teamNumber = (EditText) v.findViewById(R.id.team_number_edit_text);

		builder.setView(v).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String text = teamNumber.getText().toString();

				if (text.length() > 0 && !text.equals("-")) {
					TeamNumberManager.setTeamNumber(Integer.valueOf(teamNumber.getText().toString()), getActivity());
					ConfirmationDialogFragment.ConfirmDialogListener confirm = getArguments().getParcelable("listener");
					if (confirm != null) {
						confirm.confirm();
					}
				}
			}
		});

		return builder.create();
	}

}
