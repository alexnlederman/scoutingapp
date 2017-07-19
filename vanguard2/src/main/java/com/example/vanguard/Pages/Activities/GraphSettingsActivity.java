package com.example.vanguard.Pages.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.vanguard.CustomUIElements.ErrorTextView;
import com.example.vanguard.Graphs.GraphEditor;
import com.example.vanguard.R;

/**
 * Created by mbent on 7/6/2017.
 */

public class GraphSettingsActivity extends AbstractActivity {

	private final int ADD_GRAPH_MENU_ITEM = 1;

	public GraphSettingsActivity() {
		super(R.layout.activity_match_form, R.string.graph_settings_page_title);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.linear_layout);

		GraphEditor editor = new GraphEditor(this);

		mainLayout.addView(editor);

		mainLayout.addView(new ErrorTextView(this, R.string.add_graph_page_error));
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
}
