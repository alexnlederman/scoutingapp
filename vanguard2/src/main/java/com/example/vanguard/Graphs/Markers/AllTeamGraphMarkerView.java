package com.example.vanguard.Graphs.Markers;

import android.content.Context;
import android.widget.TextView;

import com.example.vanguard.R;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.Locale;

/**
 * Created by mbent on 7/28/2017.
 */

public class AllTeamGraphMarkerView extends AbstractMarkerView {


	/**
	 * Constructor. Sets up the MarkerView with a custom layout resource.
	 *
	 * @param context
	 */
	public AllTeamGraphMarkerView(Chart graph, Context context) {
		super(graph, context, R.layout.marker_all_team_graph);
	}

	@Override
	public void refreshContent(Entry e, Highlight highlight) {
		TextView teamNumber = (TextView) this.findViewById(R.id.team_number);
		String teamText = String.format(Locale.US, "Team %d", (Integer) e.getData());
		teamNumber.setText(teamText);
		super.refreshContent(e, highlight);
	}
}
