package com.example.vanguard.graphs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.vanguard.pages.activities.MainActivity;

import java.util.List;

/**
 * Created by mbent on 7/7/2017.
 */

public class GraphViewer extends ListView {

	List<Graph> graphs;
	Context context;

	public GraphViewer(Context context) {
		super(context);
		this.context = context;
		this.graphs = MainActivity.databaseManager.getAllTeamGraphs();
		setupLayout();
	}

	public GraphViewer(Context context, int teamNumber) {
		super(context);
		this.context = context;
		this.graphs = MainActivity.databaseManager.getTeamGraphs(teamNumber);
		setupLayout();
	}

	private void setupLayout() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		this.setLayoutParams(params);
		this.setAdapter(new GraphAdapter(graphs));
	}

	public int getGraphCount() {
		return this.graphs.size();
	}

	public class GraphAdapter extends ArrayAdapter<Graph> {

		List<Graph> graphs;

		public GraphAdapter(List<Graph> graphs) {
			super(context, -1, graphs);
			this.graphs = graphs;
		}

		@NonNull
		@Override
		public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
			View graph = (View) graphs.get(position);
			return graph;
		}
	}
}
