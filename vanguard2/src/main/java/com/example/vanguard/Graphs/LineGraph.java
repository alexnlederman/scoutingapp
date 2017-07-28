package com.example.vanguard.Graphs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.R;
import com.github.mikephil.charting.charts.LineChart;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by mbent on 7/3/2017.
 */

public class LineGraph extends LineChart implements Graph {

	GraphDetails details;


	public LineGraph(Activity context, AnswerList<? extends Question> questions, int teamNumber) {
		super(context);

		this.details = new GraphDetails(this, GraphTypes.LINE_GRAPH, questions, new String[0], new HashMap<String, Boolean>(), false);

		GraphManager.setupLineGraph(this, teamNumber, context);
	}

	@Override
	public GraphDetails getGraphDetails() {
		return this.details;
	}
}
