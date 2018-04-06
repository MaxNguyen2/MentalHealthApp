package com.example.max.mentalhealthapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//Decides if lock screen or home screen should be opened on startup
public class ActivityLauncher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("key", Context.MODE_PRIVATE);

        Intent intent;
        boolean pin = prefs.getBoolean("pin", false); //checks if password protection feature is enabled
        if (pin) //if it is, then the lock screen is opened
            intent = new Intent(this, LockScreen.class);
        else //if password protection is disabled, then goes to home screen
            intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();


    }
}
