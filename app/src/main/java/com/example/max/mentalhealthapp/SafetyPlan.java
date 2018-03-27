package com.example.max.mentalhealthapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SafetyPlan extends SetupClass {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_safety_plan);
        super.onCreate(savedInstanceState);
        setupStatusBar(R.color.StatusRed);

        Button warningButton = (Button) findViewById(R.id.warningButton);
        warningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myIntent = new Intent(SafetyPlan.this, WarningSigns.class);
                startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        Button strategiesButton = (Button) findViewById(R.id.strategiesButton);
        strategiesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myIntent = new Intent(SafetyPlan.this, CopingStrategies.class);
                startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        Button familyButton = (Button) findViewById(R.id.familyButton);
        familyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myIntent = new Intent(SafetyPlan.this, FamilyContact.class);
                startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        Button proButton = (Button) findViewById(R.id.proButton);
        proButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myIntent = new Intent(SafetyPlan.this, ContactProfessionals.class);
                startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

}
