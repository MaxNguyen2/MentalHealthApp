package com.example.max.mentalhealthapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button settings = (Button) findViewById(R.id.settingsButton);
        settings.setOnClickListener(this);

    }

    @Override
    public void onClick (View v){
        switch (v.getId()){
            case R.id.settingsButton:
                Intent myIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                HomeActivity.this.startActivity(myIntent);
                break;
        }
    }

}
