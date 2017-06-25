package com.example.vanguard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.UnsavedRevision;
import com.couchbase.lite.View;
import com.couchbase.lite.android.AndroidContext;
import com.example.vanguard.Questions.QuestionViewers.MatchScoutQuestionList;
import com.example.vanguard.Questions.QuestionViewers.QuestionListViewers.QuestionListEditViewer;
import com.example.vanguard.Questions.QuestionViewers.QuestionListViewers.QuestionListFormViewer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


	public static int questionCount;
	public static float dpToPixels;
	LinearLayout mainLayout;
	MatchScoutQuestionList questions;
	QuestionListEditViewer questionEditor;
	QuestionListFormViewer questionViewer;
	boolean isEditingQuestions;
	DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//		SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
//		questionCount = sharedPreferences.getInt(getString(R.string.question_count), 0);

		MainActivity.dpToPixels = this.getResources().getDisplayMetrics().density;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




//		Manager manager = null;
//		try {
//			manager = new Manager(new AndroidContext(getApplicationContext()), Manager.DEFAULT_OPTIONS);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		Database database = null;
//		try {
//			database = manager.getDatabase("app");
//		} catch (CouchbaseLiteException e) {
//			e.printStackTrace();
//		}
//
//		SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
//		String docId = sharedPreferences.getString(getString(R.string.doc_id), null);
//		docId = null;
//		System.out.println("Doc Id: " + docId);
//		if (docId == null) {
//
//			this.document = database.createDocument();
//			try {
//				this.document.putProperties(new HashMap<String, Object>());
//			} catch (CouchbaseLiteException e) {
//				e.printStackTrace();
//			}
//			docId = this.document.getId();
//			SharedPreferences.Editor editor = sharedPreferences.edit();
//			editor.putString(getString(R.string.doc_id), docId);
//			editor.commit();
//		}
//		else {
//			this.document = database.getDocument(docId);
//		}

//		questions = new MatchScoutQuestionList(this, this.document, "key");
////		questions.add(new IntegerQuestion(this, "How many auto points?"));
////		questions.add(new IntegerQuestion(this, "How many opcon points?"));
////		questions.add(new IntegerQuestion(this, "How many gears scored?"));
////		questions.add(new StringQuestion(this, "Comments?"));
//		isEditingQuestions = true;
//
////        System.out.println("FDLKDJ SFLK :DL F:SDLK JFLDK FDS JFLKSFJD F");
////
////        AsyncTesting async = new AsyncTesting();
////        System.out.println(async.execute());
////
////        CombinedChart chart = new CombinedChart(this);
//
//		questionViewer = new QuestionListFormViewer(this, questions);
//
////		Map<String, Object> properties = new HashMap<>();
////		Document response1 = database.createDocument();
////		Document response2 = database.createDocument();
////		Document[] array = {response1, response2};
////		String[] array2 = {"Bye", "Goodbye", "Adios"};
////		properties.put("Introduction", array);
////		Map<String, Object> prop2 = new HashMap<>();
////		prop2.put("Leaving", array2);
////
////		Document document = database.createDocument();
////		database.getDocument();
////
////
////		try {
////			response1.putProperties(prop2);
////			document.putProperties(properties);
////		} catch (CouchbaseLiteException e) {
////			e.printStackTrace();
////		}
////
////		System.out.println(document.getProperty("Introduction"));
////		System.out.println(document.getProperty("Leaving"));
////		System.out.println();
////		Document[] documents = (Document[]) document.getProperty("Introduction");
////		System.out.println(documents[0].getProperty("Leaving"));
//
//		Document pit_quest1 = database.getDocument("pit_quest1");
//		Document pit_quest2 = database.getDocument("pit_quest2");
//		Document match_quest1 = database.getDocument("match_quest1");
//		Document match_quest2 = database.getDocument("match_quest2");
//		Document match_quest3 = database.getDocument("match_quest3");
//
//		try {
//			pit_quest1.update(new PitInitUpdate());
//			pit_quest2.update(new PitInitUpdate());
//			match_quest1.update(new MatchInitUpdate());
//			match_quest2.update(new MatchInitUpdate());
//			match_quest3.update(new MatchInitUpdate());
//		} catch (CouchbaseLiteException e) {
//			e.printStackTrace();
//		}
//
//
//		View match_view = database.getView("match_view");
//		match_view.setMap(new Mapper() {
//			@Override
//			public void map(Map<String, Object> map, Emitter emitter) {
//				System.out.println(map.get("type"));
//				System.out.println(map.values());
//				for (String string : map.keySet()) {
//					System.out.println(string);
//				}
//				for (Object string : map.values()) {
//					System.out.println(string);
//				}
//				if (map.containsKey("type")) {
//					if (map.get("type").equals("match_quest")) {
//						emitter.emit(map.get("index"), map);
//					}
//				}
//			}
//		}, "1");
//
////		QueryOptions opt = new QueryOptions();
//		Query query = null;
////		Map<String, Object> map = null;
////		try {
////			map = database.getAllDocs(opt);
////		} catch (CouchbaseLiteException e) {
////			e.printStackTrace();
////		}
////		for (Object string : map.values()) {
////			System.out.println(string);
////		}
////		for (Object string : map.keySet()) {
////			System.out.println(string);
////		}
//
//		try {
//			query = match_view.createQuery();
//			QueryEnumerator result = query.run();
//			for (Iterator<QueryRow> it = result; it.hasNext();) {
//				QueryRow row = it.next();
//				System.out.println(row.getKey());
//				System.out.println(row.getValue());
//			}
//
//		} catch (CouchbaseLiteException e) {
//			e.printStackTrace();
//		}

		this.databaseManager = new DatabaseManager(this);

		questions = new MatchScoutQuestionList(this.databaseManager, this);
		isEditingQuestions = true;
		questionViewer = new QuestionListFormViewer(this, this.databaseManager);
	}

	public class PitInitUpdate implements Document.DocumentUpdater {

		@Override
		public boolean update(UnsavedRevision unsavedRevision) {
			Map<String, Object> prop = unsavedRevision.getUserProperties();
			prop.put("title", "Hello");
			prop.put("value", (new Random()).nextInt());
			prop.put("type", "pit_quest");
			unsavedRevision.setUserProperties(prop);
			return true;
		}
	}

	public class MatchInitUpdate implements Document.DocumentUpdater {

		@Override
		public boolean update(UnsavedRevision unsavedRevision) {
			Map<String, Object> prop = unsavedRevision.getUserProperties();
			prop.put("title", "Hello2");
			prop.put("value", (new Random()).nextInt());
			prop.put("type", "match_quest");
			unsavedRevision.setUserProperties(prop);
			return true;
		}
	}

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        mainLayout = (LinearLayout) findViewById(R.id.true_main_layout);
//        layout.addView(chart);
//        List<BarEntry> entries = new ArrayList<>();
//        entries.add(new BarEntry(0f, 20f));
//        entries.add(new BarEntry(1f, 10f));
//        entries.add(new BarEntry(2f, 40f));
//        entries.add(new BarEntry(3f, 30f));
//        List<Entry> enter = new ArrayList<>();
//        enter.add(new Entry(0f, 15f));
//        enter.add(new Entry(1f, 25f));
//        enter.add(new Entry(2f, 35f));
//        enter.add(new Entry(3f, 35f));
//
//        LineData lineData = new LineData(new LineDataSet(enter, "Line Data"));
//        BarData barData = new BarData(new BarDataSet(entries, "Testing Data"));
//
//        CombinedData data = new CombinedData();
//        data.setData(lineData);
//        data.setData(barData);
//        chart.setData(data);

        questionEditor = new QuestionListEditViewer(this, menu, this.databaseManager, true);

        mainLayout.addView(questionEditor);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
			if (isEditingQuestions) {
				item.setTitle("Edit");
				this.mainLayout.removeView(this.questionEditor);
				this.questionViewer.prepareQuestions();
				this.mainLayout.addView(this.questionViewer);
				isEditingQuestions = false;
			}
			else {
				item.setTitle("Save");
				this.mainLayout.removeView(questionViewer);
				this.mainLayout.addView(questionEditor);
				isEditingQuestions = true;
			}
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_data) {
            // Handle the camera action
        } else if (id == R.id.nav_schedule) {

        } else if (id == R.id.nav_scout) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_sync) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
