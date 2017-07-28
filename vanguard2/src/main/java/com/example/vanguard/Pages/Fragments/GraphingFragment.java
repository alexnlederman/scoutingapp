package com.example.vanguard.Pages.Fragments;

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

import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Pages.Activities.NavDrawerFragment;
import com.example.vanguard.R;

/**
 * Created by mbent on 7/5/2017.
 */

public class GraphingFragment extends Fragment implements NavDrawerFragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_view_pager, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		((MainActivity) getActivity()).setupToolbar(MainActivity.ToolbarStyles.TABBED);
		ViewPager viewPager = (ViewPager) getView().findViewById(R.id.view_pager);
		viewPager.setAdapter(new GraphPagerAdapter(getChildFragmentManager()));
		TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
		tabLayout.setupWithViewPager(viewPager);
		getActivity().setTitle(R.string.graph_page_title);
	}

	@Override
	public int getNavDrawerPosition() {
		return R.id.nav_data;
	}

//	@Override
//	public void onResume() {
//		super.onResume();
//		ViewPager viewPager = (ViewPager) getView().findViewById(R.id.view_pager);
//		viewPager.setAdapter(new GraphPagerAdapter(getFragmentManager()));
//		TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
//		tabLayout.setupWithViewPager(viewPager);
//	}

	private class GraphPagerAdapter extends FragmentStatePagerAdapter {

		public GraphPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0:
					return new AllTeamGraphFragment();
				case 1:
					return new TeamGraphFragment();
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
					return "All Team Graphs";
				case 1:
					return "Individual Team Graphs";
			}
			return "Error: " + position;
		}
	}
}
