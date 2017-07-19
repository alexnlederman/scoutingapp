package com.example.vanguard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.Toast;

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
import com.couchbase.lite.support.LazyJsonArray;
import com.example.vanguard.Graphs.BarGraph;
import com.example.vanguard.Graphs.Graph;
import com.example.vanguard.Graphs.GraphDescriber;
import com.example.vanguard.Graphs.LineGraph;
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Properties.QuestionProperty;
import com.example.vanguard.Questions.Properties.SimpleQuestionProperty;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionTypes.IntegerQuestion;
import com.example.vanguard.Questions.QuestionTypes.MatchNumberQuestion;
import com.example.vanguard.Questions.QuestionTypes.PitTeamNumberQuestion;
import com.example.vanguard.Questions.QuestionTypes.StringQuestion;
import com.example.vanguard.Questions.QuestionTypes.MatchTeamNumberQuestion;
import com.example.vanguard.Responses.Response;
import com.example.vanguard.Responses.SimpleResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by mbent on 6/19/2017.
 * Does all of the interacting with the couchbase database.
 * Nothing else should directly access the database but go through here.
 */
public class DatabaseManager {

	// List of keys for accessing items in the couchbase database.
	private final String match_question_type = "match_question";
	private final String pit_question_type = "pit_question";

	private final String question_label_key = "label";
	private final String question_type_key = "question_type";
	private final String question_responses_key = "responses";
	private final String question_index_key = "index";
	private final String question_properties_key = "question_properties_key";

	private final String question_property_name_key = "question_property_name_key";
	private final String question_property_value_key = "question_property_value_key";

	private final String response_value_key = "response_value";
	private final String response_match_number_key = "match_number";
	private final String response_team_number_key = "team_number";
	private final String response_is_practice_match_key = "response_is_practice_match_key";
	private final String response_event_key = "response_event_key";

	public static final String event_key_key = "event_name";
	public static final String event_match_number_key = "event_match_number";
	public static final String event_match_blue_team = "event_match_blue_team";
	public static final String event_match_red_team = "event_match_red_team";
	private final String event_match_document_type = "event_match_document";

	private final String event_teams_key = "event_teams";
	private final String event_teams_document_type = "event_teams_document";
	public static final String event_name_key = "event_name_key";

	private final String event_information_document_type = "event_info_document";
	private final String is_current_event_key = "is_current_event_key";

	private final String graph_question_id_key = "graph_question_id_key";
	private final String is_all_team_graph_key = "is_all_team_graph_key";
	private final String graph_type_key = "graph_type_key";
	private final String graph_document_type = "graph_key";


	private final String document_type_key = "document_type";


	private final String match_questions_view = "match_questions";
	private final String pit_questions_view = "pit_questions";
	private final String match_response_view = "match_responses";
	private final String event_view = "event_view";
	private final String event_teams_view = "event_teams_view";
	private final String event_info_view = "event_info_view";
	private final String graph_view = "graph_view";

	// Views to help find specific documents.

	// Finds all of the match questions.
	View matchQuestionView;
	// Finds all of the pit questions.
	View pitQuestionView;
	// Finds all of the responses for the pit questions and compiles them into a more accesible state.
	// Hashmap of team numbers which go to a hashmap of their responses sorted by qual number, with the key being the response's question's id.
	View matchResponseView;

	// Finds the events the person is scouting.
	View eventMatchesView;

	View eventTeamsView;

	View eventInfoView;

	View graphView;

	Database database;

	Activity context;

	/**
	 * Creates a new DatabaseManager. This sets up the whole database.
	 * @param context the application context.
	 */
	public DatabaseManager(Activity context) {
		this.context = context;
		Manager manager;
		try {
			// Finds the database.
			manager = new Manager(new AndroidContext(this.context.getApplicationContext()), Manager.DEFAULT_OPTIONS);
			// Can change string below to reset the database.
			this.database = manager.getDatabase("app31");

			// Creates database views.
			matchQuestionView = this.database.getView(match_questions_view);
			matchQuestionView.setMap(new MatchQuestionMap(), "1");
			pitQuestionView = this.database.getView(pit_questions_view);
			pitQuestionView.setMap(new PitQuestionMap(), "1");
			matchResponseView = this.database.getView(match_response_view);
			matchResponseView.setMapReduce(new MatchResponsesMap(), new MatchResponsesMap(), "4");
			eventMatchesView = this.database.getView(event_view);
			eventMatchesView.setMap(new EventMatchesMapper(), "2");
			eventTeamsView = this.database.getView(event_teams_view);
			eventTeamsView.setMap(new EventTeamsMapper(), "5");
			eventInfoView = this.database.getView(event_info_view);
			eventInfoView.setMap(new EventInformationMapper(), "1");
			graphView = this.database.getView(graph_view);
			graphView.setMap(new GraphMapper(), "4");
		} catch (CouchbaseLiteException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new question in the database.
	 * @param label The text for the question.
	 * @param questionType The type of question. Should be a {@link Question.QuestionType} enum.
	 * @param isMatchQuestion Whether the question is used for match or pit scouting.
	 * @return The {@link Question} object that was created.
	 */
	public Question createQuestion(String label, String questionType, boolean isMatchQuestion, List<QuestionProperty> questionProperties) {

		// Finds the index of the question.
		int index;
		if (isMatchQuestion) {
			index = this.matchQuestionView.getTotalRows();
		}
		else {
			index = this.pitQuestionView.getTotalRows();
		}

		List<Map<String, Object>> propertyMapList = new ArrayList<>();
		for (QuestionProperty property : questionProperties) {
			Map<String, Object> map = new HashMap<>();
			map.put(question_property_name_key, property.getName());
			map.put(question_property_value_key, property.getValue());
			propertyMapList.add(map);
		}

		// Creates a new document for question.
		Document document = this.database.createDocument();

		// Adds values to document.
		try {
			document.update(new QuestionCreator(label, index, questionType, isMatchQuestion, propertyMapList));
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}

		// Gets question Object to return.
		return getQuestionVariable(document.getProperties());
	}

	/**
	 * Updates a question's label.
	 * @param question The question that is being changed.
	 * @param newLabel The new label.
	 */
	public void setQuestionLabel(Question question, final String newLabel) {
		// Gets the question.
		Document document = this.database.getDocument(question.getID());

		// Updates the label.
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

	/**
	 * Swaps two question's indexes.
	 * @param firstQuestionIndex The index of one of the questions.
	 * @param secondQuestionIndex The index of a second question.
	 * @param isMatchQuestion If the questions are from a match.
	 */
	public void swapQuestionIndexes(final int firstQuestionIndex, final int secondQuestionIndex, boolean isMatchQuestion) {

		// Gets question documents.
		Document originalIndexDocument = this.getQuestionDocumentByIndex(firstQuestionIndex, isMatchQuestion);
		Document newIndexDocument = this.getQuestionDocumentByIndex(secondQuestionIndex, isMatchQuestion);

		// Swaps the indexes of each of the questions.
		try {
			originalIndexDocument.update(new Document.DocumentUpdater() {
				@Override
				public boolean update(UnsavedRevision newRevision) {
					Map<String, Object> properties = newRevision.getProperties();
					properties.put(question_index_key, secondQuestionIndex);
					newRevision.setProperties(properties);
					return true;
				}
			});
			newIndexDocument.update(new Document.DocumentUpdater() {
				@Override
				public boolean update(UnsavedRevision newRevision) {
					Map<String, Object> properties = newRevision.getProperties();
					properties.put(question_index_key, firstQuestionIndex);
					newRevision.setProperties(properties);
					return true;
				}
			});
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves all the responses for a list of questions.
	 * @param questions The list of questions to have their responses saved.
	 */
	public void saveResponses(AnswerList<Question> questions) {
		// Loops through each question.
		for (final Question question : questions) {

			// Gets the current question document.
			Document document = this.database.getDocument(question.getID());

			if (document != null) {
				// Saves the response of the question.
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
	}

	/**
	 * Gets hashmaps of the responses for the question.
	 * @param question
	 * @return
	 */
	private List<HashMap<String, Object>> getQuestionResponseHashmaps(Question question) {
		// Gets question responses.
		AnswerList<Response> responses = question.getResponses();

		List<HashMap<String, Object>> responseHashMaps = new ArrayList<>();

		// Loops through the current responses and adds the proper values to them.
		for (Response response : responses) {
			HashMap<String, Object> responseHashMap = new HashMap<>();
			responseHashMap.put(response_value_key, response.getValue());
			responseHashMap.put(response_match_number_key, response.getMatchNumber());
			responseHashMap.put(response_team_number_key, response.getTeamNumber());
			responseHashMap.put(response_is_practice_match_key, response.isPracticeMatchResponse());
			responseHashMap.put(response_event_key, response.getEventKey());
			responseHashMaps.add(responseHashMap);
		}
		return responseHashMaps;
	}

	/**
	 * Deletes a question.
	 * @param question the question to delete.
	 */
	public void deleteQuestion(Question question) {
		// TODO have this set the questions index to -1 and then ignore it. This will make graphing work with deleted questions.
		Document document = database.getDocument(question.getID());
		Map<String, Object> prop = document.getProperties();
		boolean isMatchQuestion = prop.get(document_type_key).equals(match_question_type);
		int index = (int) prop.get(question_index_key);
		try {
			document.delete();
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
		Query query;
		// Updates all the other question's index so they fill the space taken by the deleted question.
		if (isMatchQuestion)
			query = matchQuestionView.createQuery();
		else
			query = pitQuestionView.createQuery();
		query.setStartKey(index + 1);
		try {
			QueryEnumerator result = query.run();
			for (Iterator<QueryRow> it = result; it.hasNext();) {
				QueryRow row = it.next();
				final int currentQuestionIndex = (int) row.getKey();
				Document questionDocument = getQuestionDocumentByIndex(currentQuestionIndex, isMatchQuestion);
				questionDocument.update(new Document.DocumentUpdater() {
					@Override
					public boolean update(UnsavedRevision newRevision) {
						Map<String, Object> prop = newRevision.getProperties();
						prop.put(question_index_key, currentQuestionIndex - 1);
						newRevision.setProperties(prop);
						return true;
					}
				});
			}

		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return An {@link AnswerList} of pit questions.
	 */
	public AnswerList<Question> getPitQuestions() {
		AnswerList<Question> questions = createQuestionListFromQuery(this.pitQuestionView.createQuery());
		if (questions.size() == 0) {
			questions.add(createQuestion("Team Number", Question.QuestionType.PIT_TEAM_NUMBER.toString(), false, new ArrayList<QuestionProperty>()));
		}
		return questions;
	}

	/**
	 * @return An {@link AnswerList} of match questions.
	 */
	public AnswerList<Question> getMatchQuestions() {
		AnswerList<Question> questions = createQuestionListFromQuery(this.matchQuestionView.createQuery());
		if (questions.size() == 0) {
			questions.add(createQuestion("Match Number", Question.QuestionType.MATCH_NUMBER.toString(), true, new ArrayList<QuestionProperty>()));
			questions.add(createQuestion("Team Number", Question.QuestionType.MATCH_TEAM_NUMBER.toString(), true, new ArrayList<QuestionProperty>()));
		}
		return questions;
	}

	/**
	 * Generates {@link AnswerList} of questions from a query.
	 * @param query The query.
	 * @return The {@link AnswerList} of questions.
	 */
	private AnswerList<Question> createQuestionListFromQuery(Query query) {
		AnswerList<Question> questions = new AnswerList<Question>();
		try {
			// Runs the query.
			QueryEnumerator enumerator = query.run();

			// Goes through the results and adds questions to AnswerList.
			for (Iterator<QueryRow> it = enumerator; it.hasNext();) {
				QueryRow row = it.next();
				Map<String, Object> map = (Map<String, Object>) row.getValue();
				questions.add(getQuestionVariable(map));
			}

		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
		return questions;
	}

	/**
	 * Gets a {@link Question} from its hashmap.
	 * @param map The hashmap of the question.
	 * @return The {@link Question} object generated.
	 */
	private Question getQuestionVariable(Map<String, Object> map) {
//		Question question = null;
		// Checks which question type it correspondse to and creates a Question from that.
		boolean isMatchQuestion = map.get(document_type_key).equals(match_question_type);
//		switch (Question.QuestionType.valueOf((String) map.get(question_type_key))) {
//			case INTEGER:
//				question = new IntegerQuestion(this.context, (String) map.get(question_label_key), getQuestionMapResponses(map), (String) map.get("_id"), isMatchQuestion);
//				break;
//			case STRING:
//				question = new StringQuestion(this.context, (String) map.get(question_label_key), getQuestionMapResponses(map), (String) map.get("_id"), isMatchQuestion);
//				break;
//			case MATCH_NUMBER:
//				question = new MatchNumberQuestion(this.context, getQuestionMapResponses(map), (String) map.get("_id"));
//				break;
//			case MATCH_TEAM_NUMBER:
//				question = new MatchTeamNumberQuestion(this.context, getQuestionMapResponses(map), (String) map.get("_id"));
//				break;
//			case PIT_TEAM_NUMBER:
//				question = new PitTeamNumberQuestion(this.context, getQuestionMapResponses(map), (String) map.get("_id"));
//				break;
//		}

		List<QuestionProperty> properties = getQuestionPropertiesFromMap((List<Map<String, Object>>) map.get(question_properties_key));

		return Question.createQuestionByType(Question.QuestionType.valueOf((String) map.get(question_type_key)), (String) map.get(question_label_key), getQuestionMapResponses(map), isMatchQuestion, (String) map.get("_id"), this.context, properties);
//		return question;
	}

	private List<QuestionProperty> getQuestionPropertiesFromMap(List<Map<String, Object>> propertyMaps) {
		List<QuestionProperty> properties = new ArrayList<>();
		for (Map<String, Object> map : propertyMaps) {
			QuestionProperty property = new SimpleQuestionProperty(map.get(question_property_value_key), (String) map.get(question_property_name_key));
			properties.add(property);
		}
		return properties;
	}

	/**
	 * Gets list of responses from a question Map.
	 * @param questionMap The question Map.
	 * @return The responses for the question.
	 */
	private AnswerList<Response> getQuestionMapResponses(Map<String, Object> questionMap) {
		// Gets list of questions Maps.
		ArrayList<Map<String, Object>> responseMaps = (ArrayList<Map<String, Object>>) questionMap.get(question_responses_key);
		AnswerList<Response> responses = new AnswerList<>();
		// Converts list of question Maps to a list of responses.
		for (Map<String, Object> map : responseMaps) {
			SimpleResponse response = getResponseFromMap(map);
			responses.add(response);
		}
		return responses;
	}

	/**
	 * Gets the document for a question from its index.
	 * @param index The index of the question.
	 * @param isMatchQuestion If the question is a match question or a pit question.
	 * @return The document of the question.
	 */
	private Document getQuestionDocumentByIndex(int index, boolean isMatchQuestion) {
		Document document = null;

		// Gets proper query.
		Query query;
		if (isMatchQuestion) {
			query = matchQuestionView.createQuery();
		}
		else {
			query = pitQuestionView.createQuery();
		}
		// Only querys for question with proper index.
		List keys = new ArrayList<>();
		keys.add(index);
		query.setKeys(keys);
		try {
			QueryEnumerator results = query.run();
			if (results.getCount() != 1) {
				throw new Exception("More than one question has an index.");
			}
			// TODO might be able to make this faster.
			// Finds the question Id and gets the document.
			QueryRow row = results.getRow(0);
			Map<String, Object> map = (Map<String, Object>) row.getValue();
			document = database.getDocument((String) map.get("_id"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * Sets up a questions values.
	 */
	private class QuestionCreator implements Document.DocumentUpdater {

		String label;
		int index;
		String questionType;
		boolean isMatchQuestion;
		List<Map<String, Object>> propertyMaps;

		public QuestionCreator(String label, int index, String questionType, boolean isMatchQuestion, List<Map<String, Object>> propertyMaps) {
			this.label = label;
			this.index = index;
			this.questionType = questionType;
			this.isMatchQuestion = isMatchQuestion;
			this.propertyMaps = propertyMaps;
		}

		/**
		 * Saves values for questions.
		 * {@inheritDoc}
		 */
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
			map.put(question_properties_key, this.propertyMaps);
			newRevision.setProperties(map);
			return true;
		}
	}

	/**
	 * Gets the current event's key and name in that order.
	 * @return An array of the events key then name.
	 */
	private List getCurrentEventInfo() {
		Query query = eventInfoView.createQuery();

		query.setStartKey(true);
		query.setEndKey(true);

		QueryEnumerator result = null;
		try {
			result = query.run();
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}

		QueryRow row = result.next();
		return (row == null) ? null : (List) row.getValue();
	}

	public boolean isCurrentEventSet() {
		return !getCurrentEventName().equals("");
	}

	public String getCurrentEventName() {
		List info = getCurrentEventInfo();
		if (info == null)
			return "";
		return (String) info.get(1);
	}

	public String getCurrentEventKey() {
		List info = getCurrentEventInfo();
		if (info == null)
			return "";
		return (String) info.get(0);
	}

	public List<Integer> getCurrentEventTeams() {
		return getEventTeams(getCurrentEventKey());
	}

	public List<Map<String, Object>> getCurrentEventMatches() {
		return getEventMatches(getCurrentEventKey());
	}

	public void setCurrentEvent(String eventName) {
		List currentInfo = getCurrentEventInfo();
		if (currentInfo != null) {
			String id = (String) currentInfo.get(2);
			Document document = this.database.getDocument(id);
			try {
				document.update(new Document.DocumentUpdater() {
					@Override
					public boolean update(UnsavedRevision newRevision) {
						Map<String, Object> prop = newRevision.getProperties();
						prop.put(is_current_event_key, false);
						newRevision.setProperties(prop);
						return true;
					}
				});
			} catch (CouchbaseLiteException e) {
				e.printStackTrace();
			}
		}
		Document newDocument = this.database.getDocument(getEventInfoDocId(eventName));
		try {
			newDocument.update(new Document.DocumentUpdater() {
				@Override
				public boolean update(UnsavedRevision newRevision) {
					Map<String, Object> prop = newRevision.getProperties();
					prop.put(is_current_event_key, true);
					newRevision.setProperties(prop);
					return true;
				}
			});
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
	}

	public List<String> getEventNames() {
		Query query = eventInfoView.createQuery();

		QueryEnumerator result = null;
		try {
			result = query.run();
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
		List<String> names = new ArrayList<>();
		for (Iterator<QueryRow> it = result; it.hasNext();) {
			QueryRow row = it.next();
			names.add((String) ((List) row.getValue()).get(1));
		}
		return names;
	}

	private String getEventInfoDocId(String eventName) {
		Query query = eventInfoView.createQuery();

		QueryEnumerator result = null;
		try {
			result = query.run();
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}

		for (Iterator<QueryRow> it = result; it.hasNext();) {
			QueryRow row = it.next();
			if (((List) row.getValue()).get(1).equals(eventName)) {
				return (String) ((List) row.getValue()).get(2);
			}
		}
		return null;
	}

	public void addEvent(String eventKey, String eventName, Context context) {
		if (getEventTeams(eventKey).size() != 0) {
			System.out.println("Already Added: " + getEventMatches(eventKey).size());
			return;
		}
		AddEventDocuments asyncTask = new AddEventDocuments(context);
		asyncTask.execute(eventKey, eventName);
	}

	private class AddEventDocuments extends AsyncTask<String, Boolean, Boolean> {

		ProgressDialog progressDialog;
		Context context;

		public AddEventDocuments(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Loading");
			progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				String matchScheduleJson = getUrlValue(new URL("https://www.thebluealliance.com/api/v3/event/" + params[0] + "/matches/simple?X-TBA-Auth-Key=vVc9R5KHLDG2zkDgqFzRQRAFWBIPSSdyesezDG0m44p5yAiUBAz7qMasclG4Ua7a"));

				JSONArray eventJSONArray = new JSONArray(matchScheduleJson);
				for (int i = 0; i < eventJSONArray.length(); i++) {
					JSONObject matchJson = eventJSONArray.getJSONObject(i);
					if (matchJson.getString("comp_level").equals("qm")) {
						int qualNumber = matchJson.getInt("match_number");
						JSONObject matchTeamsJson = matchJson.getJSONObject("alliances");
						String[] blueTeams = getAllianceTeams(matchTeamsJson.getJSONObject("blue"));
						String[] redTeams = getAllianceTeams(matchTeamsJson.getJSONObject("red"));
						Document matchDocument = database.createDocument();
						matchDocument.update(new EventMatchDocumentUpdater(qualNumber, params[0], redTeams, blueTeams, params[1]));
					}
				}
				String eventTeamsJson = getUrlValue(new URL("https://www.thebluealliance.com/api/v3/event/" + params[0] + "/teams/keys?X-TBA-Auth-Key=vVc9R5KHLDG2zkDgqFzRQRAFWBIPSSdyesezDG0m44p5yAiUBAz7qMasclG4Ua7a"));
				System.out.println(eventTeamsJson);
				JSONArray array = new JSONArray(eventTeamsJson);
				List<Integer> eventTeams = new ArrayList<>();
				for (int i = 0; i < array.length(); i++) {
					eventTeams.add(Integer.valueOf(array.getString(i).substring(3)));
				}
				Document documument = database.createDocument();
				documument.update(new EventTeamsDocumentUpdater(params[0], params[1], eventTeams));

				Document document = database.createDocument();
				document.update(new EventInformationDocumentUpdater(params[0], params[1]));
			}
			catch (Exception e) {
				e.printStackTrace();
				return  false;
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean aBoolean) {
			super.onPostExecute(aBoolean);
			progressDialog.dismiss();
			if (aBoolean) {
				Toast.makeText(context, "Successfully Added", Toast.LENGTH_LONG).show();
			}
			else {
				Toast.makeText(context, "Make Sure You Have a Wireless Connections", Toast.LENGTH_LONG).show();
			}
		}
	}

	private class EventInformationDocumentUpdater implements Document.DocumentUpdater {

		String eventKey;
		String eventName;

		private EventInformationDocumentUpdater(String eventKey, String eventName) {
			this.eventKey = eventKey;
			this.eventName = eventName;
		}

		@Override
		public boolean update(UnsavedRevision newRevision) {
			Map<String, Object> prop = newRevision.getProperties();
			prop.put(event_name_key, eventName);
			prop.put(event_key_key, eventKey);
			prop.put(document_type_key, event_information_document_type);
			prop.put(is_current_event_key, false);
			newRevision.setProperties(prop);
			return true;
		}
	}

	private class EventInformationMapper implements Mapper {

		@Override
		public void map(Map<String, Object> document, Emitter emitter) {
			if (document.containsKey(document_type_key) && document.get(document_type_key).equals(event_information_document_type)) {
				emitter.emit(document.get(is_current_event_key), new Object[]{document.get(event_key_key), document.get(event_name_key), document.get("_id")});
			}
		}
	}

	private class GraphMapper implements Mapper {

		@Override
		public void map(Map<String, Object> document, Emitter emitter) {
			System.out.println(document.get(document_type_key));
			if (document.containsKey(document_type_key) && document.get(document_type_key).equals(graph_document_type)) {
				System.out.println("Emit");
				emitter.emit(document.get(is_all_team_graph_key), new Object[] {document.get(graph_type_key), document.get(graph_question_id_key), document.get("_id")});
			}
		}
	}

	public void addGraph(Graph.GraphTypes type, AnswerList<Question> questions) {
		Document document = this.database.createDocument();
		System.out.println("Q Size: " + questions.size());
		try {
			document.update(new GraphDocumentUpdater(type, questions));
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
	}

	public List<Graph> getTeamGraphs(int teamNumber) {
		Query query = graphView.createQuery();
		query.setStartKey(false);
		query.setEndKey(false);
		return getGraphs(query, teamNumber);
	}

	public List<Graph> getAllTeamGraphs() {
		Query query = graphView.createQuery();
		query.setStartKey(true);
		query.setEndKey(true);
		return getGraphs(query, -1);
	}

	private List<Graph> getGraphs(Query query, int teamNumber) {
		QueryEnumerator result = null;
		try {
			result = query.run();
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
		List<Graph> graphs = new ArrayList<>();
		for (Iterator<QueryRow> it = result; it.hasNext();) {
			QueryRow row = it.next();
			Graph.GraphTypes graphType = Graph.GraphTypes.valueOf((String) ((List) row.getValue()).get(0));
			List<String> graphQuestionIds = (List<String>) ((List) row.getValue()).get(1);
			graphs.add(createGraph(graphType, getQuestionFromIds(graphQuestionIds), teamNumber));
		}
		return graphs;
	}

	public List<GraphDescriber> getGraphDescribers() {
		Query query = graphView.createQuery();
		QueryEnumerator result = null;
		try {
			result = query.run();
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
		List<GraphDescriber> graphDescribers = new ArrayList<>();
		for (Iterator<QueryRow> it = result; it.hasNext();) {
			QueryRow row = it.next();
			Graph.GraphTypes graphType = Graph.GraphTypes.valueOf((String) ((List) row.getValue()).get(0));
			List<String> graphQuestionIds = (List<String>) ((List) row.getValue()).get(1);
			String id = (String) ((List) row.getValue()).get(2);
			graphDescribers.add(new GraphDescriber(context, getQuestionFromIds(graphQuestionIds), graphType, id));
		}
		return graphDescribers;
	}

	public void deleteGraph(String id) {
		Document document = this.database.getDocument(id);
		try {
			document.delete();
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
	}

	private AnswerList<Question> getQuestionFromIds(List<String> ids) {
		AnswerList<Question> questions = new AnswerList<>();
		for (String id : ids) {
			questions.add(getQuestionVariable(this.database.getDocument(id).getProperties()));
		}
		return questions;
	}

	private Graph createGraph(Graph.GraphTypes type, AnswerList<Question> questions, int teamNumber) {
		switch (type) {
			case LINE_GRAPH:
				return new LineGraph(context, questions, teamNumber);
			case BAR_GRAPH:
				return new BarGraph(context, questions);
		}
		return null;
	}

	private class GraphDocumentUpdater implements Document.DocumentUpdater {

		Graph.GraphTypes type;
		AnswerList<Question> questions;

		public GraphDocumentUpdater(Graph.GraphTypes type, AnswerList<Question> questions) {
			this.type = type;
			this.questions = questions;
			System.out.println("Question Size: " + questions.size());
		}

		@Override
		public boolean update(UnsavedRevision newRevision) {
			Map<String, Object> prop = newRevision.getProperties();
			prop.put(graph_type_key, this.type);
			prop.put(graph_question_id_key, getQuestionIds(this.questions));
			prop.put(document_type_key, graph_document_type);
			prop.put(is_all_team_graph_key, this.type.isAllTeamGraph());
			newRevision.setProperties(prop);
			return true;
		}
	}

	public List<String> getQuestionIds(List<Question> questions) {
		List<String> ids = new ArrayList<>();
		for (Question question : questions) {
			ids.add(question.getID());
		}
		return ids;
	}


	public static String getUrlValue(URL url) throws IOException {
		String input = "";
		HttpURLConnection blueAllianceConnection = (HttpURLConnection) url.openConnection();


		blueAllianceConnection.setConnectTimeout(5000);


		BufferedReader in = new BufferedReader(new InputStreamReader(blueAllianceConnection.getInputStream()));
		String currentLine = in.readLine();
		while (currentLine != null) {
			input += currentLine + "\n";
			currentLine = in.readLine();
		}
		in.close();
		return input;
	}

	private String[] getAllianceTeams(JSONObject allianceJson) throws JSONException {
		JSONArray teamKeysJson = allianceJson.getJSONArray("team_keys");
		String[] teamKeys = new String[3];
		for (int i = 0; i < teamKeysJson.length(); i++) {
			teamKeys[i] = teamKeysJson.getString(i);
		}
		return teamKeys;
	}

	public List<Map<String, Object>> getEventMatches(String event) {
		Query query = eventMatchesView.createQuery();
		List<Map<String, Object>> out = new ArrayList<>();
		try {
			QueryEnumerator result = query.run();
			for (Iterator<QueryRow> it = result; it.hasNext();) {
				QueryRow row = it.next();
				if (((List) row.getValue()).get(2).equals(event)) {
					Map<String, Object> matchMap = new HashMap<>();
					System.out.println(row.getKey());
					List<Object> values = (List<Object>) row.getValue();
					matchMap.put(event_match_number_key, row.getKey());
					matchMap.put(event_match_red_team, values.get(0));
					matchMap.put(event_match_blue_team, values.get(1));
					out.add(matchMap);
				}
			}
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
		return out;
	}

	private class EventMatchDocumentUpdater implements Document.DocumentUpdater {

		int matchNumber;
		String eventKey;
		String eventName;
		String[] redTeam;
		String[] blueTeam;

		private EventMatchDocumentUpdater(int matchNumber, String eventKey, String[] redTeam, String[] blueTeam, String eventName) {
			this.matchNumber = matchNumber;
			this.eventKey = eventKey;
			this.redTeam = redTeam;
			this.blueTeam = blueTeam;
			this.eventName = eventName;
		}

		@Override
		public boolean update(UnsavedRevision newRevision) {
			Map<String, Object> properties = newRevision.getProperties();
			properties.put(event_key_key, this.eventKey);
			properties.put(event_name_key, this.eventName);
			properties.put(event_match_number_key, this.matchNumber);
			properties.put(event_match_blue_team, this.blueTeam);
			properties.put(event_match_red_team, this.redTeam);
			properties.put(document_type_key, event_match_document_type);
			newRevision.setProperties(properties);
			return true;
		}
	}

	private class EventTeamsDocumentUpdater implements Document.DocumentUpdater {

		String eventKey;
		String eventName;
		List<Integer> teams;

		public EventTeamsDocumentUpdater(String eventKey, String eventName, List<Integer> teams) {
			this.eventKey = eventKey;
			this.teams = teams;
			this.eventName = eventName;
		}

		@Override
		public boolean update(UnsavedRevision newRevision) {
			Map<String, Object> properties = newRevision.getProperties();
			properties.put(event_key_key, this.eventKey);
			properties.put(event_name_key, this.eventName);
			properties.put(event_teams_key, this.teams);
			properties.put(document_type_key, event_teams_document_type);
			newRevision.setProperties(properties);
			return true;
		}
	}

	public List<Integer> getEventTeams(String event) {
		Query query = eventTeamsView.createQuery();
		List<Object> keys = new ArrayList<>();
		keys.add(event);
		query.setKeys(keys);
		QueryEnumerator result = null;
		try {
			result = query.run();
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}

		if (result.hasNext()) {
			QueryRow row = result.next();

			List<Integer> teamNumbers = (ArrayList<Integer>) ((LazyJsonArray) row.getValue()).get(0);

			System.out.println("Class: " + teamNumbers.getClass());

			Collections.sort(teamNumbers);
			return teamNumbers;
		}
		return new ArrayList<>();
	}

	private class EventTeamsMapper implements Mapper {

		@Override
		public void map(Map<String, Object> document, Emitter emitter) {
			if (document.containsKey(document_type_key) && document.get(document_type_key).equals(event_teams_document_type)) {
				emitter.emit(document.get(event_key_key), new Object[] {document.get(event_teams_key), document.get(event_name_key)});
			}
		}
	}

	private class EventMatchesMapper implements Mapper {

		@Override
		public void map(Map<String, Object> document, Emitter emitter) {
			System.out.println(document.get(document_type_key));
			if (document.containsKey(document_type_key) && document.get(document_type_key).equals(event_match_document_type)) {
				emitter.emit(document.get(event_match_number_key), new Object[]{document.get(event_match_red_team), document.get(event_match_blue_team), document.get(event_key_key), document.get(event_name_key)});
			}
		}
	}

	/**
	 * Finds all match questions.
	 * Emits the index as the key and Map as the value.
	 * {@inheritDoc}
	 */
	private class MatchQuestionMap implements Mapper {

		@Override
		public void map(Map<String, Object> document, Emitter emitter) {
			if (document.containsKey(document_type_key) && document.get(document_type_key).equals(match_question_type)) {
				emitter.emit(document.get(question_index_key), document);
			}
		}
	}

	/**
	 * Finds all pit questions.
	 * Emits the index as the key and Map as the value.
	 * {@inheritDoc}
	 */
	private class PitQuestionMap implements Mapper {

		@Override
		public void map(Map<String, Object> document, Emitter emitter) {
			if (document.containsKey(document_type_key) && document.get(document_type_key).equals(pit_question_type)) {
				emitter.emit(document.get(question_index_key), document);
			}
		}
	}

	private SimpleResponse getResponseFromMap(Map<String, Object> map) {
		return new SimpleResponse(map.get(response_value_key), (int) map.get(response_match_number_key), (int) map.get(response_team_number_key), (Boolean) map.get(response_is_practice_match_key), (String) map.get(response_event_key));
	}

	/**
	 * Gets all the responses stored by the match questions.
	 * Sorted by Hashmap of team numbers which go to a hashmap of their responses sorted by qual number, with the key being the response's question's id.
	 * {@inheritDoc}
	 */
	private class MatchResponsesMap implements Mapper, Reducer {

		@Override
		public void map(Map<String, Object> document, Emitter emitter) {
			// Gets proper documents.
			if (document.containsKey(document_type_key) && document.get(document_type_key).equals(match_question_type)) {
				// Gets the responses.
				ArrayList<Map<String, Object>> responses = (ArrayList<Map<String, Object>>) document.get(question_responses_key);
				for (int i = 0; i < responses.size(); i++) {
					// Emits the responses by team number.
					emitter.emit(responses.get(i).get(response_team_number_key), new Object[]{document.get("_id"), responses.get(i)});
				}
			}
		}

		@Override
		public Object reduce(List<Object> keys, List<Object> values, boolean rereduce) {
			System.out.println("Re: " + rereduce);
			int currentTeamNumber = (int) keys.get(0);

			Map<Integer, Map<String, List<Map<String, Object>>>> result = new HashMap<>();

			Map<String, List<Map<String, Object>>> teamResponses = new HashMap<>();

			// Goes through the team numbers and the responses.
			for (int i = 0; i < keys.size();) {
				// Gets the responses for current response question.
				List<Map<String, Object>> responses = teamResponses.get(((List) values.get(i)).get(0));
				if (responses == null) responses = new ArrayList<>();

				// Adds the current responses to the other question responses.
				responses.add(((Map<String, Object>)((List) values.get(i)).get(1)));
				// Sorts the responses by qual number.
				Collections.sort(responses, new ResponseComparator());
				// Adds the responses to the the team's responses.
				teamResponses.put(((String) ((List) values.get(i)).get(0)), responses);
				System.out.println("VALUE ADDED: " + ((List) values.get(i)).get(1));

				i++;

				// Checks if the team number changed.
				System.out.println("Bool: " + (keys.size() > i));
				System.out.println("i: " + i);
				System.out.println("Size: " + keys.size());

				if (keys.size() == i || currentTeamNumber != (int) keys.get(i)) {
					// Adds the responses to the team number.
					result.put(currentTeamNumber, teamResponses);
					// Updates the team number.
					currentTeamNumber = (int) keys.get(i - 1);
					// Resets the responses.
					teamResponses = new HashMap<>();
					System.out.println("Saved");
				}
			}

			System.out.println("Result Size: " + result.size());

			return result;
		}

		/**
		 * Compares two responses for the one with the greater qual match.
		 * {@inheritDoc}
		 */
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

	/**
	 * Does not work yet. Gets responses for team and a specific question. Sorted in order of qual.
	 * @param id The id of the question.
	 * @param teamNumber The team number.
	 */
	public AnswerList<Response> getTeamMatchQuestionResponses(String id, int teamNumber) {
		Query matchResponseQuery = this.matchResponseView.createQuery();
		AnswerList<Response> responses = new AnswerList<>();
		List<Object> keys = new ArrayList<>();
		keys.add(teamNumber);
		matchResponseQuery.setKeys(keys);
		QueryEnumerator result = null;
		try {
			result = matchResponseQuery.run();
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
		QueryRow aggregate = result.next();
		if (aggregate != null) {
			Map<Integer, Map<String, List<Map<String, Object>>>> value = (Map<Integer, Map<String, List<Map<String, Object>>>>) aggregate.getValue();
			if (!value.containsKey(teamNumber))
				return new AnswerList<>();
			Map<String, List<Map<String, Object>>> teamResponses = value.get(teamNumber);
			if (!teamResponses.containsKey(id))
				return new AnswerList<>();
			List<Map<String, Object>> questionResponses = teamResponses.get(id);
			for (Map<String, Object> responseMap : questionResponses) {
				responses.add(getResponseFromMap(responseMap));
			}
		}
		return responses;
	}
}
