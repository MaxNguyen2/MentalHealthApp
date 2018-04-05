package com.example.max.mentalhealthapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class BreathingExercises extends SetupClass implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_breathing_exercises);
        super.onCreate(savedInstanceState);
        setStatusBar(R.color.StatusBlue);

        Button belly = (Button) findViewById(R.id.bellyButton);
        belly.setOnClickListener(this);
        Button morning = (Button) findViewById(R.id.morningButton);
        morning.setOnClickListener(this);
        Button countButton = (Button) findViewById(R.id.countButton);
        countButton.setOnClickListener(this);

        ImageView info = (ImageView) findViewById(R.id.infoIcon);
        info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BreathingExercises.this);
                builder.setTitle("Tips")
                        .setMessage("Belly breathing is the easiest technique.\nMorning breathing is great for muscle stiffness and clogged breathing passages.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.bellyButton:
                myIntent = new Intent(BreathingExercises.this, BellyBreathing.class);
                BreathingExercises.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.morningButton:
                myIntent = new Intent(BreathingExercises.this, MorningBreathing.class);
                BreathingExercises.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.countButton:
                myIntent = new Intent(BreathingExercises.this, CountBreathing.class);
                BreathingExercises.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }


}
