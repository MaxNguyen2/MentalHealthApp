package com.example.max.mentalhealthapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//Navigation page for the safety plan feature
public class SafetyPlan extends SetupClass implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_safety_plan);
        super.onCreate(savedInstanceState);
        setStatusBar(R.color.StatusRed);

        Button warningButton = (Button) findViewById(R.id.warningButton);
        warningButton.setOnClickListener(this);

        Button strategiesButton = (Button) findViewById(R.id.strategiesButton);
        strategiesButton.setOnClickListener(this);

        Button familyButton = (Button) findViewById(R.id.familyButton);
        familyButton.setOnClickListener(this);

        Button proButton = (Button) findViewById(R.id.proButton);
        proButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) { //goes to appropriate section of safety plan feature when button is clicked
            case R.id.warningButton:
                myIntent = new Intent(SafetyPlan.this, WarningSigns.class);
                startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.strategiesButton:
                myIntent = new Intent(SafetyPlan.this, CopingStrategies.class);
                startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.familyButton:
                myIntent = new Intent(SafetyPlan.this, FamilyContact.class);
                startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.proButton:
                myIntent = new Intent(SafetyPlan.this, ContactProfessionals.class);
                startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }
}
