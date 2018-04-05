package com.example.max.mentalhealthapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//page where user decides whether to report a mood or view mood graphs
public class MoodMonitoring extends SetupClass {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mood_monitoring);
        super.onCreate(savedInstanceState);
        setStatusBar(R.color.StatusOrange);

        //when button is clicked, goes to mood reporting page
        Button button = (Button) findViewById(R.id.reportButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myIntent = new Intent(MoodMonitoring.this, MoodReporting.class);
                MoodMonitoring.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        Button graphButton = (Button) findViewById(R.id.graphButton);
        graphButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myIntent = new Intent(MoodMonitoring.this, MoodGraphs.class);
                MoodMonitoring.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
}


