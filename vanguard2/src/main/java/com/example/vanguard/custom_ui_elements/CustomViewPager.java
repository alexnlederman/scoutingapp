package com.example.vanguard.custom_ui_elements;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by mbent on 8/31/2017.
 */

public class CustomViewPager extends ViewPager {

	boolean swippable = true;

	
	public CustomViewPager(Context context) {
		super(context);
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setSwippable(boolean swippable) {
		this.swippable = this.swippable;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (this.swippable) {
			return super.onInterceptTouchEvent(ev);
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (this.swippable) {
			return super.onTouchEvent(ev);
		}
		return false;
	}
}
