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

//Super class of DisablePassword and SetPassword
public class LockScreen extends AppCompatActivity implements View.OnClickListener{

    int digitCounter = 0; //counts how many digits have been entered

    //circles that show how many digits of password have been entered
    ImageView indicator1;
    ImageView indicator2;
    ImageView indicator3;
    ImageView indicator4;

    ArrayList<Integer> enteredPassword = new ArrayList<>(); //stores digits that user enters
    ArrayList<Integer> checkPassword = new ArrayList<>(); //stores digits of the set password
    Toast toast;
    Vibrator vibe;
    Animation shake;

    @Override
    public void onBackPressed () { //when the back button is pressed
        if (digitCounter > 0 ) { //when the user has entered at least one digit
            enteredPassword.remove(enteredPassword.size()-1); //removes the last entered digit
            switch (digitCounter) { //changes the appearance of the indicator circles to indicate that a digit has been removed
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

        //creates references to all the buttons of the number grid
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
        for (int i = 0; i < password.length(); i++) //stores the digits of the set password into the array
        {
            checkPassword.add(Character.getNumericValue(password.charAt(i)));
        }

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);

    }

    @Override
    public void onClick (View v){
    digitCounter++; //increases to indicate that a digit has been entered

        switch (digitCounter) { //makes the corresponding indicator circle to be filled to show that a digit has been entered
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

        switch (v.getId()) //adds digit to array that corresponds to button that is pressed
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

        if (digitCounter == 4) //when 4 digits have been entered
        check();
    }

    public void clearIndicators (){ //makes all the indicator circles empty to indicate that entered digits have been cleared
        indicator1.setImageResource(R.drawable.lockscreen_indicator_circle);
        indicator2.setImageResource(R.drawable.lockscreen_indicator_circle);
        indicator3.setImageResource(R.drawable.lockscreen_indicator_circle);
        indicator4.setImageResource(R.drawable.lockscreen_indicator_circle);
    }

    public void check() {
            if (enteredPassword.equals(checkPassword)) { //when entered password is the same as the stored password
                toast = Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT);
                Intent myIntent = new Intent(LockScreen.this, HomeActivity.class); //goes to home screen
                startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }

            else { //if password does not match, clears indicator circles and entered digits
                reset();
                toast = Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT);
            }
            toast.show(); //shows message if right or wrong password was entered
    }

    public void reset() {
        vibe.vibrate(100);
        indicator1.startAnimation(shake);
        indicator2.startAnimation(shake);
        indicator3.startAnimation(shake);
        indicator4.startAnimation(shake);
        clearIndicators();
        enteredPassword.clear();
        digitCounter = 0;
    }

}
