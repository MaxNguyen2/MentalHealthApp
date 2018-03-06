package com.example.max.mentalhealthapp;

import android.app.AlarmManager;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MoodGraphs extends MoodMonitoring {

    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_graphs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //sets up navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupDrawer();
        mDrawerList = (ListView) findViewById(R.id.navList);
        addDrawerItems();

        //sets status bar color to be dark orange
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.StatusOrange));

        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems("Happy", "Sad", "Energized", "Irritated", "Anxious");

        graph = (GraphView) findViewById(R.id.graph);
        GridLabelRenderer styleGraph = graph.getGridLabelRenderer();
        styleGraph.setVerticalLabelsVisible(true);
        styleGraph.setLabelVerticalWidth(25);
        styleGraph.setLabelHorizontalHeight(25);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data());

        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(7);

        // enable scaling and scrolling
        graph.getViewport().setScrollable(true);
        graph.addSeries(series);
        series.setDrawDataPoints(true);

        // set date label formatter
        styleGraph.setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        styleGraph.setNumHorizontalLabels(5); // only 4 because of the space
        styleGraph.setHumanRounding(false);

    }
    public DataPoint[] data() {
        SharedPreferences mPrefs = getSharedPreferences("key", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("moodArray", "");
        ArrayList<MoodReport> obj = gson.fromJson(json, new TypeToken<ArrayList<MoodReport>>() {
        }.getType());
        Collections.sort(obj);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(obj.get(obj.size()-1).getDateObj().getTime()- 2* AlarmManager.INTERVAL_DAY);
        graph.getViewport().setMaxX(obj.get(obj.size()-1).getDateObj().getTime() + 2 * AlarmManager.INTERVAL_DAY);

        int n = obj.size(); //to find out the no. of data-points
        DataPoint[] values = new DataPoint[n]; //creating an object of type
        for(int i=0; i<n; i++){
            DataPoint v = new
                    DataPoint(obj.get(i).getDateObj(),obj.get(i).getHappy());
            values[i] = v;
        }
        return values;

    }
}
