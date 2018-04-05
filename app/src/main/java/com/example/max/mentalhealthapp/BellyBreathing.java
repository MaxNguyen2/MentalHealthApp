package com.example.max.mentalhealthapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class BellyBreathing extends SetupClass implements View.OnClickListener{
    int counter = 0;
    int repetitions = -2;
    TextView instructionText, counterText;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_belly_breathing);
        super.onCreate(savedInstanceState);
        setStatusBar(R.color.StatusBlue);

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
        Button exitButton = (Button) findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
        instructionText = (TextView) findViewById(R.id.instructionText);
        counterText = (TextView) findViewById(R.id.counterText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                switch (counter){
                    case 0:
                        counter++;
                        instructionText.setText("Put one hand on your belly just below your ribs and the other hand on your chest.");
                        nextButton.setText("Next Step");
                        repetitions++;
                        counterText.setText(String.valueOf(repetitions + 1));
                        break;
                    case 1:
                        counter++;
                        instructionText.setText("Take a deep breath in through your nose, and let your belly push your hand out.\nYour chest should not move.");
                        break;
                    case 2:
                        counter++;
                        instructionText.setText("Breathe out through pursed lips as if you were whistling.\nFeel the hand on your belly go in, and use it to push all the air out.");
                        break;
                    case 3:
                        counter = 0;
                        instructionText.setText("Do this breathing 3 to 10 times.\nTake your time with each breath.");
                        nextButton.setText("Repeat Exercise");
                        break;
                }
                break;
            case R.id.exitButton:
                myIntent = new Intent(BellyBreathing.this, BreathingExercises.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                BellyBreathing.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
        }
    }
}
