package com.example.vanguard.Pages.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.vanguard.Pages.Fragments.AddGraphFragment;
import com.example.vanguard.R;

/**
 * Created by mbent on 7/6/2017.
 */

public class AddGraphActivity extends AbstractActivity {

	public AddGraphActivity() {
		super(R.layout.activity_fragment_holder, R.string.add_graph_page_title);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MainActivity.setFragmentSave(new AddGraphFragment(), R.id.fragment_holder_layout, this, false);
	}
}
