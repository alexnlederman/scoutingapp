package com.example.vanguard.graphs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.pages.activities.MainActivity;

import java.util.List;

/**
 * Created by mbent on 7/6/2017.
 */

public class GraphEditor extends LinearLayout {

	public GraphEditor(Context context) {
		super(context);

		setOrientation(VERTICAL);

		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		this.setLayoutParams(params);
	}

	private void updateGraphs() {

		this.removeAllViews();

		List<GraphDescriber> graphDescriberList = MainActivity.databaseManager.getGraphDescribers();

		if (graphDescriberList.size() == 0)
			this.setVisibility(GONE);
		else
			this.setVisibility(VISIBLE);

		for (GraphDescriber describer : graphDescriberList) {
			this.addView(describer);
		}
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		if (visibility == View.VISIBLE)
			this.updateGraphs();
	}
}
