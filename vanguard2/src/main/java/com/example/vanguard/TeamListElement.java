package com.example.vanguard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.vanguard.Pages.Activities.PitFormActivity;
import com.example.vanguard.Questions.QuestionViewers.QuestionListViewers.QuestionListFormViewer;

import static com.example.vanguard.Pages.Activities.MatchFormActivity.TEAM_NUMBER;

/**
 * Created by mbent on 6/29/2017.
 */

public class TeamListElement extends RelativeLayout {

	public TeamListElement(final Activity context, final Integer team, final TeamSelectedListener listener) {
		super(context);
		TextView textView = new TextView(context);
		textView.setText(String.valueOf(team));
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.teamSelected(context, Integer.valueOf(team));

			}
		});
		textView.setTextSize(30);
		this.addView(textView);
		System.out.println("Created");
	}

	public interface TeamSelectedListener {
		void teamSelected(Context context, int team);
	}
}
