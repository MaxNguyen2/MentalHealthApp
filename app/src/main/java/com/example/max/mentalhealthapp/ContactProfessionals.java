package com.example.max.mentalhealthapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class ContactProfessionals extends FamilyContact {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        key = "proList";
        setArrayAdapter();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Contact Health Professional");

    }
}
