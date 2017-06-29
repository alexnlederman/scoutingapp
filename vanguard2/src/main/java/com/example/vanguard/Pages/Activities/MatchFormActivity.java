package com.example.vanguard.Pages.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.Questions.QuestionViewers.QuestionListViewers.QuestionListFormViewer;
import com.example.vanguard.R;

public class MatchFormActivity extends AppCompatActivity {


	public static final String TEAM_NUMBER = "team_number";
	public static final String QUAL_NUMBER = "match_number";

	int teamNumber;
	int qualNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_form);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		Intent intent = getIntent();
		this.teamNumber = intent.getIntExtra(TEAM_NUMBER, 0);
		this.qualNumber = intent.getIntExtra(QUAL_NUMBER, 0);

		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.linear_layout);
		QuestionListFormViewer formViewer = new QuestionListFormViewer(this, teamNumber, qualNumber);
		mainLayout.addView(formViewer);
	}

	@Override
	public boolean onSupportNavigateUp() {
		System.out.println("Back");
		onBackPressed();
		return true;
	}
}
