package com.example.vanguard.Pages.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.example.vanguard.Pages.Fragments.AddGraphFragment;
import com.example.vanguard.R;

/**
 * Created by mbent on 7/6/2017.
 */

public class AddGraphActivity extends AppCompatActivity {

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fragment_holder);

		MainActivity.setFragment(new AddGraphFragment(), R.id.fragment_holder_layout, this);
	}
}
