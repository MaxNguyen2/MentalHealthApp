package com.example.max.mentalhealthapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class LockScreen extends AppCompatActivity implements View.OnClickListener{

    int digitCounter = 0;
    ImageView indicator1;
    ImageView indicator2;
    ImageView indicator3;
    ImageView indicator4;
    ArrayList<Integer> enteredPassword = new ArrayList<>();
    ArrayList<Integer> checkPassword = new ArrayList<>();
    Toast toast;
    Vibrator vibe;
    Animation shake;

    @Override
    public void onBackPressed () {
        if (enteredPassword.size() > 0)
            enteredPassword.remove(enteredPassword.size()-1);
        if (digitCounter > 0 ) {
            switch (digitCounter) {
                case 1:
                    indicator1.setImageResource(R.drawable.lockscreen_indicator_circle);
                    break;
                case 2:
                    indicator2.setImageResource(R.drawable.lockscreen_indicator_circle);
                    break;
                case 3:
                    indicator3.setImageResource(R.drawable.lockscreen_indicator_circle);
                    break;
            }
            digitCounter--;
        }
        else
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        Button one = (Button) findViewById(R.id.button1);
        one.setOnClickListener(this);
        Button two = (Button) findViewById(R.id.button2);
        two.setOnClickListener(this);
        Button three = (Button) findViewById(R.id.button3);
        three.setOnClickListener(this);
        Button four = (Button) findViewById(R.id.button4);
        four.setOnClickListener(this);
        Button five = (Button) findViewById(R.id.button5);
        five.setOnClickListener(this);
        Button six = (Button) findViewById(R.id.button6);
        six.setOnClickListener(this);
        Button seven = (Button) findViewById(R.id.button7);
        seven.setOnClickListener(this);
        Button eight = (Button) findViewById(R.id.button8);
        eight.setOnClickListener(this);
        Button nine = (Button) findViewById(R.id.button9);
        nine.setOnClickListener(this);
        Button zero = (Button) findViewById(R.id.button0);
        zero.setOnClickListener(this);

        indicator1 = (ImageView) findViewById(R.id.indicator1);
        indicator2 = (ImageView) findViewById(R.id.indicator2);
        indicator3 = (ImageView) findViewById(R.id.indicator3);
        indicator4 = (ImageView) findViewById(R.id.indicator4);

        SharedPreferences prefs = getSharedPreferences("key", Context.MODE_PRIVATE);
        String password = prefs.getString("password","");
        for (int i = 0; i < password.length(); i++)
        {
            checkPassword.add(Character.getNumericValue(password.charAt(i)));
        }

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);

    }

    @Override
    public void onClick (View v){
    digitCounter++;

        switch (digitCounter) {
            case 1:
                indicator1.setImageResource(R.drawable.indicator_circle_filled);
                break;
            case 2:
                indicator2.setImageResource(R.drawable.indicator_circle_filled);
                break;
            case 3:
                indicator3.setImageResource(R.drawable.indicator_circle_filled);
                break;
            case 4:
                indicator4.setImageResource(R.drawable.indicator_circle_filled);
                break;
        }

        switch (v.getId())
        {
            case (R.id.button1):
                enteredPassword.add(1);
                break;
            case (R.id.button2):
                enteredPassword.add(2);
                break;
            case (R.id.button3):
                enteredPassword.add(3);
                break;
            case (R.id.button4):
                enteredPassword.add(4);
                break;
            case (R.id.button5):
                enteredPassword.add(5);
                break;
            case (R.id.button6):
                enteredPassword.add(6);
                break;
            case (R.id.button7):
                enteredPassword.add(7);
                break;
            case (R.id.button8):
                enteredPassword.add(8);
                break;
            case (R.id.button9):
                enteredPassword.add(9);
                break;
            case (R.id.button0):
                enteredPassword.add(0);
                break;
        }
        check();
    }

    public void clearIndicators (){
        indicator1.setImageResource(R.drawable.lockscreen_indicator_circle);
        indicator2.setImageResource(R.drawable.lockscreen_indicator_circle);
        indicator3.setImageResource(R.drawable.lockscreen_indicator_circle);
        indicator4.setImageResource(R.drawable.lockscreen_indicator_circle);
    }

    public void check () {
        if (digitCounter == 4){
            if (enteredPassword.equals(checkPassword)) {
                toast = Toast.makeText(getApplicationContext(), "Right", Toast.LENGTH_SHORT);
                Intent myIntent = new Intent(LockScreen.this, HomeActivity.class);
                startActivity(myIntent);
            }
            else {
                vibe.vibrate(100);
                toast = Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT);
                indicator1.startAnimation(shake);
                indicator2.startAnimation(shake);
                indicator3.startAnimation(shake);
                indicator4.startAnimation(shake);
                clearIndicators();
            }

            enteredPassword.clear();
            digitCounter = 0;
            toast.show();
        }
    }

}
