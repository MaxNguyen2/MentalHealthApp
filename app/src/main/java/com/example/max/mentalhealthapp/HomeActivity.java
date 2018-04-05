package com.example.max.mentalhealthapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    Intent myIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button settings = (Button) findViewById(R.id.settingsButton);
        settings.setOnClickListener(this);
        Button mood = (Button) findViewById(R.id.moodButton);
        mood.setOnClickListener(this);
        Button safety = (Button) findViewById(R.id.safetyButton);
        safety.setOnClickListener(this);
        Button breathing = (Button) findViewById(R.id.breathingButton);
        breathing.setOnClickListener(this);

    }

    @Override
    public void onClick (View v){
        switch (v.getId()){
            case R.id.settingsButton: //goes to settings page when button is clicked
                myIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                HomeActivity.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.moodButton: //goes to mood monitoring page
                myIntent = new Intent(HomeActivity.this, MoodMonitoring.class);
                HomeActivity.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.safetyButton:
                myIntent = new Intent(HomeActivity.this, SafetyPlan.class);
                HomeActivity.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.breathingButton:
                myIntent = new Intent(HomeActivity.this, BreathingExercises.class);
                HomeActivity.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

}
