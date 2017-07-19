package com.example.vanguard.Pages.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.R;

/**
 * Created by mbent on 7/7/2017.
 */

public class TeamGraphFragment extends Fragment {
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_fragment_holder, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		MainActivity.setFragmentSave(new GraphTeamSelectorFragment(), R.id.fragment_fragment_holders, getActivity(), false);
	}
}
