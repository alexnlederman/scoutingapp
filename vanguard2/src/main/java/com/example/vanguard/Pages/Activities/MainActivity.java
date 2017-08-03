package com.example.vanguard.Pages.Activities;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.vanguard.DatabaseManager;
import com.example.vanguard.Pages.Fragments.BluetoothDataTransferFragment;
import com.example.vanguard.Pages.Fragments.GraphingFragment;
import com.example.vanguard.Pages.Fragments.ScoutingFragment;
import com.example.vanguard.R;
import com.example.vanguard.TeamNumberManager;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {


	public static float dpToPixels;
	public static DatabaseManager databaseManager;
	public static String SHARED_PREF_NAME = "SHARED_PREFS";
	Menu menu;
	DrawerLayout drawer;
	View headerView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MainActivity.dpToPixels = this.getResources().getDisplayMetrics().density;


		this.databaseManager = new DatabaseManager(this);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		this.drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		this.headerView = navigationView.getHeaderView(0);

		getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
			@Override
			public void onBackStackChanged() {
				updateCheckItem(navigationView);
			}
		});

		navigationView.getMenu().getItem(0).setChecked(true);

		setupPermissions();
	}

	private void updateCheckItem(NavigationView navigationView) {
		Fragment f = getFragmentManager().findFragmentById(R.id.fragment_holder);
		if (f instanceof NavDrawerFragment) {
			navigationView.setCheckedItem(((NavDrawerFragment) f).getNavDrawerPosition());
		}
	}

	@Override
	public void onBackPressed() {
		if (this.drawer.isDrawerOpen(GravityCompat.START)) {
			this.drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.empty_menu, menu);
		this.menu = menu;
		setFragmentSave(new ScoutingFragment(), R.id.fragment_holder, this, false);
		getSupportActionBar().setTitle("Scouting");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.

		int id = item.getItemId();

		Fragment fragment = null;

		if (id == R.id.nav_data) {
			setView(new GraphingFragment());
		} else if (id == R.id.nav_schedule) {
		} else if (id == R.id.nav_scout) {
			setView(new ScoutingFragment());
		} else if (id == R.id.nav_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);

		} else if (id == R.id.nav_sync) {
			setView(new BluetoothDataTransferFragment());
		}
		return true;
	}

	private void setView(Fragment fragment) {
		setFragment(fragment, R.id.fragment_holder, this);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
	}

	@Override
	public void setTitle(CharSequence title) {
		TextView titleTextView = (TextView) findViewById(R.id.title);
		titleTextView.setText(title);
	}

	public static void setFragment(Fragment fragment, int fragmentHolder, Activity activity) {
		MainActivity.setFragmentSave(fragment, fragmentHolder, activity, true);

	}

	public static void setFragmentSave(Fragment fragment, int fragmentHolder, Activity activity, boolean saveFragment) {
		FragmentManager fragmentManager = activity.getFragmentManager();
		if (saveFragment)
			fragmentManager.beginTransaction().replace(fragmentHolder, fragment).addToBackStack("fragment" + fragment.toString()).commit();
		else
			fragmentManager.beginTransaction().replace(fragmentHolder, fragment).commit();
	}

	public enum ToolbarStyles {
		TABBED,
		STANDARD
	}

	public void setupToolbar(ToolbarStyles style) {
		TabLayout tabs = (TabLayout) findViewById(R.id.tab_layout);

		switch (style) {
			case TABBED:
				tabs.setVisibility(View.VISIBLE);
				break;
			case STANDARD:
				tabs.setVisibility(View.GONE);
				break;
		}
	}

	// Storage Permissions
	private static final int REQUEST_PERMISSIONS = 3;
	private final String[] PERMISSIONS = {
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.ACCESS_COARSE_LOCATION
	};

	private void setupPermissions() {

		int locationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
		int storagePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (locationPermission != PackageManager.PERMISSION_GRANTED || storagePermission != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, this.PERMISSIONS, REQUEST_PERMISSIONS);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		TextView teamNumber = (TextView) this.headerView.findViewById(R.id.team_number_nav_header_text_view);
		teamNumber.setText(String.valueOf(TeamNumberManager.getTeamNumber(this)));
	}
}
