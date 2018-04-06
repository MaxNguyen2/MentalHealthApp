package com.example.max.mentalhealthapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

//Base class that sets up navigation drawer and toolbar so that all other activities can extend it
public class SetupClass extends AppCompatActivity {
    ListView mDrawerList;
    ArrayAdapter<String> mAdapter;
    Intent myIntent;
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //sets up navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupDrawer();
        mDrawerList = (ListView) findViewById(R.id.navList);
        addDrawerItems();
        setStatusBar(android.R.color.black);
    }

    //sets up navigation list with the pages for the different features
    public void addDrawerItems() {
        String[] navArray = {"Home", "Mood Monitoring", "Safety Plan", "Breathing Exercises", "Crisis Lines", "Information", "Settings"};
        mAdapter = new ArrayAdapter<>(this, R.layout.roboto_medium_list, navArray);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.bringToFront();

        //takes user to page of feature that they click on
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        myIntent = new Intent(SetupClass.this, HomeActivity.class);
                        SetupClass.this.startActivity(myIntent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case 1:
                        myIntent = new Intent(SetupClass.this, MoodMonitoring.class);
                        SetupClass.this.startActivity(myIntent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case 2:
                        myIntent = new Intent(SetupClass.this, SafetyPlan.class);
                        SetupClass.this.startActivity(myIntent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case 3:
                        myIntent = new Intent(SetupClass.this, BreathingExercises.class);
                        SetupClass.this.startActivity(myIntent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
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
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    //sets color of status bar
    public void setStatusBar(int color){
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), color));
    }
}
