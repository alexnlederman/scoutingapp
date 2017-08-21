package com.example.vanguard.graphs;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vanguard.custom_ui_elements.MaterialLinearLayout;
import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.example.vanguard.R;

/**
 * Created by mbent on 7/6/2017.
 */

public class GraphDescriber extends MaterialLinearLayout {

	public GraphDescriber(Context context, AnswerList<Question> questions, Graph.GraphTypes graphType, final String id) {
		super(context);

		this.setOrientation(HORIZONTAL);

		Button deleteButton = new Button(context);
		deleteButton.setText("X");
		deleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.databaseManager.deleteGraph(id);
				setVisibility(GONE);
			}
		});
		this.addView(deleteButton);

		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(VERTICAL);


		TextView graphTypeTextView = new TextView(context);
		graphTypeTextView.setText(graphType.getName());
		graphTypeTextView.setTextColor(ContextCompat.getColor(context, R.color.textColor));
		layout.addView(graphTypeTextView);

		for (Question question : questions) {
			TextView questionLabelTextView = new TextView(context);
			questionLabelTextView.setText(question.getQualifiedLabel());
			questionLabelTextView.setTextColor(ContextCompat.getColor(context, R.color.textColor));
			layout.addView(questionLabelTextView);
		}

		this.addView(layout);


		LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(layoutParams);
	}
}
