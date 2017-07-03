package com.example.vanguard.Pages.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.vanguard.DatabaseManager;
import com.example.vanguard.Pages.Fragments.ScoutingFragment;
import com.example.vanguard.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


	public static float dpToPixels;
	public static DatabaseManager databaseManager;
	Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

		MainActivity.dpToPixels = this.getResources().getDisplayMetrics().density;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


		this.databaseManager = new DatabaseManager(getApplicationContext());
		System.out.println("Started");
//		this.databaseManager.addEvent("2016nytr");
	}

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.empty_menu, menu);
		this.menu = menu;
		setView(new ScoutingFragment());
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
            // Handle the camera action
        } else if (id == R.id.nav_schedule) {
        } else if (id == R.id.nav_scout) {
			setView(new ScoutingFragment());
        } else if (id == R.id.nav_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);

        } else if (id == R.id.nav_sync) {

        }
        return true;
    }

    private void setView(Fragment fragment) {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.fragment_holder, fragment).commit();

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
	}

	@Override
	public void setTitle(CharSequence title) {
		TextView titleTextView = (TextView) findViewById(R.id.title);
		titleTextView.setText(title);
	}
}
