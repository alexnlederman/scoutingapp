package com.example.vanguard.Questions;

import android.content.Context;
import android.content.SharedPreferences;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.UnsavedRevision;
import com.couchbase.lite.support.JsonDocument;
import com.couchbase.lite.support.Range;
import com.example.vanguard.Questions.QuestionTypes.StringQuestion;
import com.example.vanguard.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BertTurtle on 6/15/2017.
 */

public class ScoutQuestionList extends QuestionList {

	String questionKey;
	Document document;

	public ScoutQuestionList(Context context, Document document, String questionKey) {
		super(context);
		this.questionKey = questionKey;
		this.document = document;
		System.out.println(document.getProperties());

		if (this.document.getProperties().containsKey(this.questionKey)) {
			HashMap<String, Object>[] hashMap = (HashMap<String, Object>[]) this.document.getProperties().get(this.questionKey);
			this.setHashMap(hashMap, context);
		}
	}

	public void saveQuestions() {
//		Map<String, Object> map = this.document.getProperties();
//		System.out.println(map.getClass());
//		map.put(this.questionKey, "Hi");
		try {
			System.out.println(getQuestionHashMaps());
			this.document.update(new CUpdate());
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
	}

	public class CUpdate implements Document.DocumentUpdater {

		@Override
		public boolean update(UnsavedRevision unsavedRevision) {
			Map<String, Object> map = unsavedRevision.getProperties();
			System.out.println(map.getClass());
			map.put(questionKey, getQuestionHashMaps());
			return true;
		}
	}
}
