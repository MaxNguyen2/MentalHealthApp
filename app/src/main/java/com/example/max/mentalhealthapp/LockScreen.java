package com.example.max.mentalhealthapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class LockScreen extends AppCompatActivity implements View.OnClickListener{

    int digitCounter = 0;
    ImageView indicator1;
    ImageView indicator2;
    ImageView indicator3;
    ImageView indicator4;

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

    }

    @Override
    public void onClick (View v){
    digitCounter++;
        switch (digitCounter) {
            case 1:
                indicator1.setImageResource(R.drawable.my_button_bg_pressed);
                break;
            case 2:
                indicator2.setImageResource(R.drawable.my_button_bg_pressed);
                break;
            case 3:
                indicator3.setImageResource(R.drawable.my_button_bg_pressed);
                break;
            case 4:
                indicator4.setImageResource(R.drawable.my_button_bg_pressed);
                break;

        }
    }

}
