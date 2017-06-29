package com.example.vanguard;

import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEventSource;

/**
 * Created by mbent on 6/29/2017.
 */

public interface AnswerUI<T> {

	T getValue();

	void setValue(T value);

}
