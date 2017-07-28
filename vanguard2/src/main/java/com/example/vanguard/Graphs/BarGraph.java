package com.example.vanguard.Graphs;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.R;
import com.example.vanguard.Responses.Response;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mbent on 7/7/2017.
 */

public class BarGraph extends BarChart implements Graph{

	GraphDetails details;

	public BarGraph(Context context, AnswerList<? extends Question> questions, Map<String, Boolean> options) {
		super(context);

		this.details = new GraphDetails(this, GraphTypes.BAR_GRAPH, questions, new String[]{PRACTICE_MATCH_OPTION}, options, true);

		GraphManager.setupBarGraph(this, context);
	}

	@Override
	public GraphDetails getGraphDetails() {
		return this.details;
	}
}
