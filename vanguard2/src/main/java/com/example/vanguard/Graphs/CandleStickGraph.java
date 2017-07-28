package com.example.vanguard.Graphs;

import android.content.Context;

import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;

import java.util.Map;

/**
 * Created by mbent on 7/25/2017.
 */

public class CandleStickGraph extends CandleStickChart implements Graph {

	GraphDetails details;

	public CandleStickGraph(Context context, AnswerList<? extends Question> questions, Map<String, Boolean> options) {
		super(context);

		this.details = new GraphDetails(this, GraphTypes.CANDLE_STICK_GRAPH, questions, new String[]{PRACTICE_MATCH_OPTION}, options, true);

		GraphManager.setupCandleStickGraph(this, context);
	}

	@Override
	public GraphDetails getGraphDetails() {
		return this.details;
	}
}
