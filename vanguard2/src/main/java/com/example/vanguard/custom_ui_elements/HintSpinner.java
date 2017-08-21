package com.example.vanguard.custom_ui_elements;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.vanguard.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mbent on 8/17/2017.
 */

public class HintSpinner extends AppCompatSpinner {

	String hint;
	OnItemSelectedListener selectedListener;

	public HintSpinner(Context context, Object[] values, String hint) {
		super(context);
		this.hint = hint;

		List<Object> listValues = new ArrayList<Object>(Arrays.asList(values));
		listValues.add(0, hint);

		this.setAdapter(new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, listValues));

		this.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position != 0) {
					selectedListener.onItemSelected(parent, view, position, id);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				selectedListener.onNothingSelected(parent);
			}
		});
	}

	public void setSelectedListener(OnItemSelectedListener selectedListener) {
		this.selectedListener = selectedListener;
	}

//	private class HintAdapter extends ArrayAdapter<String> {
//
//		public HintAdapter(Context context, String[] objects) {
//			super(context,  R.layout.support_simple_spinner_dropdown_item, objects);
//		}
//
//
//	}
}
