package com.example.vanguard.Graphs;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Pages.Fragments.DialogFragments.ConfirmationDialogFragment;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.R;
import com.example.vanguard.Responses.Response;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BaseDataSet;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.example.vanguard.Graphs.Graph.PRACTICE_MATCH_OPTION;

/**
 * Created by mbent on 7/14/2017.
 */

public class GraphManager {

	private static final int[] colors = {Color.rgb(63, 82, 181), Color.rgb(48, 135, 168), Color.rgb(101, 59, 181)};

	private static void removeYAxisTruncation(BarLineChartBase graph) {
		graph.getAxisLeft().setAxisMinimum(0);
		graph.getAxisRight().setAxisMinimum(0);
	}

	private static List<List<Entry>> getTeamGraphEntries(AnswerList<? extends Question> questions, final int teamNumber) {
		return getGraphListEntries(questions, new GetEntries() {
			@Override
			public List<Entry> getEntries(Question question) {
				return question.getTeamEntryResponseEntries(teamNumber);
			}
		});
	}

	private static void setupDataSetColors(BaseDataSet dataSet, int i) {
		int color;
		if (GraphManager.colors.length > i) {
			color = colors[i];
		} else {
			Random random = new Random();
			color = Color.rgb(random.nextInt(250), random.nextInt(250), random.nextInt(250));
		}
		dataSet.setColors(color);
		if (dataSet instanceof LineDataSet) {
			((LineDataSet) dataSet).setCircleColor(color);
		} else if (dataSet instanceof CandleDataSet) {
			((CandleDataSet) dataSet).setIncreasingColor(color);
			((CandleDataSet) dataSet).setShadowColor(color);
			((CandleDataSet) dataSet).setNeutralColor(color);
		} else if (dataSet instanceof PieDataSet) {
			dataSet.setColors(colors);
		}
	}

	private static List<List<Entry>> getAllTeamGraphEntries(AnswerList<? extends Question> questions, final boolean includePracticeMatches) {
		return getGraphListEntries(questions, new GetEntries() {
			@Override
			public List<Entry> getEntries(Question question) {
				return question.getAllTeamEntryResponseEntries(includePracticeMatches);
			}
		});
	}

	private static List<List<Entry>> getGraphListEntries(AnswerList<? extends Question> questions, GetEntries entryGetter) {
		List<List<Entry>> graphData = new ArrayList<>();
		for (Question question : questions) {
			List<Entry> entries = entryGetter.getEntries(question);
			if (entries.size() > 0) {
				graphData.add(entries);
			}
		}
		return graphData;
	}

	private static void setBarData(BarChart chart, List<List<Entry>> data, AnswerList<? extends Question> questions) {
//		List<IBarDataSet> barDataSets = new ArrayList<>();
		List<List<BarEntry>> barEntries = new ArrayList<>();

		int y = 0;
		for (List<Entry> entries : data) {
			barEntries.add(new ArrayList<BarEntry>());
			for (Entry entry : entries) {
				barEntries.get(y).add(new BarEntry(entry.getX(), entry.getY(), entry.getData()));
			}
			y++;
		}

		List<? extends IBarDataSet> dataSet = getDataSet(barEntries, questions, true, new DataSetGetter<BarDataSet, BarEntry>() {
			@Override
			public BarDataSet getDataSet(List<BarEntry> entries, Question question) {
				return new BarDataSet(entries, question.getQualifiedLabel());
			}
		}, null);

		chart.setData(getGraphData((List<IBarDataSet>) dataSet, new GraphDataGetter<BarData, IBarDataSet>() {
			@Override
			public BarData getGraphData(List<IBarDataSet> dataSet) {
				return new BarData(dataSet);
			}
		}));

//		for (int i = 0; i < data.size(); i++) {
//			BarDataSet dataSet = new BarDataSet(barEntries.get(i), questions.get(i).getQualifiedLabel());
//			setupDataSetColors(dataSet, i);
//			dataSet.setValueTextSize(10);
//			dataSet.setValueFormatter(new IValueFormatter() {
//
//				DecimalFormat format = new DecimalFormat("#.#");
//
//				@Override
//				public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//					return Float.valueOf(format.format(value)).toString().replaceAll("\\.?0*$", "");
//				}
//			});
//			barDataSets.add(dataSet);
//		}

//		if (barDataSets.size() > 0) {
//			BarData barData = new BarData(barDataSets);
//			chart.setData(barData);
//		}
	}

	private static void setLineData(LineChart chart, List<List<Entry>> data, AnswerList<? extends Question> questions) {

		List<? extends ILineDataSet> lineDataSets = getDataSet(data, questions, true, new DataSetGetter<LineDataSet, Entry>() {
			@Override
			public LineDataSet getDataSet(List<Entry> entries, Question question) {
				LineDataSet lineDataSet = new LineDataSet(entries, question.getQualifiedLabel());
				lineDataSet.setLineWidth(5);
				return lineDataSet;
			}
		}, chart);

//		List<ILineDataSet> lineDataSets = new ArrayList<>();
//		for (int i = 0; i < data.size(); i++) {
//			LineDataSet dataSet = new LineDataSet(data.get(i), questions.get(i).getQualifiedLabel());
//			setupDataSetColors(dataSet, i);
//
//			dataSet.setValueTextSize(10);
//			dataSet.setLineWidth(5);
//			addPercentileLimitLines(chart, questions.get(i), dataSet);
//			lineDataSets.add(dataSet);
//		}

		chart.setData(getGraphData((List<ILineDataSet>) lineDataSets, new GraphDataGetter<LineData, ILineDataSet>() {
			@Override
			public LineData getGraphData(List<ILineDataSet> dataSet) {
				return new LineData(dataSet);
			}
		}));
//		if (lineDataSets.size() > 0) {
//			LineData lineData = new LineData(lineDataSets);
//			chart.setData(lineData);
//		}
	}

	private static void setScatterData(ScatterGraph graph, AnswerList<? extends Question> questions) {
		List<Entry> entries = new ArrayList<>();

		List<Float> xValues = questions.get(0).getAllTeamNumberValues();
		List<Float> yValues = questions.get(1).getAllTeamNumberValues();
//		AnswerList<Question> firstQuestion = new AnswerList<>();
//		firstQuestion.add(questions.get(0));
		List<List<Entry>> teamEntries = getAllTeamGraphEntries(questions, graph.getGraphDetails().getOptions().get(PRACTICE_MATCH_OPTION));

		for (int i = 0; i < xValues.size(); i++) {
			entries.add(new Entry(yValues.get(i), xValues.get(i), teamEntries.get(0).get(i).getData()));
		}

		Collections.sort(entries, new EntryXAndYComparator());


		List<List<Entry>> data = new ArrayList<>();
		data.add(entries);

		graph.getLegend().setEnabled(false);

		List<? extends IScatterDataSet> scatterDataSets = getDataSet(data, questions, false, new DataSetGetter<ScatterDataSet, Entry>() {
			@Override
			public ScatterDataSet getDataSet(List<Entry> entries, Question question) {
				ScatterDataSet dataSet = new ScatterDataSet(entries, "");
//				dataSet.setDrawValues(false);
				dataSet.setValueFormatter(new IValueFormatter() {
					@Override
					public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
						return entry.getData().toString();
					}
				});
				return dataSet;
			}
		}, null);

		ScatterData scatterData = getGraphData((List<IScatterDataSet>) scatterDataSets, new GraphDataGetter<ScatterData, IScatterDataSet>() {
			@Override
			public ScatterData getGraphData(List<IScatterDataSet> dataSet) {
				return new ScatterData(dataSet);
			}
		});

		graph.setData(scatterData);

		graph.getXAxis().setAxisMinimum(-0.1f);
		graph.getXAxis().setAxisMaximum(graph.getXAxis().getAxisMaximum() + 0.1f);
	}

	private static void setCandleStickData(CandleStickGraph candleStickGraph, AnswerList<? extends Question> questions) {
		Question question = questions.get(0);
		final List<Integer> teams = question.getEventTeams();

		List<CandleEntry> candleEntries = new ArrayList<>();

		int i = 0;
		for (Integer team : teams) {
			List<Float> teamValues = question.getTeamResponseFloatValues(team, candleStickGraph.getGraphDetails().getOptions().get(PRACTICE_MATCH_OPTION));
			Collections.sort(teamValues);
			Float[] percentiles = getFloatPercentiles(teamValues);
			CandleEntry entry;
			if (teamValues.size() == 0) {
				entry = new CandleEntry(i, 0, 0, 0, 0, team);
			} else {
				entry = new CandleEntry(i, teamValues.get(teamValues.size() - 1), teamValues.get(0), percentiles[0], percentiles[2], team);
			}
			candleEntries.add(entry);
			i++;
		}

		List<List<CandleEntry>> data = new ArrayList<>();
		data.add(candleEntries);

		List<? extends ICandleDataSet> dataSets = getDataSet(data, questions, false, new DataSetGetter<CandleDataSet, CandleEntry>() {
			@Override
			public CandleDataSet getDataSet(List<CandleEntry> entries, Question question) {
				CandleDataSet dataSet = new CandleDataSet(entries, question.getQualifiedLabel());
				dataSet.setDecreasingPaintStyle(Paint.Style.FILL);
				dataSet.setIncreasingPaintStyle(Paint.Style.FILL_AND_STROKE);
				dataSet.setDrawValues(false);
				return dataSet;
			}
		}, null);

		candleStickGraph.setData(getGraphData((List<ICandleDataSet>) dataSets, new GraphDataGetter<CandleData, ICandleDataSet>() {
			@Override
			public CandleData getGraphData(List<ICandleDataSet> dataSet) {
				return new CandleData(dataSet);
			}
		}));

	}

	private static void setPieGraphData(PieGraph pieGraph, AnswerList<? extends Question> questions, int teamNumber, boolean includePracticeMatches) {

		Question question = questions.get(0);
		List responseValues = question.getTeamResponseValues(teamNumber, includePracticeMatches);

		List<PieEntry> entries = convertValuesToPieEntries(responseValues);

		List<IPieDataSet> dataSets = getPieDataSet(entries, questions);

		setupPieGraphData(pieGraph, dataSets);

	}

	private static List<PieEntry> convertValuesToPieEntries(List values) {
		List<PieEntry> entries = new ArrayList<>();
		int totalSize = values.size();

		while (values.size() > 0) {
			String value = values.get(0).toString();
			int count = Collections.frequency(values, value);
			while (values.remove(value)) ;
			System.out.println("Ratio: " + (float) count / totalSize);
			entries.add(new PieEntry((float) count / totalSize, value));
		}

		return entries;
	}

	private static List<IPieDataSet> getPieDataSet(List<PieEntry> entries, AnswerList<? extends Question> questions) {
		List<List<PieEntry>> data = new ArrayList<>();
		data.add(entries);
		List<? extends IPieDataSet> dataSet = getDataSet(data, questions, false, new DataSetGetter<PieDataSet, PieEntry>() {
			@Override
			public PieDataSet getDataSet(List<PieEntry> entries, Question question) {
				PieDataSet pieDataSet = new PieDataSet(entries, question.getQualifiedLabel());
				pieDataSet.setValueFormatter(new IValueFormatter() {
					@Override
					public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
						return String.format(Locale.US, "%.1f%%", value * 100);
					}
				});
				return pieDataSet;
			}
		}, null);

		return (List<IPieDataSet>) dataSet;
	}

	private static void setupPieGraphData(PieGraph graph, List<IPieDataSet> dataSet) {
		graph.setData(getGraphData(dataSet, new GraphDataGetter<PieData, IPieDataSet>() {
			@Override
			public PieData getGraphData(List<IPieDataSet> dataSet) {
				return new PieData(dataSet.get(0));
			}
		}));
	}

	private static void setupPieGraphVisuals(PieGraph pieGraph, AnswerList<? extends Question> questions) {
		Question question = questions.get(0);
		pieGraph.setCenterText(question.getLabel());
		pieGraph.setCenterTextRadiusPercent(80f);
	}

	private static void setPieGraphData(PieGraph pieGraph, AnswerList<? extends Question> questions, boolean includePracticeMatches) {
		Question question = questions.get(0);
		List responseValues = question.getAllTeamResponseValues(includePracticeMatches);

		List<PieEntry> entries = convertValuesToPieEntries(responseValues);

		List<IPieDataSet> dataSet = getPieDataSet(entries, questions);

		setupPieGraphData(pieGraph, dataSet);
	}

	private static <T extends ChartData, E extends IDataSet> T getGraphData(List<E> dataSet, GraphDataGetter<T, E> graphDataGetter) {
		if (dataSet.size() > 0) {
			return graphDataGetter.getGraphData(dataSet);
		}
		return null;
	}

	public static <T extends BaseDataSet, E extends Entry> List<T> getDataSet(List<List<E>> data, AnswerList<? extends Question> questions, boolean formatValue, DataSetGetter<T, E> dataSetGetter, @Nullable BarLineChartBase chart) {
		List<T> dataSets = new ArrayList<>();
		for (int i = 0; i < data.size(); i++) {
			T dataSet = dataSetGetter.getDataSet(data.get(i), questions.get(i));
			setupDataSetColors(dataSet, i);
			dataSet.setValueTextSize(10);
			if (formatValue) {
				dataSet.setValueFormatter(new IValueFormatter() {

					DecimalFormat format = new DecimalFormat("#.#");

					@Override
					public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
						return Float.valueOf(format.format(value)).toString().replaceAll("\\.?0*$", "");
					}
				});
			}
			if (chart != null) {
				addPercentileLimitLines(chart, questions.get(i), dataSet);
			}
			dataSets.add(dataSet);
		}
		return dataSets;
	}

	private static void setGraphLayoutParams(Chart graph) {
		// TODO make this number better than hard coded.
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Math.round(300 * MainActivity.dpToPixels));
		graph.setLayoutParams(params);
	}

	private static void addPercentileLimitLines(BarLineChartBase graph, Question question, BaseDataSet dataSet) {

		Float[] percentiles = getFloatPercentiles(question.getAllTeamNumberValues());

		LimitLine percentile1 = new LimitLine(percentiles[0], "25th Percentile");
		LimitLine median = new LimitLine(percentiles[1], "Median");
		LimitLine percentile2 = new LimitLine(percentiles[2], "75th Percentile");

		int color = dataSet.getColor();
		percentile1.setLineColor(color);
		percentile2.setLineColor(color);
		median.setLineColor(color);

		YAxis axis = graph.getAxisLeft();

		axis.addLimitLine(percentile1);
		axis.addLimitLine(median);
		axis.addLimitLine(percentile2);
		graph.getAxisLeft().setDrawLimitLinesBehindData(true);
	}

	private static Float[] getFloatPercentiles(List<Float> values) {
		Collections.sort(values);
		Float[] percentiles = new Float[3];
		if (values.size() == 0) {
			percentiles[0] = 0f;
			percentiles[1] = 0f;
			percentiles[2] = 0f;
			return percentiles;
		} else if (values.size() % 2 == 0) {
			percentiles[1] = values.get(values.size() / 2);
		} else if (values.size() == 1) {
			percentiles[0] = values.get(0);
			percentiles[1] = values.get(0);
			percentiles[2] = values.get(0);
			return percentiles;
		} else {
			float upperValue = values.get((int) Math.ceil((double) values.size() / 2));
			float lowerValue = values.get((int) Math.floor((double) values.size() / 2));
			float average = Math.round((upperValue + lowerValue) / 2);

			percentiles[1] = average;
		}
		if (values.size() % 4 == 0) {
			percentiles[0] = values.get(values.size() / 4);
			percentiles[2] = values.get(3 * values.size() / 4);
		} else if (values.size() < 4) {
			percentiles[0] = values.get(0);
			percentiles[2] = values.get(values.size() - 1);
		} else {
			float upper25Value = values.get((int) Math.ceil((double) values.size() / 4));
			float lower25Value = values.get((int) Math.floor((double) values.size() / 4));
			float average25 = (upper25Value + lower25Value) / 2;
			percentiles[0] = average25;
			float upper75Value = values.get((int) Math.ceil(((double) 3 * values.size()) / 4));
			float lower75Value = values.get((int) Math.floor(((double) 3 * values.size()) / 4));
			float average75 = (upper75Value + lower75Value) / 2;
			percentiles[2] = average75;
		}
		return percentiles;
	}

	private static void addPracticeMatchDividerLine(Chart graph) {
		if (graph.getXChartMin() <= 0) {
			LimitLine limitLine = new LimitLine(0, "Practice Match End");
			graph.getXAxis().addLimitLine(limitLine);
		}
	}

	private static void disableRightAxis(BarLineChartBase graph) {
		graph.getAxisRight().setDrawGridLines(false);
		graph.getAxisRight().setDrawLabels(false);
	}

	private static List<String> getQuestionListQualifiedNames(AnswerList<? extends Question> questions) {
		List<String> names = new ArrayList<>();
		for (Question question : questions) {
			names.add(question.getQualifiedLabel());
		}
		return names;
	}

	private static void setDescription(Chart graph, String description) {
		graph.getDescription().setText(description);
	}

	private static void setupDataMarker(Chart graph, MarkerView marker) {
		graph.setMarker(marker);
	}

	private static void hideXAxis(Chart graph) {
		graph.getXAxis().setDrawLabels(false);
	}

	public static void setupLineGraph(LineGraph lineGraph, int teamNumber, Activity context) {
		removeYAxisTruncation(lineGraph);
		setLineData(lineGraph, getTeamGraphEntries(lineGraph.getGraphDetails().getGraphQuestions(), teamNumber), lineGraph.getGraphDetails().getGraphQuestions());
		setDescription(lineGraph, "Team " + teamNumber);
		setGraphLayoutParams(lineGraph);
		disableRightAxis(lineGraph);
		addPracticeMatchDividerLine(lineGraph);
		setupDataMarker(lineGraph, new SingleResponseGraphMarkerView<>(context, lineGraph));
	}

	public static void setupBarGraph(BarGraph barGraph, Context context) {
		System.out.println("Include Practice Matches: " + barGraph.getGraphDetails().getOptions().get(PRACTICE_MATCH_OPTION));
		removeYAxisTruncation(barGraph);
		setBarData(barGraph, getAllTeamGraphEntries(barGraph.getGraphDetails().getGraphQuestions(), barGraph.getGraphDetails().getOptions().get(PRACTICE_MATCH_OPTION)), barGraph.getGraphDetails().getGraphQuestions());
		setGraphLayoutParams(barGraph);
		setDescription(barGraph, "");
		disableRightAxis(barGraph);
		hideXAxis(barGraph);
		setupDataMarker(barGraph, new AllTeamGraphMarkerView(context));
	}

	public static void setupScatterGraph(ScatterGraph scatterGraph) {
		removeYAxisTruncation(scatterGraph);
		setScatterData(scatterGraph, scatterGraph.getGraphDetails().getGraphQuestions());
		setGraphLayoutParams(scatterGraph);
		setDescription(scatterGraph, "");
	}

	public static void setupCandleStickGraph(CandleStickGraph candleStickGraph, Context context) {
		removeYAxisTruncation(candleStickGraph);
		setCandleStickData(candleStickGraph, candleStickGraph.getGraphDetails().getGraphQuestions());
		setGraphLayoutParams(candleStickGraph);
		setDescription(candleStickGraph, "");
		hideXAxis(candleStickGraph);
		setupDataMarker(candleStickGraph, new AllTeamGraphMarkerView(context));
	}

	public static void setupPieGraph(PieGraph pieGraph, int teamNumber) {

		if (pieGraph.getGraphDetails().isAllTeamGraph()) {
			setPieGraphData(pieGraph, pieGraph.getGraphDetails().getGraphQuestions(), pieGraph.getGraphDetails().getOptions().get(PRACTICE_MATCH_OPTION));
		} else {
			setPieGraphData(pieGraph, pieGraph.getGraphDetails().getGraphQuestions(), teamNumber, pieGraph.getGraphDetails().getOptions().get(PRACTICE_MATCH_OPTION));
		}

		setGraphLayoutParams(pieGraph);

		setupPieGraphVisuals(pieGraph, pieGraph.getGraphDetails().getGraphQuestions());
		setDescription(pieGraph, "");
	}

	private interface GetEntries {
		List getEntries(Question question);
	}

	private interface GraphDataGetter<T extends ChartData, E extends IDataSet> {
		T getGraphData(List<E> dataSet);
	}

	private interface DataSetGetter<T extends BaseDataSet, E extends Entry> {
		T getDataSet(List<E> entries, Question question);
	}

	private static class AllTeamGraphMarkerView extends MarkerView {

		/**
		 * Constructor. Sets up the MarkerView with a custom layout resource.
		 *
		 * @param context
		 */
		public AllTeamGraphMarkerView(Context context) {
			super(context, R.layout.marker_all_team_graph);
		}

		@Override
		public void refreshContent(Entry e, Highlight highlight) {
			TextView teamNumber = (TextView) this.findViewById(R.id.team_number);
			String teamText = String.format(Locale.US, "Team %d", (Integer) e.getData());
			teamNumber.setText(teamText);
			super.refreshContent(e, highlight);
		}

		@Override
		public MPPointF getOffset() {
			MPPointF mOffset = new MPPointF(-(getWidth() / 2), 0);

			return mOffset;
		}
	}

	public static class SingleResponseGraphMarkerView<T extends Chart & Graph> extends MarkerView {

		T graph;
		Activity context;
		private Rect deleteButtonRect;
		private float drawingPosX;
		private float drawingPosY;

		/**
		 * Constructor. Sets up the MarkerView with a custom layout resource.
		 *
		 * @param context The context
		 */
		public SingleResponseGraphMarkerView(Activity context, final T graph) {
			super(context, R.layout.marker_single_response);
			this.graph = graph;
			this.context = context;

			graph.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					boolean handled = true;

					// if there is no marker view or drawing marker is disabled
					if (graph.getGraphDetails().isShowingMarker() && graph.getMarker() instanceof GraphManager.SingleResponseGraphMarkerView && event.getAction() == MotionEvent.ACTION_UP) {
						GraphManager.SingleResponseGraphMarkerView markerView = (GraphManager.SingleResponseGraphMarkerView) graph.getMarker();
						Rect rect = markerView.getDeleteButtonRect();
						if (rect.contains((int) event.getX(), (int) event.getY())) {
							getButton().performClick();
						} else {
							handled = graph.onTouchEvent(event);
						}
					} else {
						handled = graph.onTouchEvent(event);
					}
					return handled;
				}
			});
		}

		private Button getButton() {
			return (Button) findViewById(R.id.delete_data_point_button);
		}

		@Override
		public void refreshContent(final Entry e, Highlight highlight) {
			Button deleteButton = (Button) findViewById(R.id.delete_data_point_button);
			deleteButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DialogFragment fragment = ConfirmationDialogFragment.newInstance(R.string.confirm_delete_data_point_title, R.string.confirm_delete_data_point_text, new ConfirmationDialogFragment.ConfirmDialogListener() {
						@Override
						public void confirm() {
							Response response = (Response) e.getData();
							List<? extends IDataSet> dataSets = graph.getData().getDataSets();

							for (int i = 0; i < dataSets.size(); i++) {
								if (dataSets.get(i).removeEntry(e)) {
									if (dataSets.get(i).getEntryCount() == 0) {
										if (graph.getData().getDataSets().size() == 1) {
											graph.setData(null);
										} else {
											graph.getData().removeDataSet(dataSets.get(i));
										}
									}
									graph.getGraphDetails().getGraphQuestions().get(i).deleteResponse(response);
									AnswerList<Question> question = new AnswerList<>();
									question.add(graph.getGraphDetails().getGraphQuestions().get(i));
									MainActivity.databaseManager.saveResponses(question);
									graph.invalidate();
								}
							}
						}

						@Override
						public int describeContents() {
							return 0;
						}

						@Override
						public void writeToParcel(Parcel dest, int flags) {

						}
					});
					fragment.show(context.getFragmentManager(), "Event Selector");
				}
			});
			deleteButton.setFocusableInTouchMode(true);
			super.refreshContent(e, highlight);
		}

		@Override
		public MPPointF getOffset() {
			MPPointF mOffset = new MPPointF(-(getWidth() / 2), 0);

			return mOffset;
		}

		public Rect getDeleteButtonRect() {
			Button button = getButton();

			int left = (int) this.drawingPosX + button.getLeft() + button.getPaddingLeft();
			int top = (int) this.drawingPosY + button.getTop() + button.getPaddingTop();
			int right = left + button.getWidth() - button.getPaddingRight();
			int bottom = top + button.getHeight() - button.getPaddingBottom();

			this.deleteButtonRect = new Rect(left, top, right, bottom);
			return deleteButtonRect;
		}

		@Override
		public void draw(Canvas canvas, float posX, float posY) {
			super.draw(canvas, posX, posY);
			MPPointF offset = getOffsetForDrawingAtPoint(posX, posY);
			this.drawingPosX = posX + offset.x;
			this.drawingPosY = posY + offset.y;
		}
	}
}
