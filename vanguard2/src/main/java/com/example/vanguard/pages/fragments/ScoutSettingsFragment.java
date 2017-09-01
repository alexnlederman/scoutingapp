package com.example.vanguard.pages.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanguard.R;
import com.example.vanguard.pages.activities.MainActivity;
import com.example.vanguard.questions.AnswerList;
import com.example.vanguard.questions.Question;
import com.example.vanguard.questions.question_viewers.question_list_viewers.QuestionEditListView.ListViewAdapter;
import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.swipe.ListSwipeHelper;
import com.woxthebox.draglistview.swipe.ListSwipeItem;

/**
 * Created by mbent on 7/3/2017.
 */

public class ScoutSettingsFragment extends Fragment {

	DragListView selector;

	public ScoutSettingsFragment() {
		setHasOptionsMenu(true);
	}

	public static ScoutSettingsFragment newInstance(boolean isMatchForm) {

		Bundle args = new Bundle();

		args.putBoolean("match_form", isMatchForm);

		ScoutSettingsFragment fragment = new ScoutSettingsFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_question_edit_view, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.selector = (DragListView) getActivity().findViewById(R.id.question_edit_list_view);
		final AnswerList<Question> questions;
		this.selector.setCanDragHorizontally(false);
		if (isMatchForm()) {
			questions = MainActivity.databaseManager.getMatchQuestions();
		} else
			questions = MainActivity.databaseManager.getPitQuestions();

		this.selector.setSwipeListener(new ListSwipeHelper.OnSwipeListenerAdapter() {
			@Override
			public void onItemSwipeStarted(ListSwipeItem item) {
				super.onItemSwipeStarted(item);
				System.out.println("SWIPE START");

			}

			@Override
			public void onItemSwipeEnded(ListSwipeItem item, ListSwipeItem.SwipeDirection swipedDirection) {
				super.onItemSwipeEnded(item, swipedDirection);
				System.out.println("SWIPED: " + swipedDirection.name());

			}

			@Override
			public void onItemSwiping(ListSwipeItem item, float swipedDistanceX) {
				super.onItemSwiping(item, swipedDistanceX);
				System.out.println("SWIPING: " + swipedDistanceX);

			}
		});
		this.selector.setAdapter(new ListViewAdapter(getActivity(), questions), false);
		this.selector.setLayoutManager(new LinearLayoutManager(this.getActivity()));
	}

	public boolean isMatchForm() {
		return getArguments().getBoolean("match_form");
	}
}
