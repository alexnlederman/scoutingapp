package com.example.vanguard.CustomUIElements;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.MainActivity;

/**
 * Created by BertTurtle on 6/5/2017.
 */

public abstract class MaterialLinearLayout extends LinearLayout{

	public MaterialLinearLayout(Context context) {
		super(context);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			this.setElevation(MainActivity.dpToPixels * 2);
		}
		else {
			ViewCompat.setElevation(this, MainActivity.dpToPixels * 2);
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		int margin = Math.round(8 * MainActivity.dpToPixels);
		params.setMargins(margin, margin / 2, margin / 2, margin);
		this.setLayoutParams(params);
	}

}
