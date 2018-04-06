package com.example.max.mentalhealthapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

//Guides user through 4-7-8 breathing exercise
public class CountBreathing extends SetupClass implements View.OnClickListener{
    int counter = 0; //keeps track of step of breathing exercise
    int repetitions = -2; //keeps track of how many times exercise has been repeated, displays 0 first
    TextView instructionText, counterText, timerText;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_count_breathing);
        super.onCreate(savedInstanceState);
        setStatusBar(R.color.StatusBlue);

        //creates references to layout elements
        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
        Button exitButton = (Button) findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
        instructionText = (TextView) findViewById(R.id.instructionText);
        counterText = (TextView) findViewById(R.id.counterText);
        timerText = (TextView) findViewById(R.id.timerText);
        timerText.setVisibility(View.GONE); //makes text box that displays timer to be invisible since it is not being used yet
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                switch (counter){ //determines what is displayed based on what step of the exercise the user is on
                    case 0:
                        counter++;
                        instructionText.setText("Put one hand on your belly and the other on your chest.");
                        nextButton.setText("Next Step");
                        repetitions++;
                        counterText.setText(String.valueOf(repetitions + 1));
                        break;
                    case 1:
                        timerText.setText(String.valueOf(0));
                        counter++;
                        instructionText.setText("Take a deep,\nslow breath from your belly,\nand silently count to 4 as you breathe in.");
                        nextButton.setClickable(false); //disables button to go to the next step until timer finishes
                        nextButton.setAlpha(.5f);
                        timerText.setVisibility(View.VISIBLE);
                        new CountDownTimer(6000, 1000) { //sets and starts timer to go from 0 to 4
                            public void onTick(long millisUntilFinished) {
                                String text = String.valueOf(5-millisUntilFinished/1000);
                                timerText.setText(text); //displays timer value in text box
                                if (text.equals("4")){
                                    nextButton.setClickable(true);
                                    nextButton.setAlpha(1f);
                                }
                            }

                            public void onFinish() {
                            }
                        }.start();
                        break;
                    case 2:
                        timerText.setText(String.valueOf(0));
                        counter++;
                        instructionText.setText("Hold your breath,\nand silently count from 1 to 7.");
                        nextButton.setClickable(false);
                        nextButton.setAlpha(.5f);
                        new CountDownTimer(9000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                String text = String.valueOf(8-millisUntilFinished/1000);
                                timerText.setText(text);
                                if (text.equals("7")){
                                    nextButton.setClickable(true);
                                    nextButton.setAlpha(1f);
                                }
                            }

                            public void onFinish() {
                            }
                        }.start();
                        break;
                    case 3:
                        timerText.setText(String.valueOf(0));
                        counter++;
                        instructionText.setText("Breathe out completely as you silently count from 1 to 8.\nTry to get all the air out of your lungs by the time you count to 8.");
                        nextButton.setClickable(false);
                        nextButton.setAlpha(.5f);
                        new CountDownTimer(10000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                String text = String.valueOf(9-millisUntilFinished/1000);
                                timerText.setText(text);
                                if (text.equals("8")){
                                    nextButton.setClickable(true);
                                    nextButton.setAlpha(1f);
                                }
                            }

                            public void onFinish() {
                            }
                        }.start();
                        break;
                    case 4:
                        counter = 0;
                        timerText.setVisibility(View.GONE);
                        instructionText.setText("Repeat 3 to 7 times or until you feel calm.");
                        nextButton.setText("Repeat Exercise");
                        break;
                }
                break;
            case R.id.exitButton: //sends user to breathing exercises navigation page
                myIntent = new Intent(CountBreathing.this, BreathingExercises.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                CountBreathing.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
        }
    }

}
