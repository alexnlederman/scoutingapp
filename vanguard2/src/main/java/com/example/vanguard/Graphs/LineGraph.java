package com.example.vanguard.Graphs;

import android.content.Context;

import com.example.vanguard.Pages.Fragments.SetGraphQuestionsFragment;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Responses.Response;
import com.github.mikephil.charting.charts.LineChart;

import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbent on 7/3/2017.
 */

public class LineGraph extends LineChart implements Graph {

	AnswerList<Question> questions;

	public LineGraph(Context context, AnswerList<Question> questions, int teamNumber) {
		super(context);
		this.questions = questions;

		List<ILineDataSet> lineGraphData = new ArrayList<>();
		for (Question question : questions) {
//			List<Entry> entries = new ArrayList<>();
//			AnswerList<Response<Number>> responses = question.getTeamResponses(teamNumber);

//			for (Response<Number> response : responses) {
//				entries.add(new Entry(response.getMatchNumber(), response.getValue().floatValue()));
//			}
			List<Entry> entries = question.getTeamEntryResponseValues(teamNumber);
			if (entries.size() > 0) {
				LineDataSet dataSet = new LineDataSet(entries, question.getLabel());
				dataSet.setLineWidth(4);
				lineGraphData.add(dataSet);
			}
		}
		System.out.println("Data Size: " + lineGraphData.size());
		if (lineGraphData.size() > 0) {
			LineData data = new LineData(lineGraphData);
			this.setData(data);
		}
		else {
			this.setData(null);
		}
		this.getAxisRight().setEnabled(false);
		this.setContentDescription("Test 123");
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
	public AnswerList<Question> getGraphQuestions() {
		return questions;
	}
}
