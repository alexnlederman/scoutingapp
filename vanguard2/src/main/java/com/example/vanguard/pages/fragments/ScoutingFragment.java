package com.example.vanguard.pages.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanguard.R;
import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.pages.activities.NavDrawerFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class ScoutingFragment extends Fragment implements NavDrawerFragment {


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
		((MainActivity) getActivity()).setupToolbar(MainActivity.ToolbarStyles.TABBED);


		ViewPager viewPager = (ViewPager) getView().findViewById(R.id.view_pager);
		viewPager.setAdapter(new ScoutPagerAdapter(getChildFragmentManager()));
		TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
		tabLayout.setupWithViewPager(viewPager);
		String eventName = MainActivity.databaseManager.getCurrentEventName();
		if (!eventName.equals(""))
			getActivity().setTitle(MainActivity.databaseManager.getCurrentEventName());
		else
			getActivity().setTitle("Scouting");
	}

	@Override
	public int getNavDrawerIconId() {
		return R.id.nav_scout;
	}

	private class ScoutPagerAdapter extends FragmentStatePagerAdapter {

		public ScoutPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
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
