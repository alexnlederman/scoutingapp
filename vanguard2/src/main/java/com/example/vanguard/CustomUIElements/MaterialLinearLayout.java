package com.example.vanguard.CustomUIElements;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.R;

/**
 * Created by BertTurtle on 6/5/2017.
 * A material version of a LinearLayout
 */


public class MaterialLinearLayout extends LinearLayout{

	public MaterialLinearLayout(Context context) {
		super(new ContextThemeWrapper(context, R.style.materialStyle), null, R.style.materialStyle);
		LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		int margin = Math.round(8 * MainActivity.dpToPixels);
		params.setMargins(margin, margin / 2, margin, margin / 2);
		this.setLayoutParams(params);
	}

}
