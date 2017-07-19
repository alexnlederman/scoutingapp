package com.example.vanguard.Graphs;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Parcel;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by mbent on 7/14/2017.
 */

public class GraphManager {

	private static void removeYAxisTruncation(BarLineChartBase graph) {
		graph.getAxisLeft().setAxisMinimum(0);
	}

	private static List<List<Entry>> getTeamGraphEntries(AnswerList<? extends Question> questions, final int teamNumber) {
		return getGraphListEntries(questions, new GetEntries() {
			@Override
			public List<Entry> getEntries(Question question) {
				return question.getTeamEntryResponseValues(teamNumber);
			}
		});
	}

	private static void setLineData(LineChart chart, List<List<Entry>> data, AnswerList<? extends Question> questions, boolean addPercentileLines) {
		List<ILineDataSet> lineDataSets = new ArrayList<>();
		for (int i = 0; i < data.size(); i++) {
			LineDataSet dataSet = new LineDataSet(data.get(i), questions.get(i).getQualifiedLabel());
			dataSet.setValueTextSize(10);
			dataSet.setLineWidth(5);
			if (addPercentileLines)
				addPercentileLimitLines(chart, questions.get(i), dataSet);
			lineDataSets.add(dataSet);
		}
		if (lineDataSets.size() > 0) {
			LineData lineData = new LineData(lineDataSets);
			chart.setData(lineData);
		}
	}

	private static List<List<Entry>> getAllTeamGraphEntries(AnswerList<? extends Question> questions) {
		return getGraphListEntries(questions, new GetEntries() {
			@Override
			public List<Entry> getEntries(Question question) {
				return question.getAllTeamEntryResponseValues();
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

	private interface GetEntries {
		List<Entry> getEntries(Question question);
	}

	private static void setBarData(BarChart chart, List<List<Entry>> data, AnswerList<? extends Question> questions, boolean addPercentileLines) {
		List<IBarDataSet> barDataSets = new ArrayList<>();
		List<List<BarEntry>> barEntries = new ArrayList<>();

		int y = 0;
		for (List<Entry> entries : data) {
			barEntries.add(new ArrayList<BarEntry>());
			for (Entry entry : entries) {
				barEntries.get(y).add(new BarEntry(entry.getX(), entry.getY(), entry.getData()));
			}
			y++;
		}

		for (int i = 0; i < data.size(); i++) {
			BarDataSet dataSet = new BarDataSet(barEntries.get(i), questions.get(i).getQualifiedLabel());
			dataSet.setValueTextSize(10);
			dataSet.setValueFormatter(new IValueFormatter() {

				DecimalFormat format = new DecimalFormat("#.#");

				@Override
				public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
					return new Float(format.format(value)).toString().replaceAll("\\.?0*$", "");
				}
			});
			if (addPercentileLines)
				addPercentileLimitLines(chart, questions.get(i), dataSet);
			barDataSets.add(dataSet);
		}

		if (barDataSets.size() > 0) {
			BarData barData = new BarData(barDataSets);
			chart.setData(barData);
		}
	}

	private static void setGraphLayoutParams(Chart graph) {
		// TODO make this number better than hard coded. Should also be in dp.
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Math.round(300 * MainActivity.dpToPixels));
		graph.setLayoutParams(params);
	}

	private static void addPercentileLimitLines(BarLineChartBase graph, Question question, BaseDataSet dataSet) {

		Float[] percentiles = getFloatPercentiles(question.getAllTeamNumberValues());

		LimitLine percentile1 = new LimitLine(percentiles[0], "25th Percentile");
		LimitLine median = new LimitLine(percentiles[1], "Median");
		LimitLine percentile2 = new LimitLine(percentiles[2], "75th Percentile");


		if (dataSet.getYMax() < percentiles[2]) {
			graph.getAxisLeft().setAxisMaximum(percentiles[2] * (1 + (graph.getAxisLeft().getSpaceTop() / 100)));
		}

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
		if (values.size() % 2 == 0) {
			percentiles[1] = values.get(values.size() / 2);
		} else {
			float upperValue = values.get((int) Math.ceil((double) values.size() / 2));
			float lowerValue = values.get((int) Math.floor((double) values.size() / 2));
			float average = (upperValue + lowerValue) / 2;
			percentiles[1] = values.get((int) average);
		}
		if (values.size() % 4 == 0) {
			percentiles[0] = values.get(values.size() / 4);
			percentiles[2] = values.get(3 * values.size() / 4);
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
			String teamText = String.format(Locale.US, "Team %d", ((AnswerList<Response>) e.getData()).get(0).getTeamNumber());
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
					if (graph.isShowingMarker() && graph.getMarker() instanceof GraphManager.SingleResponseGraphMarkerView && event.getAction() == MotionEvent.ACTION_UP) {
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
					DialogFragment fragment = ConfirmationDialogFragment.newInstance(R.layout.dialog_confirm_delete_response, new ConfirmationDialogFragment.ConfirmDialogListener() {
						@Override
						public void confirm() {
							Response response = (Response) e.getData();
							List<? extends IDataSet> dataSets = graph.getData().getDataSets();

							for (int i = 0; i < dataSets.size(); i++) {
								if (dataSets.get(i).removeEntry(e)) {
									if (dataSets.get(i).getEntryCount() == 0) {
										// TODO test what happens when all the responses are deleted for one question in a graph that has more than one question.
										graph.setData(null);
									}
									graph.getGraphQuestions().get(i).deleteResponse(response);
									AnswerList<Question> question = new AnswerList<>();
									question.add(graph.getGraphQuestions().get(i));
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

		private Rect deleteButtonRect;

		public Rect getDeleteButtonRect() {
			Button button = getButton();

			int left = (int) this.drawingPosX + button.getLeft() + button.getPaddingLeft();
			int top = (int) this.drawingPosY + button.getTop() + button.getPaddingTop();
			int right = left + button.getWidth() - button.getPaddingRight();
			int bottom = top + button.getHeight() - button.getPaddingBottom();

			this.deleteButtonRect = new Rect(left, top, right, bottom);
			return deleteButtonRect;
		}

		private float drawingPosX;
		private float drawingPosY;

		@Override
		public void draw(Canvas canvas, float posX, float posY) {
			super.draw(canvas, posX, posY);
			MPPointF offset = getOffsetForDrawingAtPoint(posX, posY);
			this.drawingPosX = posX + offset.x;
			this.drawingPosY = posY + offset.y;
		}
	}

	public static void setupLineGraph(LineGraph lineGraph, AnswerList<? extends Question> questions, int teamNumber, Activity context) {
		removeYAxisTruncation(lineGraph);
		setLineData(lineGraph, getTeamGraphEntries(questions, teamNumber), questions, true);
		setDescription(lineGraph, "Team " + teamNumber);
		setGraphLayoutParams(lineGraph);
		disableRightAxis(lineGraph);
		addPracticeMatchDividerLine(lineGraph);
		setupDataMarker(lineGraph, new SingleResponseGraphMarkerView<>(context, lineGraph));
	}

	public static void setupBarGraph(BarGraph barGraph, AnswerList<? extends Question> questions, Context context) {
		removeYAxisTruncation(barGraph);
		setBarData(barGraph, getAllTeamGraphEntries(questions), questions, false);
		setGraphLayoutParams(barGraph);
		setDescription(barGraph, "");
		disableRightAxis(barGraph);
		hideXAxis(barGraph);
		setupDataMarker(barGraph, new AllTeamGraphMarkerView(context));
	}
}
