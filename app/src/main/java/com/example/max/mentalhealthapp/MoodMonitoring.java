package com.example.max.mentalhealthapp;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

//page where user decides whether to report a mood or view mood graphs
public class MoodMonitoring extends SetupClass {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mood_monitoring);
        super.onCreate(savedInstanceState);
        setupStatusBar(R.color.StatusOrange);

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


