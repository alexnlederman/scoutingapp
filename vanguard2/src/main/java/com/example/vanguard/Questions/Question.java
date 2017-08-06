package com.example.vanguard.Questions;

import android.content.Context;
import android.view.View;

import com.example.vanguard.EnumName;
import com.example.vanguard.Pages.Activities.MainActivity;
import com.example.vanguard.Questions.QuestionTypes.BooleanQuestion;
import com.example.vanguard.Questions.QuestionTypes.IntegerQuestion;
import com.example.vanguard.Questions.QuestionTypes.MatchNumberQuestion;
import com.example.vanguard.Questions.QuestionTypes.MatchTeamNumberQuestion;
import com.example.vanguard.Questions.QuestionTypes.PitTeamNumberQuestion;
import com.example.vanguard.Questions.QuestionTypes.StringQuestion;
import com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers.SingleLineFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.FormQuestionViewers.TwoLineFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.QuestionEditorViewers.SingleLineEditQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.QuestionEditorViewers.TwoLineEditQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.SimpleFormQuestionViewer;
import com.example.vanguard.Questions.QuestionViewers.SimpleQuestionEditViewer;
import com.example.vanguard.Responses.Response;
import com.example.vanguard.Responses.SimpleResponse;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by BertTurtle on 6/1/2017.
 */

public abstract class Question<T extends Object> implements Label, Answer<T> {

	protected final static String QUESTION_TITLE_PROPERTY_NAME = "Question Title";
	protected final ViewStyle viewStyle;
	protected final QuestionType questionType;
	protected final boolean isEditable;
	protected final T defaultValue;
	protected final Map<String, Object> questionProperties;
	private final boolean isMatchQuestion;
	protected int matchNumber;
	protected int teamNumber;
	protected AnswerList<Response> responses;
	protected String id;
	protected boolean isPracticeMatchQuestion;

	public Question(String label, AnswerList<Response> responses, String id, boolean isMatchQuestion, ViewStyle viewStyle, QuestionType questionType, boolean isEditable, T defaultValue, Map<String, Object> properties) {
		this.matchNumber = 0;
		this.id = id;
		this.responses = responses;
		this.isMatchQuestion = isMatchQuestion;
		this.viewStyle = viewStyle;
		this.questionType = questionType;
		this.isEditable = isEditable;
		this.defaultValue = defaultValue;
		this.questionProperties = properties;

		this.questionProperties.put(QUESTION_TITLE_PROPERTY_NAME, label);
	}

	public Question(String label, String id, boolean isMatchQuestion, ViewStyle viewStyle, QuestionType questionType, boolean isEditable, T defaultValue, SortedMap<String, Object> properties) {
		this(label, new AnswerList<Response>(), id, isMatchQuestion, viewStyle, questionType, isEditable, defaultValue, properties);
	}

	public static Question createQuestionByType(QuestionType type, String label, AnswerList<Response> previousResponses, boolean isMatchQuestion, String id, Context context, Map<String, Object> questionProperties) {
		Question question = null;
		switch (type) {
			case INTEGER:
				question = new IntegerQuestion(context, label, previousResponses, id, questionProperties, isMatchQuestion);
				break;
			case STRING:
				question = new StringQuestion(context, label, previousResponses, id, isMatchQuestion);
				break;
			case BOOLEAN:
				question = new BooleanQuestion(context, label, previousResponses, id, isMatchQuestion);
				break;
			case MATCH_NUMBER:
				question = new MatchNumberQuestion(context, previousResponses, id);
				break;
			case MATCH_TEAM_NUMBER:
				question = new MatchTeamNumberQuestion(context, previousResponses, id);
				break;
			case PIT_TEAM_NUMBER:
				question = new PitTeamNumberQuestion(context, previousResponses, id);
				break;
		}
		return question;
	}

	public String getID() {
		return this.id;
	}

	@Override
	public String getLabel() {
		return (String) this.getQuestionProperties().get(QUESTION_TITLE_PROPERTY_NAME);
	}

	@Override
	public void setLabel(String label) {
		this.getQuestionProperties().get(QUESTION_TITLE_PROPERTY_NAME);
	}

	public String getQualifiedLabel() {
		if (this.isMatchQuestion) {
			return "Match Question: " + this.getLabel();
		}
		return "Pit Question: " + this.getLabel();
	}

	public void deleteResponse(Response response) {
		this.responses.remove(response);
	}

	public void resetResponses() {
		this.responses = new AnswerList<>();
	}

	public void saveResponse() {
		if (this.isMatchQuestion) {
			saveValue();
		} else if (this.hasValue()) {
			AnswerList<Response<T>> teamResponse = this.getTeamResponses(this.teamNumber, false);
			if (teamResponse.size() == 0) {
				this.saveValue();
			} else {
				for (Response response : this.getResponses()) {
					if (response.getTeamNumber() == this.teamNumber) {
						response.setValue(this.getValue());
					}
				}
			}
		} else {
			AnswerList<Response<T>> teamResponse = this.getTeamResponses(this.teamNumber, false);
			if (teamResponse.size() != 0) {
				for (Response response : this.getResponses()) {
					if (response.getTeamNumber() == this.teamNumber) {
						this.getResponses().remove(response);
					}
				}
			}
		}
	}

	public void addResponses(AnswerList<Response> responses) {
		this.responses.addAll(responses);
	}

	public void removeResponses(AnswerList<Response> responses) {
		this.responses.removeAll(responses);
	}

	private void saveValue() {
		responses.add(getCurrentResponse());
	}

	private SimpleResponse<T> getCurrentResponse() {
		return new SimpleResponse<T>(this.getValue(), this.getMatchNumber(), this.getTeamNumber(), this.isPracticeMatchQuestion(), MainActivity.databaseManager.getCurrentEventKey());
	}

	public AnswerList<Response<T>> getTeamResponses(int teamNumber, boolean includePracticeMatches) {
		AnswerList<Response<T>> responses = getCurrentEventResponses();
		AnswerList<Response<T>> teamResponses = new AnswerList<>();
		for (Response<T> response : responses) {
			if (response.getTeamNumber() == teamNumber && (includePracticeMatches || !response.isPracticeMatchResponse())) {
				teamResponses.add(response);
			}
		}
		return teamResponses;
	}

	public AnswerList<Response<T>> getMatchResponses(int matchNumber) {
		AnswerList<Response> responses = getResponses();
		AnswerList<Response<T>> matchResponses = new AnswerList<>();
		for (Response<T> response : responses) {
			if (response.getMatchNumber() == matchNumber) {
				matchResponses.add(response);
			}
		}
		return matchResponses;
	}

	public AnswerList<Response> getResponses() {
		return responses;
	}

	public AnswerList<Response<T>> getCurrentEventResponses() {
		AnswerList<Response<T>> currentEventResponses = new AnswerList<>();
		String currentEvent = MainActivity.databaseManager.getCurrentEventKey();
		for (Response<T> response : this.getResponses()) {
			if (response.getEventKey().equals(currentEvent)) {
				currentEventResponses.add(response);
			}
		}
		return currentEventResponses;
	}

	public abstract View getAnswerUI();

	// Includes practice matches by default.
	public List<Entry> getTeamEntryResponseEntries(int teamNumber) {
		List<Entry> entries = new ArrayList<>();
		AnswerList<Response<T>> responses = this.getTeamResponses(teamNumber, true);
		int practiceMatchOffset = -getGreatestPracticeMatchNumber(responses);
		for (Response response : responses) {
			float xValue;
			if (response.isPracticeMatchResponse())
				xValue = response.getMatchNumber() + practiceMatchOffset;
			else
				xValue = response.getMatchNumber();
			entries.add(new Entry(xValue, convertResponseToNumber(response), response));
		}
		Collections.sort(entries, new EntryXComparator());
		return entries;
	}

	public List<Float> getTeamResponseFloatValues(int teamNumber, boolean includePracticeMatches) {
		List<Float> responseValues = new ArrayList<>();
		AnswerList<Response<T>> responses = this.getTeamResponses(teamNumber, includePracticeMatches);
		for (Response<T> response : responses) {
			responseValues.add(this.convertResponseToNumber(response));
		}
		return responseValues;
	}

	public List<T> getTeamResponseValues(int teamNumber, boolean includePracticeMatches) {
		List<T> responseValues = new ArrayList<>();
		AnswerList<Response<T>> responses = this.getTeamResponses(teamNumber, includePracticeMatches);
		for (Response<T> response : responses) {
			responseValues.add(response.getValue());
		}
		return responseValues;
	}

	public int getGreatestPracticeMatchNumber(AnswerList<Response<T>> responses) {
		int matchNumber = 0;
		for (Response<T> response : responses) {
			if (response.isPracticeMatchResponse())
				if (response.getMatchNumber() > matchNumber)
					matchNumber = response.getMatchNumber();
		}
		return matchNumber;
	}

	// Does not include practice matches by default.
	public List<Entry> getAllTeamEntryResponseEntries(boolean includePracticeMatches) {
		List<Entry> entries = new ArrayList<>();
		List<Integer> teams = getEventTeams();
		List<AnswerList<Response<T>>> allResponses = getAllTeamResponses(includePracticeMatches);
		int i = 0;
		for (AnswerList<Response<T>> teamResponses : allResponses) {
			entries.add(new Entry(i, convertResponsesToNumber(teamResponses), teams.get(i)));
			i++;
		}
		return entries;
	}

	public List<Float> getAllTeamNumberValues() {
		List<AnswerList<Response<T>>> responses = getAllTeamResponses(false);
		List<Float> values = new ArrayList<>();
		for (AnswerList<Response<T>> teamResponses : responses) {
			values.add(convertResponsesToNumber(teamResponses));
		}
		return values;
	}

	private List<AnswerList<Response<T>>> getAllTeamResponses(boolean includePracticeMatches) {
		List<AnswerList<Response<T>>> responses = new ArrayList<>();
		List<Integer> teams = getEventTeams();

		for (int team : teams) {
			responses.add(this.getTeamResponses(team, includePracticeMatches));
		}

		return responses;
	}

	public List<T> getAllTeamResponseValues(boolean includePracticeMatches) {
		List<T> values = new ArrayList<>();
		for (Response<T> response : this.getResponses()) {
			values.add(response.getValue());
		}
		return values;
	}

	public List<Integer> getEventTeams() {
		return MainActivity.databaseManager.getCurrentEventTeams();
	}

	public abstract float convertResponseToNumber(Response<T> response);

	public float convertResponsesToNumber(AnswerList<Response<T>> responses) {
		if (responses.size() == 0)
			return 0;
		float total = 0;
		for (Response response : responses) {
			total += this.convertResponseToNumber(response);
		}
		return total / responses.size();
	}

	@Override
	public int getTeamNumber() {
		return teamNumber;
	}

	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
		if (!isMatchQuestion && !this.getQuestionType().equals(QuestionType.PIT_TEAM_NUMBER)) {
			AnswerList<Response<T>> responses = this.getTeamResponses(teamNumber, false);
			if (responses.size() == 0) {
				this.setValue(defaultValue);
			} else {
				this.setValue(responses.get(0).getValue());
			}
		}
	}

	@Override
	public int getMatchNumber() {
		return matchNumber;
	}

	public void setMatchNumber(int matchNumber) {
		this.matchNumber = matchNumber;
	}

	public ViewStyle getViewStyle() {
		return this.viewStyle;
	}

	public QuestionType getQuestionType() {
		return this.questionType;
	}

	public Boolean isEditable() {
		return this.isEditable;
	}

	public boolean hasValue() {
		T value = this.getValue();
		return value != defaultValue && !value.equals(defaultValue);
	}

	public boolean isPracticeMatchQuestion() {
		return isPracticeMatchQuestion;
	}

	public void setIsPracticeMatchQuestion(boolean isPracticeMatchQuestion) {
		this.isPracticeMatchQuestion = isPracticeMatchQuestion;
	}

	public SimpleFormQuestionViewer getQuestionViewer(Context context) {
		SimpleFormQuestionViewer questionViewer = null;
		switch (this.getViewStyle()) {
			case SINGLE_LINE:
				questionViewer = new SingleLineFormQuestionViewer(context, this);
				break;
			case TWO_LINE:
				questionViewer = new TwoLineFormQuestionViewer(context, this);
				break;
		}
		return questionViewer;
	}

	public boolean isMatchQuestion() {
		return this.isMatchQuestion;
	}

	public SimpleQuestionEditViewer getQuestionEditViewer(Context context) {
		SimpleQuestionEditViewer questionViewer = null;
		switch (this.getViewStyle()) {
			case SINGLE_LINE:
				questionViewer = new SingleLineEditQuestionViewer(context, this);
				break;
			case TWO_LINE:
				questionViewer = new TwoLineEditQuestionViewer(context, this);
				break;
		}
		return questionViewer;
	}

	public Map<String, Object> getQuestionProperties() {
		return this.questionProperties;
	}

	/**
	 * Possible types of questions.
	 */
	public enum QuestionType implements EnumName {
		INTEGER("Integer"),
		STRING("String"),
		BOOLEAN("Boolean"),
		MATCH_NUMBER("Match Number"),
		MATCH_TEAM_NUMBER("Match Team Number"),
		PIT_TEAM_NUMBER("Pit Team Number");


		private final String label;

		QuestionType(final String label) {
			this.label = label;
		}

		public static QuestionType valueOfName(String name) {
			for (QuestionType type : QuestionType.values())
				if (type.getName().equals(name))
					return type;
			return null;
		}

		@Override
		public String getName() {
			return this.label;
		}
	}

	public enum ViewStyle {
		SINGLE_LINE,
		TWO_LINE
	}
}
