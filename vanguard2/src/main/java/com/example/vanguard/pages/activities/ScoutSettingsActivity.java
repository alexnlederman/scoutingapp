package com.example.vanguard.pages.activities;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vanguard.R;
import com.example.vanguard.pages.fragments.ScoutSettingsFragment;

import static com.example.vanguard.questions.question_viewers.question_list_viewers.edit_list_viewer.QuestionTouchCallback.QUESTION_EDITED;

/**
 * Created by mbent on 7/3/2017.
 */

public class ScoutSettingsActivity extends AbstractActivity {

	public final static int QUESTION_ADDED = 4;
	FragmentStatePagerAdapter adapter;
	ViewPager viewPager;
	Activity context;

	public ScoutSettingsActivity() {
		super(R.layout.activity_scout_settings, R.string.scout_setting_page_title);
		this.context = this;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		viewPager = (ViewPager) findViewById(R.id.view_pager);
		adapter = new ScoutSettingsPageAdapter(getFragmentManager());
		viewPager.setAdapter(adapter);
		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
		tabLayout.setupWithViewPager(viewPager);
	}

	private void setupMenu(Menu menu) {
		MenuItem addStringQuestion = menu.add("Add Question");
		addStringQuestion.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		addStringQuestion.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(context, AddQuestionActivity.class);
				intent.putExtra(AddQuestionActivity.IS_MATCH_QUESTION, getCurrentFragment().isMatchForm());
				context.startActivityForResult(intent, QUESTION_ADDED);
				return false;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == QUESTION_ADDED) {
			if (resultCode == QUESTION_ADDED) {
				this.getCurrentFragment().getAdapter().addQuestion();
			}
		}
		if (requestCode == QUESTION_EDITED) {
			this.getCurrentFragment().getAdapter().updateLabels();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setupMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}

	private ScoutSettingsFragment getCurrentFragment() {
		return ((ScoutSettingsFragment) (adapter.instantiateItem(viewPager, viewPager.getCurrentItem())));
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
}
