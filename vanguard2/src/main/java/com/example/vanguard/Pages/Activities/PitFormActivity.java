package com.example.vanguard.Pages.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.vanguard.Questions.QuestionViewers.QuestionListViewers.QuestionListFormViewer;
import com.example.vanguard.R;

import static com.example.vanguard.Pages.Activities.MatchFormActivity.TEAM_NUMBER;

public class PitFormActivity extends AbstractActivity {

	int teamNumber;

	public PitFormActivity() {
		super(R.layout.activity_pit_form, R.string.pit_scouting_page_title);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		this.teamNumber = intent.getIntExtra(TEAM_NUMBER, 0);
		LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout);
		layout.addView(new QuestionListFormViewer(this, teamNumber));
	}
}
