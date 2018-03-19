package com.example.max.mentalhealthapp;

import android.os.Bundle;

public class SafetyPlan extends SetupClass {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_safety_plan);
        super.onCreate(savedInstanceState);
        setupStatusBar(R.color.StatusRed);
    }
}
