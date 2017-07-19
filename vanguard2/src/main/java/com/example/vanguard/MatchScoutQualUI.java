package com.example.vanguard;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.vanguard.CustomUIElements.MaterialLinearLayout;
import com.example.vanguard.Pages.Activities.MainActivity;

import java.util.List;

/**
 * Created by mbent on 6/27/2017.
 */

public class MatchScoutQualUI extends LinearLayout {

	Context context;
	OnClickListener onClickListener;

	public MatchScoutQualUI(Context context, int qualNumber, List<String> blueTeams, List<String> redTeams, OnClickListener onClickListener) {
		super(context);
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.context = context;
		this.onClickListener = onClickListener;

		MaterialLinearLayout mainView = new MaterialLinearLayout(context);

		TextView qualIndicator = new TextView(new ContextThemeWrapper(context, R.style.matchQualIndicatorStyle), null, R.style.matchQualIndicatorStyle);
		qualIndicator.setText("Qual\n" + qualNumber);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.27f);
		qualIndicator.setLayoutParams(layoutParams);
		mainView.addView(qualIndicator);


		mainView.setPadding(0, mainView.getPaddingTop(), mainView.getPaddingRight(), mainView.getPaddingBottom());


		TableLayout tableLayout = new TableLayout(context);
		tableLayout.setStretchAllColumns(true);
		tableLayout.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.73f));


		TableRow blueRow = setupTableRow(blueTeams);
		blueRow.setBackgroundColor(ContextCompat.getColor(context, R.color.blueTeam));
		tableLayout.addView(blueRow);


		TableRow redRow = setupTableRow(redTeams);
		redRow.setBackgroundColor(ContextCompat.getColor(context, R.color.redTeam));
		tableLayout.addView(redRow);

		mainView.addView(tableLayout);

		this.addView(mainView);
	}

	private TableRow setupTableRow(List<String> values) {
		TableRow row = new TableRow(context);
		for (String team : values) {
			Button button = new Button(new ContextThemeWrapper(context, R.style.matchButtonStyle), null, R.style.matchButtonStyle);
			String teamNumber = team.substring(3);
			button.setText(teamNumber);
			button.setContentDescription(teamNumber);
			button.setOnClickListener(onClickListener);
			row.addView(button);
		}
		return row;
	}
}
