package com.example.max.mentalhealthapp;

import android.app.AlarmManager;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;


public class MoodGraphs extends MoodMonitoring implements ReportDialog.ReportDialogListener {

    GraphView graph;
    MaterialSpinner spinner;
    ArrayList<MoodReport> moodArray;
    SharedPreferences prefs;

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

        prefs = this.getSharedPreferences("key", Context.MODE_PRIVATE);
        final SharedPreferences.Editor prefsEdit = prefs.edit();
        updateMoodArray();

        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems("Happy", "Sad", "Energized", "Irritated", "Anxious");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                prefsEdit.putString("spinnerText",item).commit();
                formatSeries();
                formatListView();
            }
        });

        graph = (GraphView) findViewById(R.id.graph);
        GridLabelRenderer styleGraph = graph.getGridLabelRenderer();
        styleGraph.setVerticalLabelsVisible(false);

        // set manual bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinY(1);
        graph.getViewport().setMaxY(7);

        // enable scaling and scrolling
        formatSeries();
        graph.getViewport().setScrollable(true);
        prefsEdit.putString("spinnerText","Happy").commit();
        formatListView();


        // set date label formatter
        styleGraph.setNumHorizontalLabels(5); // only 4 because of the space
        styleGraph.setNumVerticalLabels(7);
        styleGraph.setHumanRounding(false);
        styleGraph.setLabelsSpace(10);
        styleGraph.setPadding(40);
        styleGraph.setGridColor(Color.rgb(220,220,220));
        styleGraph.setLabelFormatter(new DateAsXAxisLabelFormatter(this) {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return super.formatLabel(value, true).substring(0,super.formatLabel(value, true).length()-3);
                } else {
                    return super.formatLabel(value, false);
                }
            }
        });
    }
    public DataPoint[] data() {
        if (moodArray.size()!= 0) {
        int n = moodArray.size(); //to find out the no. of data-points
        Calendar cal = Calendar.getInstance();
        cal.setTime(moodArray.get(n-1).getDateObj());
        cal.set(Calendar.HOUR,0);
        graph.getViewport().setMaxX(moodArray.get(n-1).getDateObj().getTime());
        graph.getViewport().setMinX(cal.getTimeInMillis() - 4 * AlarmManager.INTERVAL_DAY);

        DataPoint[] values = new DataPoint[n]; //creating an object of type
        DataPoint v;

        switch (spinner.getText().toString()) {
            case "Happy":
                for(int i=0; i<n; i++) {
                    v = new DataPoint(moodArray.get(i).getDateObj(), moodArray.get(i).getHappy());
                    values[i] = v;
                }
                break;
            case "Sad":
                for(int i=0; i<n; i++) {
                    v = new DataPoint(moodArray.get(i).getDateObj(), moodArray.get(i).getSad());
                    values[i] = v;
                }
                break;
            case "Energized":
                for(int i=0; i<n; i++) {
                    v = new DataPoint(moodArray.get(i).getDateObj(), moodArray.get(i).getEnergy());
                    values[i] = v;
                }
                break;
            case "Irritated":
                for(int i=0; i<n; i++) {
                    v = new DataPoint(moodArray.get(i).getDateObj(), moodArray.get(i).getIrritated());
                    values[i] = v;
                }
                break;
            case "Anxious":
                for(int i=0; i<n; i++) {
                    v = new DataPoint(moodArray.get(i).getDateObj(), moodArray.get(i).getIrritated());
                    values[i] = v;
                }
                break;
        }
        return values; }
        else
            return new DataPoint[0];
    }

    public void formatSeries() {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data());
            graph.removeAllSeries();
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(20);
            series.setThickness(7);

            switch (spinner.getText().toString()) {
                case "Happy":
                    series.setColor(Color.rgb(255, 193, 7));
                    break;
                case "Sad":
                    series.setColor(Color.rgb(63, 81, 181));
                    break;
                case "Energized":
                    series.setColor(Color.rgb(5, 151, 138));
                    break;
                case "Irritated":
                    series.setColor(Color.rgb(244, 67, 54));
                    break;
                case "Anxious":
                    series.setColor(Color.rgb(156, 39, 176));
                    break;
            }
            graph.addSeries(series);
    }

    public void formatListView() {
            // Create the adapter to convert the array to views
            MoodReportAdapter adapter = new MoodReportAdapter(this, moodArray);
            // Attach the adapter to a ListView
            ListView listView = (ListView) findViewById(R.id.reportList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    prefs.edit().putInt("listPosition", position).commit();
                    if (moodArray.size() != 0)
                    showReportDialog();
                }
            });
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the ReportDialogFragment.ReportDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        updateMoodArray();
            formatListView();
            formatSeries();
    }

    public void showReportDialog() {
        DialogFragment newFragment = new ReportDialog();
        newFragment.show(getSupportFragmentManager(), "mood");
    }

    public void updateMoodArray() {
        moodArray = new Gson().fromJson(prefs.getString("moodArray", ""), new TypeToken<ArrayList<MoodReport>>() {
        }.getType());
    }
}
