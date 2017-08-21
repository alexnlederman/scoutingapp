package com.example.vanguard.graphs.markers;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.vanguard.graphs.Graph;
import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.pages.fragments.dialog_fragments.ConfirmationDialogFragment;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.example.vanguard.R;
import com.example.vanguard.responses.Response;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.List;

/**
 * Created by mbent on 7/28/2017.
 */

public class SingleResponseGraphMarkerView<T extends Chart & Graph> extends AbstractMarkerView {

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
		super(graph, context, R.layout.marker_single_response);
		this.graph = graph;
		this.context = context;

		graph.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				boolean handled = true;

				// if there is no marker view or drawing marker is disabled
				if (graph.getGraphDetails().isShowingMarker() && graph.getMarker() instanceof SingleResponseGraphMarkerView && event.getAction() == MotionEvent.ACTION_UP) {
					SingleResponseGraphMarkerView markerView = (SingleResponseGraphMarkerView) graph.getMarker();
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
				});
				fragment.show(context.getFragmentManager(), "Event Selector");
			}
		});
		deleteButton.setFocusableInTouchMode(true);
		super.refreshContent(e, highlight);
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