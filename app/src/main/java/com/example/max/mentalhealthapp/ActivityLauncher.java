package com.example.max.mentalhealthapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ActivityLauncher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("key", Context.MODE_PRIVATE);
        Intent intent;
        boolean pin = prefs.getBoolean("pin", false);
        if (pin)
            intent = new Intent(this, LockScreen.class);
        else
            intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();


    }
}
