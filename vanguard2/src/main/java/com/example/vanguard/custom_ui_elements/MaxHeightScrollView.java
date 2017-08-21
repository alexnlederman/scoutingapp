package com.example.vanguard.custom_ui_elements;

import android.content.Context;
import android.widget.ScrollView;

/**
 * Created by mbent on 7/28/2017.
 */

public class MaxHeightScrollView extends ScrollView {

	int maxHeight;

	public MaxHeightScrollView(Context context, int maxHeight) {
		super(context);
		this.maxHeight = maxHeight;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(MeasureSpec.getSize(heightMeasureSpec) > this.maxHeight) {
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(this.maxHeight, MeasureSpec.AT_MOST);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
