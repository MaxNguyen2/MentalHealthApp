package com.example.max.mentalhealthapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

//disables the password and is called from SettingsActivity
public class DisablePassword extends SetPassword {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("Enter Password to Disable"); //changes title text
    }

    @Override
    public void check(){
            if (enteredPassword.equals(checkPassword)) { //checks if entered password is the set password
                SharedPreferences prefs = this.getSharedPreferences("key", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("password", "");
                editor.apply();
                finish(); //ends activity if they are equal
            }
            else { //clears indicator circles and makes them shake to indicate that wrong password was entered
                reset();
            }
    }
}
