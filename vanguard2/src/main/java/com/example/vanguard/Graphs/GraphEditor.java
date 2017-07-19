package com.example.vanguard.Graphs;

import android.content.Context;
import android.support.annotation.IntDef;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.Pages.Activities.MainActivity;

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
