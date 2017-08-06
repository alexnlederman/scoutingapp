package com.example.vanguard.Pages.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.CustomUIElements.ErrorTextView;
import com.example.vanguard.Graphs.GraphEditor;
import com.example.vanguard.Pages.Activities.AddGraphActivity;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Pages.Activities.NavDrawerFragment;
import com.example.vanguard.R;

/**
 * Created by mbent on 7/6/2017.
 */

public class GraphSettingsFragment extends Fragment implements NavDrawerFragment {

	private final int ADD_GRAPH_MENU_ITEM = 1;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_graph_settings, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		((MainActivity) getActivity()).setupToolbar(MainActivity.ToolbarStyles.STANDARD);

		LinearLayout mainLayout = (LinearLayout) getActivity().findViewById(R.id.linear_layout);

		GraphEditor editor = new GraphEditor(getActivity());

		mainLayout.addView(editor);
		Toolbar toolbar = (Toolbar) this.getActivity().findViewById(R.id.toolbar);
		toolbar.getMenu().add(Menu.NONE, ADD_GRAPH_MENU_ITEM, Menu.NONE, "Add Graph").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(getActivity(), AddGraphActivity.class);
				startActivity(intent);
				return true;
			}
		});
		mainLayout.addView(new ErrorTextView(getActivity(), R.string.add_graph_page_error));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		System.out.println("CLICK");
		int id = item.getItemId();
		switch (id) {
			case ADD_GRAPH_MENU_ITEM:
				Intent intent = new Intent(getActivity(), AddGraphActivity.class);
				startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public int getNavDrawerIconId() {
		return R.id.nav_graph_setting;
	}
}
