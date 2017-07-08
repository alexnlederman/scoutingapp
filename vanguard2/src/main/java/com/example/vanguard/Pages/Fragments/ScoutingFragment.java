package com.example.vanguard.Pages.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanguard.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class ScoutingFragment extends Fragment {


	public ScoutingFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_view_pager, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		System.out.println("Scouting View Created");
		ViewPager viewPager = (ViewPager) getView().findViewById(R.id.view_pager);
		viewPager.setAdapter(new ScoutPagerAdapter(getChildFragmentManager()));
		TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
		tabLayout.setupWithViewPager(viewPager);
	}


//	@Override
//	public void onResume() {
//		System.out.println("Scouting Resumed");
//		ViewPager viewPager = (ViewPager) getView().findViewById(R.id.view_pager);
//		viewPager.setAdapter(new ScoutPagerAdapter(getChildFragmentManager()));
//		TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
//		tabLayout.setupWithViewPager(viewPager);
//		super.onResume();
//	}

	private class ScoutPagerAdapter extends FragmentStatePagerAdapter {

		public ScoutPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			System.out.println("Position : " + position);
			switch (position) {
				case 0:
					return new MatchScoutSelectorFragment();
				case 1:
					return new PitScoutFragment();
			}
			return null;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
				case 0:
					return "Match Scouting";
				case 1:
					return "Pit Scouting";
			}
			return "Error: " + position;
		}
	}
}
