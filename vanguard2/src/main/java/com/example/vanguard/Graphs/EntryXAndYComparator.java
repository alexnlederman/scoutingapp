package com.example.vanguard.Graphs;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.EntryXComparator;

/**
 * Created by mbent on 7/25/2017.
 */

public class EntryXAndYComparator extends EntryXComparator {

	@Override
	public int compare(Entry entry1, Entry entry2) {
		int compare = super.compare(entry1, entry2);
		if (compare == 0) {
			float diff = entry1.getY() - entry2.getY();

			if (diff == 0f) return 0;
			else {
				if (diff > 0f) return 1;
				else return -1;
			}
		}
		return compare;
	}
}
