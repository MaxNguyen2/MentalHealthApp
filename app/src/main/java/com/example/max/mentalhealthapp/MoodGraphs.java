package com.example.max.mentalhealthapp;

import android.app.AlarmManager;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//Displays mood data in graphs and past mood reports in a list
public class MoodGraphs extends SetupClass implements ReportDialog.ReportDialogListener{
    LineChart graph;
    MaterialSpinner spinner;
    ArrayList<MoodReport> moodArray;
    SharedPreferences prefs;
    long referenceTimestamp;
    LineDataSet dataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mood_graphs);
        super.onCreate(savedInstanceState);
        Log.d("DEBUGGING","STARTING ACTIVITY");
        setStatusBar(R.color.StatusOrange);

        prefs = this.getSharedPreferences("key", Context.MODE_PRIVATE);
        final SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.putString("spinnerText","Happy").commit(); //stores string so that graph displays happy mood data by default
        updateMoodArray(); //updates array of mood reports from SharedPreferences
        formatListView();

        //sets up dropdown menu for selecting which mood data set to graph
        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems("Happy", "Sad", "Energized", "Irritated", "Anxious");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) { //updates what is displayed in graph and list
                prefsEdit.putString("spinnerText",item).commit();
                formatListView();
                updateGraph();
                Log.d("DEBUGGING","SPINNER SELECTED");
            }
        });

        //setting up the graph
        graph = (LineChart) findViewById(R.id.graph);
        formatGraph();
    }

    //returns array with data points to graph based on what user selects to graph
    public List<Entry> data() {
        Log.d("DEBUGGING","SETTING GRAPH DATA");
        if (moodArray.size()!= 0) { //makes sure array of mood reports is not empty
            int n = moodArray.size(); //to find out the no. of data-points
            referenceTimestamp = moodArray.get(0).getDateObj().getTime();
            List<Entry> entries = new ArrayList<>();
            //fills array with correct data based on what is selected in the dropdown
            switch (spinner.getText().toString()) {
                case "Happy":
                    for(int i=0; i<n; i++) {
                        entries.add(new Entry(moodArray.get(i).getDateObj().getTime()-referenceTimestamp, (float) moodArray.get(i).getHappy()));
                    }
                    break;
                case "Sad":
                    for(int i=0; i<n; i++) {
                        entries.add(new Entry(moodArray.get(i).getDateObj().getTime()-referenceTimestamp, (float) moodArray.get(i).getSad()));
                    }
                    break;
                case "Energized":
                    for(int i=0; i<n; i++) {
                        entries.add(new Entry(moodArray.get(i).getDateObj().getTime()-referenceTimestamp, (float) moodArray.get(i).getEnergy()));
                    }
                    break;
                case "Irritated":
                    for(int i=0; i<n; i++) {
                        entries.add(new Entry(moodArray.get(i).getDateObj().getTime()-referenceTimestamp, (float) moodArray.get(i).getIrritated()));
                    }
                    break;
                case "Anxious":
                    for(int i=0; i<n; i++) {
                        entries.add(new Entry(moodArray.get(i).getDateObj().getTime()-referenceTimestamp, (float) moodArray.get(i).getAnxious()));
                    }
                    break;
            }
            return entries;
        }
        else
            return new ArrayList<>(); //returns empty array if there are no mood reports
    }

    public void formatListView() {
        //create the adapter to convert the array to views
        MoodReportAdapter adapter = new MoodReportAdapter(this, moodArray);
        ListView listView = (ListView) findViewById(R.id.reportList);
        //attach the adapter to a ListView
        listView.setAdapter(adapter);
        updateMoodArray();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prefs.edit().putInt("listPosition", position).commit();
                if (moodArray.size() != 0)
                    showReportDialog(); //opens dialog when the item is clicked with mood report information
            }
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        //user touched the dialog's positive button
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //user touched the dialog's cancel button
        dialog.dismiss();
        this.recreate();
    }

    //shows dialog with info of mood report
    public void showReportDialog() {
        DialogFragment newFragment = new ReportDialog();
        newFragment.show(getSupportFragmentManager(), "mood");
    }

    //updates array of mood reports from SharedPreferences
    public void updateMoodArray() {
        if (prefs.getString("moodArray","").equals(""))
            moodArray = new ArrayList<>();
        else
            moodArray = new Gson().fromJson(prefs.getString("moodArray", ""), new TypeToken<ArrayList<MoodReport>>() {
        }.getType());
    }

    //updates data shown in graph to be what is selected in dropdown menu
    public void updateGraph() {
            graph.clear();
        if (moodArray.size() != 0) {
            dataSet = new LineDataSet(data(), "Label");
            setGraphColor();
            dataSet.setDrawCircleHole(false);
            dataSet.setLineWidth(2f);
            dataSet.setCircleRadius(4f);
            LineData lineData = new LineData(dataSet);
            lineData.setDrawValues(false);
            graph.setData(lineData);
        }
        graph.animateY(2000, Easing.EasingOption.EaseInOutElastic);
    }

    //formats graph
    public void formatGraph() {
        Log.d("DEBUGGING","FORMATTING GRAPH");
        graph.clear();
        if (moodArray.size() != 0) {
            dataSet = new LineDataSet(data(), "Label");
            setGraphColor();
            dataSet.setDrawCircleHole(false);
            dataSet.setLineWidth(2f); //sets width of graphed line
            dataSet.setCircleRadius(4f); //sets size of data points
            LineData lineData = new LineData(dataSet);
            lineData.setDrawValues(false);

            graph.setData(lineData);
            graph.setHighlightPerTapEnabled(false);
            graph.setHighlightPerDragEnabled(false);
            graph.getLegend().setEnabled(false); //disables legend
            graph.setDragXEnabled(true); //allows scrolling
            graph.setDoubleTapToZoomEnabled(false); //disables zooming
            graph.setVisibleXRangeMaximum((float) AlarmManager.INTERVAL_DAY * 4 + AlarmManager.INTERVAL_HALF_DAY / 2); //sets x range so that only 5 day's worth of data is shown at a time
            graph.moveViewToX((float) moodArray.get(moodArray.size() - 1).getDateObj().getTime() - referenceTimestamp - AlarmManager.INTERVAL_DAY * 2); //displays end of the graph where the data is the latest
            graph.getDescription().setEnabled(false); //disables graph caption
            IAxisValueFormatter xAxisFormatter = new DateAxisFormatter(moodArray.get(0).getDateObj().getTime());

            YAxis yAxis = graph.getAxisLeft();
            graph.getAxisRight().setEnabled(false); //disables right y axis
            yAxis.setAxisMinimum(1f);
            yAxis.setAxisMaximum(7f);
            yAxis.setGranularity(1f);
            yAxis.setLabelCount(7, true); //sets y axis labels to increment from 1 to 7
            XAxis xAxis = graph.getXAxis();
            xAxis.setValueFormatter(xAxisFormatter); //formats x axis labels to be dates
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //sets x axis to be at the bottom
            xAxis.setLabelCount(5, true);
            xAxis.setAvoidFirstLastClipping(true);
        }
        graph.animateY(2000, Easing.EasingOption.EaseInOutElastic); //animates graph when it is first created
    }

    //sets color of graph based on selection in dropdown menu
    public void setGraphColor () {
        int[] color;
        switch (spinner.getText().toString()) {
            case "Happy":
                color = new int[] {R.color.LightOrange};
                dataSet.setColors(color, getApplicationContext());
                dataSet.setCircleColors(color, getApplicationContext());
                break;
            case "Sad":
                color = new int[] {R.color.SadBlue};
                dataSet.setColors(color, getApplicationContext());
                dataSet.setCircleColors(color, getApplicationContext());
                break;
            case "Energized":
                color = new int[] {R.color.EnergizedGreen};
                dataSet.setColors(color, getApplicationContext());
                dataSet.setCircleColors(color, getApplicationContext());
                break;
            case "Irritated":
                color = new int[] {R.color.IrritatedRed};
                dataSet.setColors(color, getApplicationContext());
                dataSet.setCircleColors(color, getApplicationContext());
                break;
            case "Anxious":
                color = new int[] {R.color.AnxiousPurple};
                dataSet.setColors(color, getApplicationContext());
                dataSet.setCircleColors(color, getApplicationContext());
                break;
        }
    }
}

//Converts data to dates to be displayed on x axis
class DateAxisFormatter implements IAxisValueFormatter {
    private long referenceTimestamp; // minimum timestamp in data set
    private SimpleDateFormat mDataFormat;
    private Date mDate;

    DateAxisFormatter(long timestamp) {
        referenceTimestamp = timestamp;
        mDataFormat = new SimpleDateFormat("M/dd", Locale.ENGLISH);
        mDate = new Date();
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        //convertedTimestamp = originalTimestamp - referenceTimestamp
        long convertedTimestamp = (long) value;
        //retrieves original timestamp
        long originalTimestamp = referenceTimestamp + convertedTimestamp;
        //convert timestamp to hour:minute
        return getHour(originalTimestamp);
    }

    private String getHour(long timestamp){
        try{
            mDate.setTime(timestamp);
            return mDataFormat.format(mDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }
}

