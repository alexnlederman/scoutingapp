package com.example.vanguard.Pages.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.vanguard.Questions.QuestionViewers.QuestionListViewers.QuestionListFormViewer;
import com.example.vanguard.R;

public class MatchFormActivity extends AbstractActivity {


	public static final String TEAM_NUMBER = "team_number";
	public static final String QUAL_NUMBER = "match_number";

	int teamNumber;
	int qualNumber;
	boolean isPracticeMatch = false;

	public MatchFormActivity() {
		super(R.layout.activity_match_form, R.string.match_scouting_page_title);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		this.teamNumber = intent.getIntExtra(TEAM_NUMBER, 0);
		this.qualNumber = intent.getIntExtra(QUAL_NUMBER, 0);

		if (this.teamNumber == 0 && this.qualNumber == 0)
			isPracticeMatch = true;

		System.out.println("Team Number: " + this.teamNumber);
		System.out.println("Qual Number: " + this.qualNumber);
		System.out.println("Practice Match: " + isPracticeMatch);

		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.linear_layout);
		QuestionListFormViewer formViewer;
		System.out.println("P Match: " + isPracticeMatch);
		if (isPracticeMatch)
			formViewer = new QuestionListFormViewer(this);
		else
			formViewer = new QuestionListFormViewer(this, teamNumber, qualNumber);
		mainLayout.addView(formViewer);
	}
}
