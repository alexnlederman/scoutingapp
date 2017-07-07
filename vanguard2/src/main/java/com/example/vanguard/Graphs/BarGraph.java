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
import java.util.List;

/**
 * Created by mbent on 7/7/2017.
 */

public class BarGraph extends BarChart implements Graph{

	AnswerList<Question> questions;

	public BarGraph(Context context, AnswerList<Question> questions) {
		super(context);

		this.questions = questions;

		List<IBarDataSet> barGraphData = new ArrayList<>();
		for (Question question : this.questions) {
			List<Entry> entries = question.getAllTeamEntryResponseValues();
			List<BarEntry> barEntries = new ArrayList<>();
			for (Entry entry : entries) {
				System.out.println("X: " + entry.getX());
				System.out.println("Y: " + entry.getY());
				barEntries.add(new BarEntry(entry.getX(), entry.getY(), entry.getData()));
			}

			BarDataSet dataSet = new BarDataSet(barEntries, question.getLabel());
			barGraphData.add(dataSet);
		}
		BarData data = new BarData(barGraphData);
		data.setBarWidth(0.45f);
		this.setData(data);
		this.getXAxis().setDrawGridLines(false);
		this.getXAxis().setDrawLabels(false);

//		this.getXAxis().setValueFormatter(new LabelValueFormatter());
		if (questions.size() > 1)
			this.groupBars(0, 0.06f, 0.02f);
//		this.setFitBars(true);
		this.invalidate();
		this.setMarker(new Marker(context, R.layout.marker_basic_layout));

		ListView.LayoutParams layoutParams = new ListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.setLayoutParams(layoutParams);
	}

	public class Marker extends MarkerView {

		TextView textView;

		public Marker(Context context, int layoutResource) {
			super(context, layoutResource);
			LinearLayout layout = (LinearLayout) this.findViewById(R.id.layout);
			this.textView = new TextView(context);
			this.textView.setTextColor(ContextCompat.getColor(context, R.color.textColor));
			layout.addView(this.textView);
		}

		@Override
		public void refreshContent(Entry e, Highlight highlight) {
			this.textView.setText("Team: " + e.getData());

			super.refreshContent(e, highlight);
		}

		private MPPointF mOffset;

		@Override
		public MPPointF getOffset() {

			if(mOffset == null) {
				// center the marker horizontally and vertically
				mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
			}

			return mOffset;
		}
	}

	@Override
	public GraphTypes getGraphType() {
		return GraphTypes.BAR_GRAPH;
	}

	@Override
	public boolean isAllTeamGraph() {
		return true;
	}

	@Override
	public AnswerList<Question> getGraphQuestions() {
		return this.questions;
	}
}
