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

//Sets password and the password has to be entered twice to confirm it
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
            if (!confirm) { //if this is the first time a password was entered
                checkPassword = new ArrayList<>(enteredPassword); //sets checkPassword to equal enteredPassword
                confirm = true; //sets variable to true to indicate that a password has already been entered
                enteredPassword.clear();
                digitCounter = 0;
                clearIndicators();
                title.setText("Confirm Password"); //changes title to indicate that password should be re-entered
            }
            else if (checkPassword.equals(enteredPassword)) { //if the re-entered password equals the password that was first entered
                String stringPassword = "";
                for (Integer i : enteredPassword) { //converts array to string so that it can be stored
                    stringPassword = stringPassword + i.toString();
                }
                SharedPreferences prefs = this.getSharedPreferences("key", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("password", stringPassword);
                editor.apply();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
            else { //if re-entered password does not equal the password that was first entered, makes user try again
                reset();
                title.setText("Set Password");
                checkPassword.clear();
                confirm = false;
            }
    }
}


