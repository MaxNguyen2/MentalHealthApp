package com.example.max.mentalhealthapp;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

//Page for the final section of the safety plan feature where users can import contacts of professionals
public class ContactProfessionals extends FamilyContact {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        key = "proList"; //sets key for storing the list of contacts
        setArrayAdapter();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Contact Health Professional");

    }
}
