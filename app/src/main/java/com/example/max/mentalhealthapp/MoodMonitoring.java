package com.example.max.mentalhealthapp;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

//Page where user decides whether to report a mood or view mood graphs
public class MoodMonitoring extends AppCompatActivity {
    ListView mDrawerList;
    ArrayAdapter<String> mAdapter;
    Intent myIntent;
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_monitoring);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //sets up navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupDrawer();
        mDrawerList = (ListView) findViewById(R.id.navList);
        addDrawerItems();

        //when button is clicked, goes to mood reporting page
        Button button = (Button) findViewById(R.id.reportButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myIntent = new Intent(MoodMonitoring.this, MoodReporting.class);
                MoodMonitoring.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    //sets up navigation list with the pages for the different features
    public void addDrawerItems() {
        String[] navArray = {"Home", "Mood Monitoring", "Safety Plan", "Breathing Exercises", "Crisis Lines", "Information", "Settings"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navArray);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.bringToFront();

        //takes user to page of feature that they click on
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    myIntent = new Intent(MoodMonitoring.this, HomeActivity.class);
                    MoodMonitoring.this.startActivity(myIntent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else if (position == 1) {
                    myIntent = new Intent(MoodMonitoring.this, MoodMonitoring.class);
                    MoodMonitoring.this.startActivity(myIntent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    //helps set up navigation drawer to be accessed by toolbar
    public void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
        };
        mDrawerToggle.syncState(); //needed to make hamburger icon functional
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_action_name); //specifies hamburger icon
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


