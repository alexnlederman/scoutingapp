package com.example.vanguard.Pages.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.vanguard.R;

/**
 * Created by mbent on 7/13/2017.
 */

public abstract class AbstractActivity extends AppCompatActivity {

	int contentView;
	int title;

	public AbstractActivity(int contentView, int title) {
		this.contentView = contentView;
		this.title = title;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(this.contentView);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		this.setTitle(this.title);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public boolean onSupportNavigateUp() {
		System.out.println("Back");
		this.onBackPressed();
		return true;
	}
}
