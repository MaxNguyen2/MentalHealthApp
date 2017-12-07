package com.example.max.mentalhealthapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class DisablePassword extends SetPassword {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("Enter Password to Disable");
    }

    @Override
    public void check(){
        if (digitCounter == 4){
            if (enteredPassword.equals(checkPassword)) {
                finish();
            }
            else {
                vibe.vibrate(100);
                indicator1.startAnimation(shake);
                indicator2.startAnimation(shake);
                indicator3.startAnimation(shake);
                indicator4.startAnimation(shake);
                clearIndicators();
            }
            enteredPassword.clear();
            digitCounter = 0;
        }
    }
}
