package com.example.max.mentalhealthapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SetPassword extends LockScreen{

    boolean confirm = false;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = (TextView) findViewById(R.id.title);
        title.setText("Set Password");
    }

    @Override
    public void check() {
        if (digitCounter == 4) {
            if (!confirm) {
                checkPassword = new ArrayList<>(enteredPassword);
                confirm = true;
                enteredPassword.clear();
                digitCounter = 0;
                clearIndicators();
                title.setText("Confirm Password");
            }
            else if (checkPassword.equals(enteredPassword)) {
                String stringPassword = "";
                for (Integer i : enteredPassword) {
                    stringPassword = stringPassword + i.toString();
                }
                SharedPreferences prefs = this.getSharedPreferences("key", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("password", stringPassword);
                editor.apply();
                finish();
            }
            else {
                clearIndicators();
                vibe.vibrate(100);
                digitCounter = 0;
                title.setText("Set Password");
                indicator1.startAnimation(shake);
                indicator2.startAnimation(shake);
                indicator3.startAnimation(shake);
                indicator4.startAnimation(shake);
                enteredPassword.clear();
                checkPassword.clear();
                confirm = false;
            }
        }
    }
}


