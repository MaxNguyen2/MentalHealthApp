package com.example.max.mentalhealthapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

public class CopingStrategies extends WarningSigns {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Coping Strategies");
        setupStatusBar(R.color.StatusRed);

        key = "strategiesList";
        setArrayAdapter();
    }

    @Override
    public void setListListener(){
        signsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CopingStrategies.this);
                builder.setTitle("Delete Coping Strategy")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeIndex = position;
                                view.startAnimation(animation);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void setInfoDialog() {
        ImageView info = (ImageView) findViewById(R.id.infoIcon);
        info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CopingStrategies.this);
                builder.setTitle("Tips")
                        .setMessage("Enter in your personal coping strategies to use during a crisis. This can include relaxation techniques, a distracting physical activity, or a safe social setting to go to.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });
    }
}
