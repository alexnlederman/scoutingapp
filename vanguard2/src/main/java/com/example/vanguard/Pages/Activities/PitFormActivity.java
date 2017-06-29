package com.example.vanguard.Pages.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.example.vanguard.PitScoutSelector;
import com.example.vanguard.Questions.QuestionViewers.QuestionListViewers.QuestionListFormViewer;
import com.example.vanguard.R;

import static com.example.vanguard.Pages.Activities.MatchFormActivity.TEAM_NUMBER;

public class PitFormActivity extends AppCompatActivity {

	int teamNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pit_form);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		Intent intent = getIntent();
		this.teamNumber = intent.getIntExtra(TEAM_NUMBER, 0);
		LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout);
		layout.addView(new QuestionListFormViewer(this, teamNumber));
	}

	@Override
	public boolean onSupportNavigateUp() {
		System.out.println("Back");
		onBackPressed();
		return true;
	}
}
