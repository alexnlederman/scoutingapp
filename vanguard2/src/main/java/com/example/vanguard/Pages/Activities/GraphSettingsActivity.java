package com.example.vanguard.Pages.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.vanguard.DatabaseManager;
import com.example.vanguard.Graphs.GraphEditor;
import com.example.vanguard.Graphs.LineGraph;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionViewers.QuestionListViewers.QuestionListFormViewer;
import com.example.vanguard.R;

/**
 * Created by mbent on 7/6/2017.
 */

public class GraphSettingsActivity extends AppCompatActivity {

	private final int ADD_GRAPH_MENU_ITEM = 1;
	private final Context context = this;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_form);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);


		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.linear_layout);

		mainLayout.addView(new GraphEditor(this));


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add(Menu.NONE, ADD_GRAPH_MENU_ITEM, Menu.NONE, "Add Graph");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case ADD_GRAPH_MENU_ITEM:
				Intent intent = new Intent(this, AddGraphActivity.class);
				startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onSupportNavigateUp() {
		System.out.println("Back");
		onBackPressed();
		return true;
	}
}
