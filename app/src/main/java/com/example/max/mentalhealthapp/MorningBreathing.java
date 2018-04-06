package com.example.max.mentalhealthapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

//Guides user through morning breathing exercise
public class MorningBreathing extends BellyBreathing {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Morning Breathing");
        instructionText.setText("From a standing position,\nbend forward from the waist with your knees slightly bent,\nletting your arms dangle close to the floor.");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                switch (counter){ //determines what to display based on what step of the exercise the user is on
                    case 0:
                        counter++;
                        instructionText.setText("As you inhale slowly and deeply,\nreturn to a standing position by rolling up slowing,\nlifting your head last.");
                        nextButton.setText("Next Step");
                        repetitions++;
                        counterText.setText(String.valueOf(repetitions + 1));
                        break;
                    case 1:
                        counter++;
                        instructionText.setText("Hold your breath for just a few seconds in this standing position.");
                        break;
                    case 2:
                        counter = 0;
                        instructionText.setText("Exhale slowly as you return to the original position,\nbending forward from the waist.");
                        nextButton.setText("Repeat Exercise");
                        break;
                }
                break;
            case R.id.exitButton: //goes to breathing exercises navigation page
                myIntent = new Intent(MorningBreathing.this, BreathingExercises.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                MorningBreathing.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
        }
    }
}
