package com.example.vanguard;

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

import com.example.vanguard.Questions.QuestionList;
import com.example.vanguard.Questions.QuestionTypes.DoubleQuestion;
import com.example.vanguard.Questions.QuestionTypes.StringQuestion;
import com.example.vanguard.Questions.QuestionViewers.QuestionListViewers.QuestionListEditViewer;
import com.example.vanguard.Questions.QuestionViewers.QuestionListViewers.QuestionListFormViewer;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


	public static float dpToPixels;
	LinearLayout mainLayout;
	QuestionList<Object> questions;
	QuestionListEditViewer questionEditor;
	QuestionListFormViewer questionViewer;
	boolean isEditingQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

		MainActivity.dpToPixels = this.getResources().getDisplayMetrics().density;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

		questions = new QuestionList<>();
		questions.add(new DoubleQuestion(this, "How many auto points?"));
		questions.add(new DoubleQuestion(this, "How many opcon points?"));
		questions.add(new DoubleQuestion(this, "How many gears scored?"));
		questions.add(new StringQuestion(this, "Comments?"));
		isEditingQuestions = true;

		questionViewer = new QuestionListFormViewer(this, questions);
//        System.out.println("FDLKDJ SFLK :DL F:SDLK JFLDK FDS JFLKSFJD F");
//
//        AsyncTesting async = new AsyncTesting();
//        System.out.println(async.execute());
//
//        CombinedChart chart = new CombinedChart(this);
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

        questionEditor = new QuestionListEditViewer(this, questions, menu);

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
				this.questions = this.questionEditor.getQuestions();
				this.mainLayout.removeView(this.questionEditor);
				this.questionViewer.setQuestions(this.questions);
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
