package com.example.vanguard.graphs.markers;

import android.content.Context;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * Created by mbent on 7/28/2017.
 */

public class AbstractMarkerView extends MarkerView {

	Chart graph;

	public AbstractMarkerView(Chart graph, Context context, int layout) {
		super(context, layout);
		this.graph = graph;
	}

	@Override
	public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {

		final float originalX = posX;
		final float originalY = posY;

		posX += -this.getWidth() / 2;

		if (posX + this.getWidth() > this.graph.getWidth()) {
			posX = this.graph.getWidth() - this.getWidth();
		} else if (posX < 0) {
			posX = 0;
		}

		if (posY + this.getHeight() > this.graph.getHeight()) {
			posY = graph.getHeight() - this.getHeight();
		}
		return new MPPointF(posX - originalX, posY - originalY);
	}

//	@Override
//	public void draw(Canvas canvas, float posX, float posY) {
//
//		posX += -this.getWidth() / 2;
//
//		if (posX + this.getWidth() > this.graph.getWidth()) {
//			posX = this.graph.getWidth() - this.getWidth();
//		} else if (posX < 0) {
//			posX = 0;
//		}
//
//		if (posY + this.getHeight() > this.graph.getHeight()) {
//			posY = graph.getHeight() - this.getHeight();
//		}
//
//		// translate to the correct position and draw
//		canvas.translate(posX, posY);
//		draw(canvas);
//		canvas.translate(-posX, -posX);
//	}

}
