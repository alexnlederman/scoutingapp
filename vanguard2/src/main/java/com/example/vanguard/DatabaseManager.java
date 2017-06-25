package com.example.vanguard;

import android.app.DownloadManager;
import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.Reducer;
import com.couchbase.lite.UnsavedRevision;
import com.couchbase.lite.View;
import com.couchbase.lite.android.AndroidContext;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionTypes.IntegerQuestion;
import com.example.vanguard.Questions.QuestionTypes.MatchNumberQuestion;
import com.example.vanguard.Questions.QuestionTypes.StringQuestion;
import com.example.vanguard.Questions.QuestionTypes.TeamNumberQuestion;
import com.example.vanguard.Questions.QuestionViewers.MatchScoutQuestionList;
import com.example.vanguard.Questions.QuestionViewers.PitScoutQuestionList;
import com.example.vanguard.Responses.Response;
import com.example.vanguard.Responses.SimpleResponse;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by mbent on 6/19/2017.
 */

public class DatabaseManager {

	private final String match_question_type = "match_question";
	private final String pit_question_type = "pit_question";

	private final String question_label_key = "label";
	private final String question_type_key = "question_type";
	private final String question_responses_key = "responses";
	private final String question_index_key = "index";

	private final String response_value_key = "response_value";
	private final String response_match_number_key = "match_number";
	private final String response_team_number_key = "team_number";

	private final String document_type_key = "document_type";


	private final String match_questions_view = "match_questions";
	private final String pit_questions_view = "pit_questions";
	private final String match_response_view = "match_responses";
	View matchQuestionView;
	View pitQuestionView;
	View matchResponseView;

	Database database;

	Context context;


	public enum QuestionTypes {
		INTEGER("INTEGER"),
		STRING("STRING"),
		MATCH_NUMBER("MATCH_NUMBER"),
		TEAM_NUMBER("TEAM_NUMBER");

		private final String text;

		private QuestionTypes(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return this.text;
		}
	}


	public DatabaseManager(Context context) {
		this.context = context;
		Manager manager = null;
		try {
			manager = new Manager(new AndroidContext(this.context.getApplicationContext()), Manager.DEFAULT_OPTIONS);
			this.database = manager.getDatabase("app2");
			matchQuestionView = this.database.getView(match_questions_view);
			matchQuestionView.setMap(new MatchQuestionMap(), "1");
			pitQuestionView = this.database.getView(pit_questions_view);
			pitQuestionView.setMap(new PitQuestionMap(), "1");
			matchResponseView = this.database.getView(match_response_view);
			matchResponseView.setMap(new MatchResponsesMap(), "1");
		} catch (CouchbaseLiteException | IOException e) {
			e.printStackTrace();
		}
	}

	public Question createQuestion(String label, String questionType, boolean isMatchQuestion) {
		int index;
		if (isMatchQuestion) {
			index = this.matchQuestionView.getTotalRows();
		}
		else {
			index = this.pitQuestionView.getTotalRows();
		}
		Document document = this.database.createDocument();
		try {
			document.update(new QuestionCreator(label, index, questionType, isMatchQuestion));
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
		return getQuestionVariable(document.getProperties());
	}

	public void setQuestionLabel(Question question, final String newLabel) {
		Document document = this.database.getDocument(question.getID());
		try {
			document.update(new Document.DocumentUpdater() {
				@Override
				public boolean update(UnsavedRevision newRevision) {
					Map<String, Object> properties = newRevision.getProperties();
					properties.put(question_label_key, newLabel);
					newRevision.setProperties(properties);
					return true;
				}
			});
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
	}

	public void swapQuestionIndexes(final int originalIndex, final int newIndex, boolean isMatchQuestion) {
		Document originalIndexDocument = this.getQuestionDocumentByIndex(originalIndex, isMatchQuestion);
		Document newIndexDocument = this.getQuestionDocumentByIndex(newIndex, isMatchQuestion);
		try {
			originalIndexDocument.update(new Document.DocumentUpdater() {
				@Override
				public boolean update(UnsavedRevision newRevision) {
					Map<String, Object> properties = newRevision.getProperties();
					properties.put(question_index_key, newIndex);
					newRevision.setProperties(properties);
					return true;
				}
			});
			newIndexDocument.update(new Document.DocumentUpdater() {
				@Override
				public boolean update(UnsavedRevision newRevision) {
					Map<String, Object> properties = newRevision.getProperties();
					properties.put(question_index_key, originalIndex);
					newRevision.setProperties(properties);
					return true;
				}
			});
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
	}

	public void saveResponses(AnswerList<Question> questions) {
		for (final Question question : questions) {
			Document document = this.database.getDocument(question.getID());
			try {
				document.update(new Document.DocumentUpdater() {
					@Override
					public boolean update(UnsavedRevision newRevision) {
						Map<String, Object> properties = newRevision.getProperties();
						properties.put(question_responses_key, getQuestionResponseHashmaps(question));
						newRevision.setProperties(properties);
						return true;
					}
				});
			} catch (CouchbaseLiteException e) {
				e.printStackTrace();
			}
		}
	}

	private List<HashMap<String, Object>> getQuestionResponseHashmaps(Question question) {
		ResponseList<Object> responses = question.getResponses();
		List<HashMap<String, Object>> responseHashMaps = new ArrayList<>();
		for (Response<Object> response : responses) {
			HashMap<String, Object> responseHashMap = new HashMap<>();
			responseHashMap.put(response_value_key, response.getValue());
			responseHashMap.put(response_match_number_key, response.getMatchNumber());
			responseHashMap.put(response_team_number_key, response.getTeamNumber());
			responseHashMaps.add(responseHashMap);
		}
		return responseHashMaps;
	}

	public void deleteQuestion(int index, boolean isMatchQuestion) {
		System.out.println("Delete Index: " + index);
		try {
			getQuestionDocumentByIndex(index, isMatchQuestion).delete();
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
	}

	public AnswerList<Question> getPitQuestions() {
		return createQuestionListFromQuery(this.pitQuestionView.createQuery());
	}

	public AnswerList<Question> getMatchQuestions() {
		return createQuestionListFromQuery(this.matchQuestionView.createQuery());
	}

	private AnswerList<Question> createQuestionListFromQuery(Query query) {
		AnswerList<Question> questions = new AnswerList<Question>();
		try {
			QueryEnumerator enumerator = query.run();

			for (Iterator<QueryRow> it = enumerator; it.hasNext();) {
				QueryRow row = it.next();
				System.out.println("Key: " + row.getKey());
				Map<String, Object> map = (Map<String, Object>) row.getValue();
				questions.add(getQuestionVariable(map));
			}

		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
		return questions;
	}

	// TODO Add method to get index of match_number and team_number question.

	// TODO come up with better method name.
	public void getMatchQuestionResponsesForTeam(String id, int teamNumber) {
		Query matchResponses = this.matchResponseView.createQuery();
//		matchResponses.setKeys();
	}

	private Question getQuestionVariable(Map<String, Object> map) {
		Question question = null;
		switch (QuestionTypes.valueOf((String) map.get(question_type_key))) {
			case INTEGER:
				question = new IntegerQuestion(this.context, (String) map.get(question_label_key), getQuestionMapResponses(map), (String) map.get("_id"));
				break;
			case STRING:
				question = new StringQuestion(this.context, (String) map.get(question_label_key), getQuestionMapResponses(map), (String) map.get("_id"));
				break;
			case MATCH_NUMBER:
				question = new MatchNumberQuestion(this.context, getQuestionMapResponses(map), (String) map.get("_id"));
				break;
			case TEAM_NUMBER:
				question = new TeamNumberQuestion(this.context, getQuestionMapResponses(map), (String) map.get("_id"));
				break;
		}
		return question;
	}

	private ResponseList getQuestionMapResponses(Map<String, Object> questionMap) {
		ArrayList<Map<String, Object>> responseMaps = (ArrayList<Map<String, Object>>) questionMap.get(question_responses_key);
		ResponseList responses = new ResponseList();
		for (Map<String, Object> map : responseMaps) {
			SimpleResponse response = new SimpleResponse(map.get(response_value_key), (int) map.get(response_match_number_key), (int) map.get(response_team_number_key));
			responses.add(response);
		}
		return responses;
	}

	private Document getQuestionDocumentByIndex(int index, boolean isMatchQuestion) {
		Document document = null;
		Query query;
		System.out.println("Is Match Question: " + isMatchQuestion);
		if (isMatchQuestion) {
			query = matchQuestionView.createQuery();
		}
		else {
			query = pitQuestionView.createQuery();
		}
		List keys = new ArrayList<>();
		keys.add(index);
		query.setKeys(keys);
		try {
			QueryEnumerator results = query.run();
			System.out.println(results.getCount());
			if (results.getCount() != 1) {
				throw new Exception("More than one question has an index.");
			}
			QueryRow row = results.getRow(0);
			Map<String, Object> map = (Map<String, Object>) row.getValue();
			document = database.getDocument((String) map.get("_id"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	private class QuestionCreator implements Document.DocumentUpdater {

		String label;
		int index;
		String questionType;
		boolean isMatchQuestion;

		public QuestionCreator(String label, int index, String questionType, boolean isMatchQuestion) {
			this.label = label;
			this.index = index;
			this.questionType = questionType;
			this.isMatchQuestion = isMatchQuestion;
		}

		@Override
		public boolean update(UnsavedRevision newRevision) {
			Map<String, Object> map = newRevision.getProperties();
			map.put(question_label_key, this.label);
			map.put(question_index_key, this.index);
			map.put(question_type_key, this.questionType);
			String doc_type_key;
			if (isMatchQuestion) {
				doc_type_key = match_question_type;
			}
			else {
				doc_type_key = pit_question_type;
			}
			map.put(document_type_key, doc_type_key);
			map.put(question_responses_key, new ArrayList<Map<String, Object>>());
			newRevision.setProperties(map);
			return true;
		}
	}

	public class MatchQuestionMap implements Mapper {

		@Override
		public void map(Map<String, Object> document, Emitter emitter) {
			if (document.containsKey(document_type_key) && document.get(document_type_key).equals(match_question_type)) {
				emitter.emit(document.get(question_index_key), document);
			}
		}
	}

	public class PitQuestionMap implements Mapper {

		@Override
		public void map(Map<String, Object> document, Emitter emitter) {
			if (document.containsKey(document_type_key) && document.get(document_type_key).equals(pit_question_type)) {
				emitter.emit(document.get(question_index_key), document);
			}
		}
	}

	public class MatchResponsesMap implements Mapper, Reducer {

		@Override
		public void map(Map<String, Object> document, Emitter emitter) {
			if (document.containsKey(document_type_key) && document.get(document_type_key).equals(match_question_type)) {
				ArrayList<Map<String, Object>> responses = (ArrayList<Map<String, Object>>) document.get(question_responses_key);
				for (Map<String, Object> response : responses) {
					// TODO make there be a list of responses sorted by team number.
					// TODO The responses should also be paired with the question somehow.
					// responses sorted by team number.
					// TODO should be able to narrow down the response return to just value, and match number.
					emitter.emit(response.get(response_team_number_key), new Object[]{document.get("_id"), response});
				}
			}
		}

		@Override
		public Object reduce(List<Object> keys, List<Object> values, boolean rereduce) {
			// Or a list of team numbers then their responses divided by question.
			int currentTeamNumber = (int) keys.get(0);

			// Hashmap of team numbers then.
			// Each value goes to a hashmap of question ids which correspond to a list of their responses.

			Map<Integer, Map<String, List<Map<String, Object>>>> result = new HashMap<>();

			Map<String, List<Map<String, Object>>> teamResponses = new HashMap<>();

			for (int i = 0; i < keys.size(); i++) {
				if (currentTeamNumber != (int) keys.get(i)) {
					// TODO sort team responses by qual.
					result.put(currentTeamNumber, teamResponses);
					currentTeamNumber = (int) keys.get(i);
					teamResponses = new HashMap<>();
				}

				List<Map<String, Object>> responses = teamResponses.get(((Object[]) values.get(i))[0]);
				if (responses == null) responses = new ArrayList<>();
				responses.add(((Map<String, Object>)((Object[]) values.get(i))[1]));
				Collections.sort(responses, new ResponseComparator());
				teamResponses.put(((String) ((Object[]) values.get(i))[0]), responses);
			}

			return null;
		}

		private class ResponseComparator implements Comparator<Map<String, Object>> {

			@Override
			public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
				if ((int) lhs.get(response_match_number_key) > (int) rhs.get(response_match_number_key)) {
					return 1;
				}
				else if ((int) lhs.get(response_match_number_key) < (int) rhs.get(response_match_number_key)) {
					return -1;
				}
				return 0;
			}
		}
	}
}
