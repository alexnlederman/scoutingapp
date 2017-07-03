package com.example.vanguard.Pages.Activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;

import com.example.vanguard.Pages.Fragments.ScoutSettingsFragment;
import com.example.vanguard.R;

/**
 * Created by mbent on 7/3/2017.
 */

public class ScoutSettingsActivity extends AppCompatActivity {

	FragmentStatePagerAdapter adapter;
	ViewPager viewPager;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("Set Content View");
		setContentView(R.layout.activity_scout_settings);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		viewPager = (ViewPager) findViewById(R.id.view_pager);
		adapter = new ScoutSettingsPageAdapter(getFragmentManager());
		viewPager.setAdapter(adapter);
		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
		tabLayout.setupWithViewPager(viewPager);
	}

	private void setupMenu(Menu menu) {
		MenuItem addStringQuestion = menu.add("Add String Question");
		addStringQuestion.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		addStringQuestion.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				getCurrentFragment().addStringQuestion();
				return false;
			}
		});

		MenuItem addIntegerQuestion = menu.add("Add Integer Question");
		addIntegerQuestion.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		addIntegerQuestion.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				getCurrentFragment().addIntegerQuestion();
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setupMenu(menu);
		System.out.println("Menu Created");
		return super.onCreateOptionsMenu(menu);
	}

	private class ScoutSettingsPageAdapter extends FragmentStatePagerAdapter {

		public ScoutSettingsPageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return ScoutSettingsFragment.newInstance(position == 0);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
				case 0:
					return "Match Scout Settings";
				case 1:
					return "Pit Scout Settings";
			}
			return null;
		}
	}

	private ScoutSettingsFragment getCurrentFragment() {
		return ((ScoutSettingsFragment) (adapter.instantiateItem(viewPager, viewPager.getCurrentItem())));
	}

	@Override
	public boolean onSupportNavigateUp() {
		System.out.println("Back");
		onBackPressed();
		return true;
	}
}
