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

import java.util.Locale;

/**
 * Created by mbent on 7/3/2017.
 */

public class LineGraph extends LineChart implements Graph {

	AnswerList<? extends Question> questions;

	public LineGraph(Activity context, AnswerList<? extends Question> questions, int teamNumber) {
		super(context);
		this.questions = questions;

		GraphManager.setupLineGraph(this, questions, teamNumber, context);
	}

	@Override
	public Graph.GraphTypes getGraphType() {
		return GraphTypes.LINE_GRAPH;
	}

	@Override
	public boolean isAllTeamGraph() {
		return false;
	}

	@Override
	public AnswerList<? extends Question> getGraphQuestions() {
		return questions;
	}

	public boolean isShowingMarker() {
		return mMarker != null && isDrawMarkersEnabled() && valuesToHighlight();
	}
}
