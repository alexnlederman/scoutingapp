package com.example.vanguard;

import android.content.Context;
import android.os.AsyncTask;

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
import com.example.vanguard.Questions.AnswerList;
import com.example.vanguard.Questions.Question;
import com.example.vanguard.Questions.QuestionTypes.IntegerQuestion;
import com.example.vanguard.Questions.QuestionTypes.MatchNumberQuestion;
import com.example.vanguard.Questions.QuestionTypes.StringQuestion;
import com.example.vanguard.Questions.QuestionTypes.TeamNumberQuestion;
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

	private final String response_value_key = "response_value";
	private final String response_match_number_key = "match_number";
	private final String response_team_number_key = "team_number";

	private final String event_name_key = "event_name";
	public static final String event_match_number_key = "event_match_number";
	public static final String event_match_blue_team = "event_match_blue_team";
	public static final String event_match_red_team = "event_match_red_team";
	private final String event_match_document_type = "event_match_document";

	private final String event_teams_key = "event_teams";
	private final String event_teams_document_type = "event_teams_document";

	private final String document_type_key = "document_type";


	private final String match_questions_view = "match_questions";
	private final String pit_questions_view = "pit_questions";
	private final String match_response_view = "match_responses";
	private final String event_view = "event_view";
	private final String event_teams_view = "event_teams_view";

	// Views to help find specific documents.

	// Finds all of the match questions.
	View matchQuestionView;
	// Finds all of the pit questions.
	View pitQuestionView;
	// Finds all of the responses for the pit questions and compiles them into a more accesible state.
	// Hashmap of team numbers which go to a hashmap of their responses sorted by qual number, with the key being the response's question's id.
	// TODO make this compile it to a more accesible state.
	View matchResponseView;

	// Finds the events the person is scouting.
	View eventMatchesView;

	View eventTeamsView;

	Database database;

	Context context;


	/**
	 * Possible types of questions.
	 * TODO add more question types.
	 */
	public enum QuestionTypes {
		INTEGER("INTEGER"),
		STRING("STRING"),
		MATCH_NUMBER("MATCH_NUMBER"),
		TEAM_NUMBER("TEAM_NUMBER");

		private final String text;

		QuestionTypes(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return this.text;
		}
	}


	/**
	 * Creates a new DatabaseManager. This sets up the whole database.
	 * @param context the application context.
	 */
	public DatabaseManager(Context context) {
		this.context = context;
		Manager manager;
		try {
			// Finds the database.
			manager = new Manager(new AndroidContext(this.context.getApplicationContext()), Manager.DEFAULT_OPTIONS);
			// Can change string below to reset the database.
			this.database = manager.getDatabase("app22");

			// Creates database views.
			matchQuestionView = this.database.getView(match_questions_view);
			matchQuestionView.setMap(new MatchQuestionMap(), "1");
			pitQuestionView = this.database.getView(pit_questions_view);
			pitQuestionView.setMap(new PitQuestionMap(), "1");
			matchResponseView = this.database.getView(match_response_view);
			matchResponseView.setMapReduce(new MatchResponsesMap(), new MatchResponsesMap(), "2");
			eventMatchesView = this.database.getView(event_view);
			eventMatchesView.setMap(new EventMatchesMapper(), "2");
			eventTeamsView = this.database.getView(event_teams_view);
			eventTeamsView.setMap(new EventTeamsMapper(), "4");

		} catch (CouchbaseLiteException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new question in the database.
	 * @param label The text for the question.
	 * @param questionType The type of question. Should be a {@link QuestionTypes} enum.
	 * @param isMatchQuestion Whether the question is used for match or pit scouting.
	 * @return The {@link Question} object that was created.
	 */
	public Question createQuestion(String label, String questionType, boolean isMatchQuestion) {

		// Finds the index of the question.
		int index;
		if (isMatchQuestion) {
			index = this.matchQuestionView.getTotalRows();
		}
		else {
			index = this.pitQuestionView.getTotalRows();
		}

		// Creates a new document for question.
		Document document = this.database.createDocument();

		// Adds values to document.
		try {
			document.update(new QuestionCreator(label, index, questionType, isMatchQuestion));
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

	/**
	 * Gets hashmaps of the responses for the question.
	 * @param question
	 * @return
	 */
	private List<HashMap<String, Object>> getQuestionResponseHashmaps(Question question) {
		// Gets question responses.
		ResponseList responses = question.getResponses();

		List<HashMap<String, Object>> responseHashMaps = new ArrayList<>();

		// Loops through the current responses and adds the proper values to them.
		for (Response<Object> response : responses) {
			HashMap<String, Object> responseHashMap = new HashMap<>();
			responseHashMap.put(response_value_key, response.getValue());
			responseHashMap.put(response_match_number_key, response.getMatchNumber());
			responseHashMap.put(response_team_number_key, response.getTeamNumber());
			responseHashMaps.add(responseHashMap);
		}
		return responseHashMaps;
	}

	/**
	 * Deletes a question.
	 * @param question the question to delete.
	 */
	public void deleteQuestion(Question question) {
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
						// TODO fix bug with this deleting incorrect question after being swapped.
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
			questions.add(createQuestion("Team Number", QuestionTypes.TEAM_NUMBER.toString(), false));
		}
		return questions;
	}

	/**
	 * @return An {@link AnswerList} of match questions.
	 */
	public AnswerList<Question> getMatchQuestions() {
		AnswerList<Question> questions = createQuestionListFromQuery(this.matchQuestionView.createQuery());
		if (questions.size() == 0) {
			questions.add(createQuestion("Match Number", QuestionTypes.MATCH_NUMBER.toString(), true));
			questions.add(createQuestion("Team Number", QuestionTypes.TEAM_NUMBER.toString(), true));
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
		Question question = null;
		// Checks which question type it correspondse to and creates a Question from that.
		boolean isMatchQuestion = map.get(question_type_key).equals(match_question_type);
		switch (QuestionTypes.valueOf((String) map.get(question_type_key))) {
			case INTEGER:
				question = new IntegerQuestion(this.context, (String) map.get(question_label_key), getQuestionMapResponses(map), (String) map.get("_id"), isMatchQuestion);
				break;
			case STRING:
				question = new StringQuestion(this.context, (String) map.get(question_label_key), getQuestionMapResponses(map), (String) map.get("_id"), isMatchQuestion);
				break;
			case MATCH_NUMBER:
				question = new MatchNumberQuestion(this.context, getQuestionMapResponses(map), (String) map.get("_id"));
				break;
			case TEAM_NUMBER:
				question = new TeamNumberQuestion(this.context, getQuestionMapResponses(map), (String) map.get("_id"), isMatchQuestion);
				break;
		}
		return question;
	}

	/**
	 * Gets list of responses from a question Map.
	 * @param questionMap The question Map.
	 * @return The responses for the question.
	 */
	private ResponseList getQuestionMapResponses(Map<String, Object> questionMap) {
		// Gets list of questions Maps.
		ArrayList<Map<String, Object>> responseMaps = (ArrayList<Map<String, Object>>) questionMap.get(question_responses_key);
		ResponseList responses = new ResponseList();
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

		public QuestionCreator(String label, int index, String questionType, boolean isMatchQuestion) {
			this.label = label;
			this.index = index;
			this.questionType = questionType;
			this.isMatchQuestion = isMatchQuestion;
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
			newRevision.setProperties(map);
			return true;
		}
	}

	public void addEvent(String eventName) {
		if (getEventTeams(eventName).size() != 0) {
			System.out.println("Already Added: " + getEventMatches(eventName).size());
			return;
		}
		System.out.println("Event Match Count: " + getEventMatches(eventName).size());
		GetEventMatches asyncTask = new GetEventMatches();
		asyncTask.execute(eventName);
	}

	private class GetEventMatches extends AsyncTask<String, Boolean, Boolean> {

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
						matchDocument.update(new EventMatchDocumentUpdater(qualNumber, params[0], redTeams, blueTeams));
						System.out.println("Saved Document: " + i);
					}
					else {
						System.out.println("No Good: " + matchJson.getString("comp_level"));
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
				documument.update(new EventTeamsDocumentUpdater(params[0], eventTeams));
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			return true;
		}

		private String getUrlValue(URL url) {
			String input = "";
			try {
				HttpURLConnection blueAllianceConnection = (HttpURLConnection) url.openConnection();

				BufferedReader in = new BufferedReader(new InputStreamReader(blueAllianceConnection.getInputStream()));
				String currentLine = in.readLine();
				while (currentLine != null) {
					input += currentLine + "\n";
					currentLine = in.readLine();
				}
				in.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return input;
		}
	}

	private String[] getAllianceTeams(JSONObject allianceJson) throws JSONException {
		JSONArray teamKeysJson = allianceJson.getJSONArray("team_keys");
		String[] teamKeys = new String[3];
		for (int i = 0; i < teamKeysJson.length(); i++) {
			teamKeys[i] = teamKeysJson.getString(i);
		}
		return teamKeys;
	}

	// TODO
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

	public class EventMatchDocumentUpdater implements Document.DocumentUpdater {

		int matchNumber;
		String eventName;
		String[] redTeam;
		String[] blueTeam;

		public EventMatchDocumentUpdater(int matchNumber, String eventName, String[] redTeam, String[] blueTeam) {
			this.matchNumber = matchNumber;
			this.eventName = eventName;
			this.redTeam = redTeam;
			this.blueTeam = blueTeam;
		}

		@Override
		public boolean update(UnsavedRevision newRevision) {
			Map<String, Object> properties = newRevision.getProperties();
			properties.put(event_name_key, this.eventName);
			properties.put(event_match_number_key, this.matchNumber);
			properties.put(event_match_blue_team, this.blueTeam);
			properties.put(event_match_red_team, this.redTeam);
			properties.put(document_type_key, event_match_document_type);
			newRevision.setProperties(properties);
			return true;
		}
	}

	public class EventTeamsDocumentUpdater implements Document.DocumentUpdater {

		String eventName;
		List<Integer> teams;

		public EventTeamsDocumentUpdater(String eventName, List<Integer> teams) {
			this.eventName = eventName;
			this.teams = teams;
		}

		@Override
		public boolean update(UnsavedRevision newRevision) {
			Map<String, Object> properties = newRevision.getProperties();
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

			List<Integer> teamNumbers = new ArrayList<>((LazyJsonArray<Integer>) row.getValue());

			System.out.println("Class: " + teamNumbers.getClass());

			Collections.sort(teamNumbers);
			return teamNumbers;
		}
		return new ArrayList<>();
	}

	public class EventTeamsMapper implements Mapper {

		@Override
		public void map(Map<String, Object> document, Emitter emitter) {
			if (document.containsKey(document_type_key) && document.get(document_type_key).equals(event_teams_document_type)) {
				emitter.emit(document.get(event_name_key), document.get(event_teams_key));
			}
		}
	}

	public class EventMatchesMapper implements Mapper {

		@Override
		public void map(Map<String, Object> document, Emitter emitter) {
			System.out.println(document.get(document_type_key));
			if (document.containsKey(document_type_key) && document.get(document_type_key).equals(event_match_document_type)) {
				emitter.emit(document.get(event_match_number_key), new Object[]{document.get(event_match_red_team), document.get(event_match_blue_team), document.get(event_name_key)});
			}
		}
	}

	/**
	 * Finds all match questions.
	 * Emits the index as the key and Map as the value.
	 * {@inheritDoc}
	 */
	public class MatchQuestionMap implements Mapper {

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
	public class PitQuestionMap implements Mapper {

		@Override
		public void map(Map<String, Object> document, Emitter emitter) {
			if (document.containsKey(document_type_key) && document.get(document_type_key).equals(pit_question_type)) {
				emitter.emit(document.get(question_index_key), document);
			}
		}
	}

	private SimpleResponse getResponseFromMap(Map<String, Object> map) {
		return new SimpleResponse(map.get(response_value_key), (int) map.get(response_match_number_key), (int) map.get(response_team_number_key));
	}

	/**
	 * Gets all the responses stored by the match questions.
	 * Sorted by Hashmap of team numbers which go to a hashmap of their responses sorted by qual number, with the key being the response's question's id.
	 * {@inheritDoc}
	 */
	public class MatchResponsesMap implements Mapper, Reducer {

		@Override
		public void map(Map<String, Object> document, Emitter emitter) {
			// Gets proper documents.
			if (document.containsKey(document_type_key) && document.get(document_type_key).equals(match_question_type)) {
				// Gets the responses.
				ArrayList<Map<String, Object>> responses = (ArrayList<Map<String, Object>>) document.get(question_responses_key);
				for (Map<String, Object> response : responses) {
					// Emits the responses by team number.
					emitter.emit(response.get(response_team_number_key), new Object[]{document.get("_id"), response});
				}
			}
		}

		@Override
		public Object reduce(List<Object> keys, List<Object> values, boolean rereduce) {
			int currentTeamNumber = (int) keys.get(0);

			Map<Integer, Map<String, List<Map<String, Object>>>> result = new HashMap<>();

			Map<String, List<Map<String, Object>>> teamResponses = new HashMap<>();

			// Goes through the team numbers and the responses.
			for (int i = 0; i < keys.size(); i++) {
				// Checks if the team number changed.
				if (currentTeamNumber != (int) keys.get(i)) {
					// TODO sort team responses by qual.
					// Adds the responses to the team number.
					result.put(currentTeamNumber, teamResponses);
					// Updates the team number.
					currentTeamNumber = (int) keys.get(i);
					// Resets the responses.
					teamResponses = new HashMap<>();
				}

				// Gets the responses for current response question.
				List<Map<String, Object>> responses = teamResponses.get(((Object[]) values.get(i))[0]);
				if (responses == null) responses = new ArrayList<>();

				// Adds the current responses to the other question responses.
				responses.add(((Map<String, Object>)((Object[]) values.get(i))[1]));
				// Sorts the responses by qual number.
				Collections.sort(responses, new ResponseComparator());
				// Adds the responses to the the team's responses.
				teamResponses.put(((String) ((Object[]) values.get(i))[0]), responses);
			}

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
	public ResponseList getTeamMatchQuestionResponses(String id, int teamNumber) {
		// TODO
		Query matchResponseQuery = this.matchResponseView.createQuery();
		ResponseList responses = new ResponseList();
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
		Map<Integer, Map<String, List<Map<String, Object>>>> value = (Map<Integer, Map<String, List<Map<String, Object>>>>) aggregate.getValue();
		if (!value.containsKey(teamNumber))
			return new ResponseList();
		Map<String, List<Map<String, Object>>> teamResponses = value.get(teamNumber);
		if (!teamResponses.containsKey(id))
			return new ResponseList();
		List<Map<String, Object>> questionResponses = teamResponses.get(id);
		for (Map<String, Object> responseMap : questionResponses) {
			responses.add(getResponseFromMap(responseMap));
		}
		return responses;
	}
}
